package com.example.ecommerce.usersrepo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.LocationLevelBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.CountryMaster;
import com.example.ecommerce.configuration.masters.LocationLevelMaster;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class LocationLevelDaoImpl implements LocationLevelDao {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Long saveLocationLevelMaster(LocationLevelBean bean) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            LocationLevelMaster locationLevelMaster = new LocationLevelMaster();
            BeanUtils.copyProperties(bean, locationLevelMaster);
            locationLevelMaster.setDeleted(Constants.NOT_DELETED);
            locationLevelMaster.setStatus(Constants.STATUS_ACTIVE);

            session.merge(locationLevelMaster);
            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAllLocationLevels(),RedisKey.LOCATION_LEVELS_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalLocationLevelsCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllLocationLevelsPagination(0, 10));
            	RedisUtils.refreshRedisDataAll(RedisKey.LOCATION_LEVELS_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.LOCATION_LEVELS_PAGINATION.getKey(i,10));
            }

            return 1L;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return 0L;
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed after completion
            }
        }
    }

    @Override
    public List<LocationLevelBean> fetchAllLocationLevels() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<LocationLevelMaster> locationLevelQuery = builder.createQuery(LocationLevelMaster.class);
            Root<LocationLevelMaster> root = locationLevelQuery.from(LocationLevelMaster.class);

            Predicate where = builder.equal(root.get("status"), builder.parameter(String.class, "status"));
            where = builder.and(where, builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted")));
            locationLevelQuery.where(where);

            Query<LocationLevelMaster> locationLevelQueryExec = session.createQuery(locationLevelQuery);
            locationLevelQueryExec.setParameter("status", Constants.STATUS_ACTIVE);
            locationLevelQueryExec.setParameter("deleted", Constants.NOT_DELETED);
            locationLevelQueryExec.setCacheable(true);
            locationLevelQueryExec.setCacheRegion("locationLevelMasterCache");
            List<LocationLevelMaster> locationLevelList = locationLevelQueryExec.getResultList();
            List<LocationLevelBean> locationLevelMapList = locationLevelList.stream().map(obj -> {
                LocationLevelBean bean = new LocationLevelBean();
                BeanUtils.copyProperties(obj, bean);
                return bean;
            }).collect(Collectors.toList());

            transaction.commit(); // Commit the transaction
            return locationLevelMapList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed after completion
            }
        }
    }

    @Override
    public List<LocationLevelBean> getAllLocationLevelsPagination(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<LocationLevelBean> locationLevelQuery = builder.createQuery(LocationLevelBean.class);
            Root<LocationLevelMaster> root = locationLevelQuery.from(LocationLevelMaster.class);
            Root<CountryMaster> rootCountry = locationLevelQuery.from(CountryMaster.class);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where = builder.and(where, builder.equal(root.get("countryId"), rootCountry.get("countryId")));
            locationLevelQuery.where(where);

            locationLevelQuery.select(builder.construct(LocationLevelBean.class,
                    root.get("locationLevelId"),
                    root.get("status"),
                    rootCountry.get("countryName")
            ));

            Query<LocationLevelBean> locationLevelQueryExec = session.createQuery(locationLevelQuery);
            locationLevelQueryExec.setParameter("deleted", Constants.NOT_DELETED);
            locationLevelQueryExec.setParameter("status", Constants.STATUS_ACTIVE);

            if ((page - 1) >= 0)
                locationLevelQueryExec.setFirstResult((page - 1) * per_page); // Correct offset
            else
                locationLevelQueryExec.setFirstResult(0);

            locationLevelQueryExec.setMaxResults(per_page);

            List<LocationLevelBean> locationLevelBeanList = locationLevelQueryExec.getResultList();

            if (!locationLevelBeanList.isEmpty()) {
                int totalRecords = getTotalLocationLevelsCount(page, per_page);
                locationLevelBeanList.get(0).setTotalRecords(totalRecords);
            }

            transaction.commit(); // Commit the transaction
            return locationLevelBeanList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed after completion
            }
        }
    }

    private int getTotalLocationLevelsCount(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> locationLevelQuery = builder.createQuery(Long.class);
            Root<LocationLevelMaster> root = locationLevelQuery.from(LocationLevelMaster.class);

            locationLevelQuery.select(builder.count(root));

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            locationLevelQuery.where(where);

            Query<Long> locationLevelQueryExec = session.createQuery(locationLevelQuery);
            locationLevelQueryExec.setParameter("deleted", Constants.NOT_DELETED);
            locationLevelQueryExec.setParameter("status", Constants.STATUS_ACTIVE);

            Long locationLevelObjCount = locationLevelQueryExec.uniqueResult();

            transaction.commit(); // Commit the transaction
            return locationLevelObjCount.intValue();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed after completion
            }
        }
    }

    @Override
    public LocationLevelBean getLocationLevelById(Integer locationLevelId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            LocationLevelMaster locationLevelMaster = session.get(LocationLevelMaster.class, locationLevelId);
            LocationLevelBean bean = new LocationLevelBean();
            BeanUtils.copyProperties(locationLevelMaster, bean);

            bean.setLocationLevel(getLocationLevelsByCountryId(bean.getCountryId()));

            transaction.commit(); // Commit the transaction
            return bean;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed after completion
            }
        }
    }

    @Override
    public Long deletelocationLevelId(Integer locationLevelId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            LocationLevelMaster locationLevelMaster = session.get(LocationLevelMaster.class, locationLevelId);
            locationLevelMaster.setDeleted(Constants.DELETED);
            session.persist(locationLevelMaster);

            transaction.commit(); // Commit the transaction
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAllLocationLevels(),RedisKey.LOCATION_LEVELS_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalLocationLevelsCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllLocationLevelsPagination(0, 10));
            	RedisUtils.refreshRedisDataAll(RedisKey.LOCATION_LEVELS_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.LOCATION_LEVELS_PAGINATION.getKey(i,10));
            }
            return 1L;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return 0L;
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed after completion
            }
        }
    }

    @Override
    public Integer getLocationLevelsByCountryId(Integer countryId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Integer> locationLevel = builder.createQuery(Integer.class);
            Root<CountryMaster> root = locationLevel.from(CountryMaster.class);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where = builder.and(where, builder.equal(root.get("countryId"), builder.parameter(Integer.class, "countryId")));
            locationLevel.where(where);
            locationLevel.select(root.get("countryLocationLevels"));

            Query<Integer> locationQuery = session.createQuery(locationLevel);
            locationQuery.setParameter("deleted", Constants.NOT_DELETED);
            locationQuery.setParameter("status", Constants.STATUS_ACTIVE);
            locationQuery.setParameter("countryId", countryId);

            Integer locationLevelResult = locationQuery.uniqueResult();

            transaction.commit(); // Commit the transaction
            return locationLevelResult;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed after completion
            }
        }
    }
}
