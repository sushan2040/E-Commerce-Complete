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
import com.example.ecommerce.configuration.beans.Level4MasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.Level3Master;
import com.example.ecommerce.configuration.masters.Level4Master;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class LocationLevel4RepoImpl implements LocationLevel4Repo {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Long saveLevel4Master(Level4MasterBean bean) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level4Master level4Master = new Level4Master();
            BeanUtils.copyProperties(bean, level4Master);

            session.merge(level4Master); // Save or update the entity
            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAllLevel4s(),RedisKey.LOCATION_LEVEL4_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalCountriesCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllLevel4sPagination(0, 0));
            	RedisUtils.refreshRedisDataAll(RedisKey.LOCATION_LEVEL4_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.LOCATION_LEVEL4_PAGINATION.getKey(i,10));
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
    public List<Level4MasterBean> fetchAllLevel4s() {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Level4Master> countryQuery = builder.createQuery(Level4Master.class);
            Root<Level4Master> root = countryQuery.from(Level4Master.class);

            Predicate where = builder.equal(root.get("status"), builder.parameter(String.class, "status"));
            where = builder.and(where, builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted")));
            countryQuery.where(where);

            Query<Level4Master> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setCacheable(true);
            countryquery.setCacheRegion("level4MasterCache");
            List<Level4Master> countryList = countryquery.getResultList();
            List<Level4MasterBean> countryMapList = countryList.stream().map(obj -> {
                Level4MasterBean bean = new Level4MasterBean();
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
    public List<Level4MasterBean> getAllLevel4sPagination(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Level4MasterBean> countryQuery = builder.createQuery(Level4MasterBean.class);
            Root<Level4Master> root = countryQuery.from(Level4Master.class);
            Root<Level3Master> rootCountry = countryQuery.from(Level3Master.class);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where = builder.and(where, builder.equal(root.get("level3Id"), rootCountry.get("level3Id")));
            countryQuery.where(where);

            countryQuery.select(builder.construct(Level4MasterBean.class,
                    root.get("level4Id"),
                    root.get("level3Id"),
                    root.get("level4Name"),
                    rootCountry.get("level3Name"),
                    root.get("status")
            ));

            Query<Level4MasterBean> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);

            if ((page - 1) >= 0)
                countryquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
            else
                countryquery.setFirstResult(0);

            countryquery.setMaxResults(per_page);

            List<Level4MasterBean> countryBeanList = countryquery.getResultList();

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
            Root<Level4Master> root = countryQuery.from(Level4Master.class);
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
    public Long deleteLevel4Id(Integer level4Id) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level4Master level4Master = session.get(Level4Master.class, level4Id);
            level4Master.setDeleted(Constants.DELETED);
            session.persist(level4Master);

            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAllLevel4s(),RedisKey.LOCATION_LEVEL4_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalCountriesCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllLevel4sPagination(0, 0));
            	RedisUtils.refreshRedisDataAll(RedisKey.LOCATION_LEVEL4_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.LOCATION_LEVEL4_PAGINATION.getKey(i,10));
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
    public Level4MasterBean getLevel4ById(Integer level4Id) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level4Master level4Master = session.get(Level4Master.class, level4Id);
            Level4MasterBean bean = new Level4MasterBean();
            BeanUtils.copyProperties(level4Master, bean);

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
	public List<Level4MasterBean> getLocationChildByParent(Integer parentId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Level4MasterBean> level4Query=builder.createQuery(Level4MasterBean.class);
		Root<Level4Master> root=level4Query.from(Level4Master.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("level3Id"),builder.parameter(Integer.class,"level3Id")));
		level4Query.where(where);
		level4Query.select(builder.construct(Level4MasterBean.class, 
				root.get("level4Id"),
				root.get("level4Name")
				));
		Query<Level4MasterBean> level4query=session.createQuery(level4Query);
		level4query.setParameter("deleted",Constants.NOT_DELETED);
		level4query.setParameter("level3Id",parentId);
		List<Level4MasterBean> level4=level4query.getResultList();
		session.getTransaction().commit();
		session.close();
		return level4;
	}
}
