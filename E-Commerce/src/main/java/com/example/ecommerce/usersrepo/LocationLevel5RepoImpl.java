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

import com.example.ecommerce.configuration.beans.Level4MasterBean;
import com.example.ecommerce.configuration.beans.Level5MasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.Level4Master;
import com.example.ecommerce.configuration.masters.Level5Master;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class LocationLevel5RepoImpl implements LocationLevel5Repo {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Long savelevel5Master(Level5MasterBean bean) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level5Master level5Master = new Level5Master();
            BeanUtils.copyProperties(bean, level5Master);

            session.merge(level5Master); // Save or update the entity
            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAlllevel5s(),RedisKey.LOCATION_LEVELS_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalCountriesCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAlllevel5sPagination(0, 0));
            	RedisUtils.refreshRedisDataAll(RedisKey.LOCATION_LEVEL5_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.LOCATION_LEVEL5_PAGINATION.getKey(i,10));
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
                session.close(); // Close session after completion
            }
        }
    }

    @Override
    public List<Level5MasterBean> fetchAlllevel5s() {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Level5Master> countryQuery = builder.createQuery(Level5Master.class);
            Root<Level5Master> root = countryQuery.from(Level5Master.class);

            Predicate where = builder.equal(root.get("status"), builder.parameter(String.class, "status"));
            where = builder.and(where, builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted")));
            countryQuery.where(where);

            Query<Level5Master> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);

            List<Level5Master> countryList = countryquery.getResultList();
            List<Level5MasterBean> countryMapList = countryList.stream().map(obj -> {
                Level5MasterBean bean = new Level5MasterBean();
                BeanUtils.copyProperties(obj, bean);
                return bean;
            }).collect(Collectors.toList());

            transaction.commit(); // Commit the transaction
            return countryMapList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close(); // Close session after completion
            }
        }
    }

    @Override
    public List<Level5MasterBean> getAlllevel5sPagination(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Level5MasterBean> countryQuery = builder.createQuery(Level5MasterBean.class);
            Root<Level5Master> root = countryQuery.from(Level5Master.class);
            Root<Level4Master> rootCountry = countryQuery.from(Level4Master.class);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where = builder.and(where, builder.equal(root.get("level4Id"), rootCountry.get("level4Id")));
            countryQuery.where(where);

            countryQuery.select(builder.construct(Level5MasterBean.class,
                    root.get("level5Id"),
                    root.get("level4Id"),
                    root.get("level5Name"),
                    rootCountry.get("level4Name"),
                    root.get("status")
            ));

            Query<Level5MasterBean> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);

            if ((page - 1) >= 0)
                countryquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
            else
                countryquery.setFirstResult(0);

            countryquery.setMaxResults(per_page);

            List<Level5MasterBean> countryBeanList = countryquery.getResultList();

            if (!countryBeanList.isEmpty()) {
                int totalRecords = getTotalCountriesCount(page, per_page);
                countryBeanList.get(0).setTotalRecords(totalRecords);
            }

            transaction.commit(); // Commit the transaction
            return countryBeanList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close(); // Close session after completion
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
            Root<Level5Master> root = countryQuery.from(Level5Master.class);
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
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close(); // Close session after completion
            }
        }
    }

    @Override
    public Long deletelevel5Id(Integer level5Id) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level5Master level5Master = session.get(Level5Master.class, level5Id);
            level5Master.setDeleted(Constants.DELETED);
            session.persist(level5Master);

            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAlllevel5s(),RedisKey.LOCATION_LEVEL5_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalCountriesCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAlllevel5sPagination(0, 0));
            	RedisUtils.refreshRedisDataAll(RedisKey.LOCATION_LEVEL5_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.LOCATION_LEVEL5_PAGINATION.getKey(i,10));
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
                session.close(); // Close session after completion
            }
        }
    }

    @Override
    public Level5MasterBean getlevel5ById(Integer level5Id) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level5Master level5Master = session.get(Level5Master.class, level5Id);
            Level5MasterBean bean = new Level5MasterBean();
            BeanUtils.copyProperties(level5Master, bean);

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
                session.close(); // Close session after completion
            }
        }
    }

	@Override
	public List<Level5MasterBean> getLocationChildByParent(Integer parentId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Level5MasterBean> level4Query=builder.createQuery(Level5MasterBean.class);
		Root<Level5Master> root=level4Query.from(Level5Master.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("level4Id"),builder.parameter(Integer.class,"level4Id")));
		level4Query.where(where);
		level4Query.select(builder.construct(Level5MasterBean.class,
				root.get("level5Id"),
				root.get("level5Name")
				));
		Query<Level5MasterBean> level4query=session.createQuery(level4Query);
		level4query.setParameter("deleted",Constants.NOT_DELETED);
		level4query.setParameter("level4Id",parentId);
		List<Level5MasterBean> level4=level4query.getResultList();
		session.getTransaction().commit();
		session.close();
		return level4;
	}
}
