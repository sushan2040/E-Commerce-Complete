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

import com.example.ecommerce.configuration.beans.Level3MasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.CountryMaster;
import com.example.ecommerce.configuration.masters.Level2Master;
import com.example.ecommerce.configuration.masters.Level3Master;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class LocationLevel3RepoImpl implements LocationLevel3Repo {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Long saveLevel3Master(Level3MasterBean bean) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level3Master level3Master = new Level3Master();
            BeanUtils.copyProperties(bean, level3Master);

            session.merge(level3Master); // Save or update the entity
            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAllLevel3s(),RedisKey.LOCATION_LEVEL3_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalCountriesCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllLevel3sPagination(0, 0));
            	RedisUtils.refreshRedisDataAll(RedisKey.LOCATION_LEVEL3_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.LOCATION_LEVEL3_PAGINATION.getKey(i,10));
            }

            return 1L;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback if error occurs
            }
            e.printStackTrace();
            return 0L;
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
    }

    @Override
    public List<Level3MasterBean> fetchAllLevel3s() {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Level3Master> countryQuery = builder.createQuery(Level3Master.class);
            Root<Level3Master> root = countryQuery.from(Level3Master.class);

            Predicate where = builder.equal(root.get("status"), builder.parameter(String.class, "status"));
            where = builder.and(where, builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted")));
            countryQuery.where(where);

            Query<Level3Master> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setCacheable(true);
            countryquery.setCacheRegion("level3MasterCache");
            List<Level3Master> countryList = countryquery.getResultList();
            List<Level3MasterBean> countryMapList = countryList.stream().map(obj -> {
                Level3MasterBean bean = new Level3MasterBean();
                BeanUtils.copyProperties(obj, bean);
                return bean;
            }).collect(Collectors.toList());

            transaction.commit(); // Commit the transaction
            return countryMapList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback if error occurs
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
    }

    @Override
    public List<Level3MasterBean> getAllLevel3sPagination(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Level3MasterBean> countryQuery = builder.createQuery(Level3MasterBean.class);
            Root<Level3Master> root = countryQuery.from(Level3Master.class);
            Root<Level2Master> rootCountry = countryQuery.from(Level2Master.class);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where = builder.and(where, builder.equal(root.get("level2Id"), rootCountry.get("level2Id")));
            countryQuery.where(where);

            countryQuery.select(builder.construct(Level3MasterBean.class,
                    root.get("level3Id"),
                    root.get("level2Id"),
                    root.get("level3Name"),
                    rootCountry.get("level2Name"),
                    root.get("status")
            ));

            Query<Level3MasterBean> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);

            if ((page - 1) >= 0)
                countryquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
            else
                countryquery.setFirstResult(0);

            countryquery.setMaxResults(per_page);

            List<Level3MasterBean> countryBeanList = countryquery.getResultList();

            if (!countryBeanList.isEmpty()) {
                int totalRecords = getTotalCountriesCount(page, per_page);
                countryBeanList.get(0).setTotalRecords(totalRecords);
            }

            transaction.commit(); // Commit the transaction
            return countryBeanList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback if error occurs
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
    }

    private int getTotalCountriesCount(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> countryQuery = builder.createQuery(Long.class);
            Root<Level3Master> root = countryQuery.from(Level3Master.class);
            countryQuery.select(builder.count(root));

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            countryQuery.where(where);

            Query<Long> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);

            Long countryObjCount = countryquery.uniqueResult();

            transaction.commit(); // Commit the transaction
            return countryObjCount.intValue();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback if error occurs
            }
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
    }

    @Override
    public Long deleteLevel3Id(Integer level3Id) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level3Master countryMaster = session.get(Level3Master.class, level3Id);
            countryMaster.setDeleted(Constants.DELETED);
            session.persist(countryMaster);

            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAllLevel3s(),RedisKey.LOCATION_LEVEL3_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalCountriesCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllLevel3sPagination(0, 0));
            	RedisUtils.refreshRedisDataAll(RedisKey.LOCATION_LEVEL3_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.LOCATION_LEVEL3_PAGINATION.getKey(i,10));
            }
            
            return 1L;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback if error occurs
            }
            e.printStackTrace();
            return 0L;
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
    }

    @Override
    public Level3MasterBean getLevelBy3ById(Integer level3Id) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level3Master countryMaster = session.get(Level3Master.class, level3Id);
            Level3MasterBean bean = new Level3MasterBean();
            BeanUtils.copyProperties(countryMaster, bean);

            transaction.commit(); // Commit the transaction
            return bean;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback if error occurs
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
    }

	@Override
	public List<Level3MasterBean> getLocationChildByParent(Integer parentId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Level3MasterBean> level3Query=builder.createQuery(Level3MasterBean.class);
		Root<Level3Master> root=level3Query.from(Level3Master.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("level2Id"),builder.parameter(Integer.class,"level2Id")));
		level3Query.where(where);
		level3Query.select(builder.construct(Level3MasterBean.class, 
				root.get("level3Id"),
				root.get("level3Name")
				));
		Query<Level3MasterBean> level3query=session.createQuery(level3Query);
		level3query.setParameter("deleted",Constants.NOT_DELETED);
		level3query.setParameter("level2Id",parentId);
		List<Level3MasterBean> level3=level3query.getResultList();
		session.getTransaction().commit();
		return level3;
	}
}
