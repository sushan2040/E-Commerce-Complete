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

import com.example.ecommerce.configuration.beans.Level2MasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.CountryMaster;
import com.example.ecommerce.configuration.masters.Level2Master;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class LocationLevel2DaoImpl implements LocationLevel2Dao {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Long saveLevel2Master(Level2MasterBean bean) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level2Master level2Master = new Level2Master();
            BeanUtils.copyProperties(bean, level2Master);

            session.merge(level2Master); // Save or update the entity
            transaction.commit(); // Commit the transaction
            
            Integer totalCount=getTotalCountriesCount(0, 0);
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
    public List<Level2MasterBean> fetchAllLevel2s() {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Level2Master> countryQuery = builder.createQuery(Level2Master.class);
            Root<Level2Master> root = countryQuery.from(Level2Master.class);

            Predicate where = builder.equal(root.get("status"), builder.parameter(String.class, "status"));
            where = builder.and(where, builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted")));
            countryQuery.where(where);

            Query<Level2Master> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setCacheable(true);
            countryquery.setCacheRegion("level2MasterCache");
            List<Level2Master> countryList = countryquery.getResultList();
            List<Level2MasterBean> countryMapList = countryList.stream().map(obj -> {
                Level2MasterBean bean = new Level2MasterBean();
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
    public List<Level2MasterBean> getAllLevel2sPagination(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Level2MasterBean> countryQuery = builder.createQuery(Level2MasterBean.class);
            Root<Level2Master> root = countryQuery.from(Level2Master.class);
            Root<CountryMaster> rootCountry = countryQuery.from(CountryMaster.class);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where = builder.and(where, builder.equal(root.get("level1Id"), rootCountry.get("countryId")));
            countryQuery.where(where);

            countryQuery.select(builder.construct(Level2MasterBean.class,
                    root.get("level2Id"),
                    root.get("level1Id"),
                    root.get("level2Name"),
                    rootCountry.get("countryName"),
                    root.get("status")
            ));

            Query<Level2MasterBean> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);

            if ((page - 1) >= 0)
                countryquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
            else
                countryquery.setFirstResult(0);

            countryquery.setMaxResults(per_page);

            List<Level2MasterBean> countryBeanList = countryquery.getResultList();

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
            Root<Level2Master> root = countryQuery.from(Level2Master.class);
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
    public Long deleteLevel2Id(Integer level2Id) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level2Master countryMaster = session.get(Level2Master.class, level2Id);
            countryMaster.setDeleted(Constants.DELETED);
            session.persist(countryMaster);

            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAllLevel2s(),RedisKey.LOCATION_LEVEL2_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalCountriesCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllLevel2sPagination(0, 0));
            	RedisUtils.refreshRedisDataAll(RedisKey.LOCATION_LEVEL2_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.LOCATION_LEVEL2_PAGINATION.getKey(i,10));
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
    public Level2MasterBean getLevel2ById(Integer level2Id) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Level2Master countryMaster = session.get(Level2Master.class, level2Id);
            Level2MasterBean bean = new Level2MasterBean();
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
	public List<Level2MasterBean> getLocationChildByParent(Integer parentId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Level2MasterBean> childQuery=builder.createQuery(Level2MasterBean.class);
		Root<Level2Master> root=childQuery.from(Level2Master.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("level1Id"),builder.parameter(Integer.class,"level1Id")));
		childQuery.where(where);
		childQuery.select(builder.construct(Level2MasterBean.class,
				root.get("level2Id"),
				root.get("level2Name")
				));
		Query<Level2MasterBean> childMasterQuery=session.createQuery(childQuery);
		childMasterQuery.setParameter("deleted",Constants.NOT_DELETED);
		childMasterQuery.setParameter("level1Id",parentId);
		List<Level2MasterBean> level2=childMasterQuery.getResultList();
		session.getTransaction().commit();
		
			session.close();
			return level2;
	}
}
