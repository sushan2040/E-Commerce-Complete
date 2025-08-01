package com.example.ecommerce.usersrepo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
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

import com.example.ecommerce.configuration.beans.BrandBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.BrandMaster;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class BrandDaoImpl implements BrandDao{

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Long saveBrandMaster(BrandBean bean) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            BrandMaster brandMaster = new BrandMaster();
            BeanUtils.copyProperties(bean, brandMaster);

            session.merge(brandMaster); // Save or update the entity
            transaction.commit(); // Commit the transaction
            
//            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
//            
//            Runnable refreshAllBrands=()->{
//            	RedisUtils.refreshRedisDataAll(RedisKey.BRANDS_ALL.getKey(),fetchAllBrands(),redisTemplate);	
//            };
//            List<Runnable> runnableTasks=new ArrayList<Runnable>();
//            runnableTasks.add(refreshAllBrands);
//            Integer total=getTotalBrandsCount(0, 0);
//           
//            for(int i=0;i<Math.ceil(total.doubleValue()/10.0);i++) {
//            	 final int page = i;
//            	Runnable task=()->{
//            	 PaginationResponse<BrandBean> response = new PaginationResponse<>();
//                 response.setPage(page);
//                 response.setTotalPages(total);
//                 response.setData(getAllBrandsPagination(page,10));
//            	RedisUtils.refreshRedisDataAll(RedisKey.BRANDS_PAGINATION.getKey(page,10),response, redisTemplate);
//            	};
//            	runnableTasks.add(task);
//            }
//
//            List<Future> future=new ArrayList<Future>();
//            future.add(executor.submit(refreshAllBrands));
//            for(Runnable task:runnableTasks) {
//            	future.add(executor.submit(task));
//            }
//            
//            for(Future executableTask:future) {
//            	executableTask.get();
//            }
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),taskExecutor,
            redisTemplate,fetchAllBrands(),RedisKey.BRANDS_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalBrandsCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse<BrandBean> response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllBrandsPagination(0,10));
            	RedisUtils.refreshRedisDataAll(RedisKey.BRANDS_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.BRANDS_PAGINATION.getKey(i,10));
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
    public List<BrandBean> fetchAllBrands() {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<BrandMaster> brandQuery = builder.createQuery(BrandMaster.class);
            Root<BrandMaster> root = brandQuery.from(BrandMaster.class);

            Predicate where = builder.equal(root.get("status"), builder.parameter(String.class, "status"));
            where = builder.and(where, builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted")));
            brandQuery.where(where);

            Query<BrandMaster> brandquery = session.createQuery(brandQuery);
            brandquery.setParameter("status", Constants.STATUS_ACTIVE);
            brandquery.setParameter("deleted", Constants.NOT_DELETED);
            brandquery.setCacheable(true);
            brandquery.setCacheRegion("brandMasterCache");
            List<BrandMaster> brandList = brandquery.getResultList();
            List<BrandBean> brandMapList = brandList.stream().map(obj -> {
                BrandBean bean = new BrandBean();
                BeanUtils.copyProperties(obj, bean);
                return bean;
            }).collect(Collectors.toList());

            transaction.commit(); // Commit the transaction

            return brandMapList;
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
    public List<BrandBean> getAllBrandsPagination(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<BrandMaster> brandQuery = builder.createQuery(BrandMaster.class);
            Root<BrandMaster> root = brandQuery.from(BrandMaster.class);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            brandQuery.where(where);

            Query<BrandMaster> brandquery = session.createQuery(brandQuery);
            brandquery.setParameter("deleted", Constants.NOT_DELETED);
            brandquery.setParameter("status", Constants.STATUS_ACTIVE);
            brandquery.setCacheable(true);
            brandquery.setCacheRegion("brandMasterCache");
            if ((page - 1) >= 0)
                brandquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
            else
                brandquery.setFirstResult(0);

            brandquery.setMaxResults(per_page);

            List<BrandMaster> brandObjList = brandquery.getResultList();
            List<BrandBean> brandBeanList = brandObjList.stream().map(obj -> {
                BrandBean bean = new BrandBean();
                BeanUtils.copyProperties(obj, bean);
                return bean;
            }).collect(Collectors.toList());

            if (!brandBeanList.isEmpty()) {
                int totalRecords = getTotalBrandsCount(page, per_page);
                brandBeanList.get(0).setTotalRecords(totalRecords);
            }

            transaction.commit(); // Commit the transaction
            return brandBeanList;
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

    private int getTotalBrandsCount(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> brandQuery = builder.createQuery(Long.class);
            Root<BrandMaster> root = brandQuery.from(BrandMaster.class);
            brandQuery.select(builder.count(root));

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            brandQuery.where(where);

            Query<Long> brandquery = session.createQuery(brandQuery);
            brandquery.setParameter("deleted", Constants.NOT_DELETED);
            brandquery.setParameter("status", Constants.STATUS_ACTIVE);

            Long brandObjCount = brandquery.uniqueResult();

            transaction.commit(); // Commit the transaction
            return brandObjCount.intValue();
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
    public BrandBean getBrandById(Integer brandId) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            BrandMaster brandMaster = session.get(BrandMaster.class, brandId);
            BrandBean bean = new BrandBean();
            BeanUtils.copyProperties(brandMaster, bean);

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
    public Long deletebrandId(Integer brandId) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            BrandMaster brandMaster = session.get(BrandMaster.class, brandId);
            brandMaster.setDeleted(Constants.DELETED);
            session.persist(brandMaster);

            transaction.commit(); // Commit the transaction
            
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,fetchAllBrands(),RedisKey.BRANDS_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalBrandsCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse<BrandBean> response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllBrandsPagination(0,10));
            	RedisUtils.refreshRedisDataAll(RedisKey.BRANDS_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.BRANDS_PAGINATION.getKey(i,10));
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
}
