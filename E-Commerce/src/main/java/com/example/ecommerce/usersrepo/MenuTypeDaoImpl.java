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

import com.example.ecommerce.configuration.beans.MenuTypeMasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.MenuTypeMaster;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;
import com.example.ecommerce.utils.StringHelperUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class MenuTypeDaoImpl implements MenuTypeDao {

    private SessionFactory sessionFactory;

    @Autowired
    public MenuTypeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public List<MenuTypeMasterBean> getMenuTypeMasterList() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> menuTypeMasterQuery = builder.createQuery(Object[].class);
            Root<MenuTypeMaster> root = menuTypeMasterQuery.from(MenuTypeMaster.class);

            menuTypeMasterQuery.multiselect(
                    root.get("menuTypeId"),
                    root.get("menuTypeName")
            );

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            menuTypeMasterQuery.where(where);

            Query<Object[]> menuTypeQuery = session.createQuery(menuTypeMasterQuery);
            menuTypeQuery.setParameter("deleted", Constants.NOT_DELETED);
            menuTypeQuery.setParameter("status", Constants.STATUS_ACTIVE);
            menuTypeQuery.setCacheable(true);
            menuTypeQuery.setCacheRegion("menuTypeMasterCache");
            List<Object[]> menuTypeObjList = menuTypeQuery.getResultList();
            List<MenuTypeMasterBean> menuTypeBeanList = menuTypeObjList.stream().map(obj -> {
                MenuTypeMasterBean bean = new MenuTypeMasterBean();
                bean.setMenuTypeId(StringHelperUtils.isNullInt(obj[0]));
                bean.setMenuTypeName(StringHelperUtils.isNullString(obj[1]));
                return bean;
            }).collect(Collectors.toList());

            transaction.commit(); // Commit the transaction
            return menuTypeBeanList;
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
    public Integer saveMenuType(MenuTypeMasterBean bean) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            MenuTypeMaster master = new MenuTypeMaster();
            BeanUtils.copyProperties(bean, master);

            session.persist(master);
            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,getMenuTypeMasterList(),RedisKey.MENU_TYPE_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalMenuTypeCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllMenusPagination(0, 10));
            	RedisUtils.refreshRedisDataAll(RedisKey.MENU_TYPE_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.MENU_TYPE_PAGINATION.getKey(i,10));
            }
            return 1;
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
    public List<MenuTypeMasterBean> getAllMenusPagination(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MenuTypeMaster> menuTypeQuery = builder.createQuery(MenuTypeMaster.class);
            Root<MenuTypeMaster> root = menuTypeQuery.from(MenuTypeMaster.class);

            menuTypeQuery.select(root);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            menuTypeQuery.where(where);

            Query<MenuTypeMaster> menuTypeQueryExec = session.createQuery(menuTypeQuery);
            menuTypeQueryExec.setParameter("deleted", Constants.NOT_DELETED);
            menuTypeQueryExec.setParameter("status", Constants.STATUS_ACTIVE);

            if ((page - 1) >= 0)
                menuTypeQueryExec.setFirstResult((page - 1) * per_page); // Correct offset
            else
                menuTypeQueryExec.setFirstResult(0);

            menuTypeQueryExec.setMaxResults(per_page);

            List<MenuTypeMaster> menuTypeObjList = menuTypeQueryExec.getResultList();
            List<MenuTypeMasterBean> menuTypeBeanList = menuTypeObjList.stream().map(obj -> {
                MenuTypeMasterBean bean = new MenuTypeMasterBean();
                BeanUtils.copyProperties(obj, bean);
                return bean;
            }).collect(Collectors.toList());

            if (!menuTypeBeanList.isEmpty()) {
                int totalRecords = getTotalMenuTypeCount(page, per_page);
                menuTypeBeanList.get(0).setTotalRecords(totalRecords);
            }

            transaction.commit(); // Commit the transaction
            return menuTypeBeanList;
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

    private int getTotalMenuTypeCount(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> menuTypeQuery = builder.createQuery(Long.class);
            Root<MenuTypeMaster> root = menuTypeQuery.from(MenuTypeMaster.class);

            menuTypeQuery.select(builder.count(root));

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            menuTypeQuery.where(where);

            Query<Long> menuTypeQueryExec = session.createQuery(menuTypeQuery);
            menuTypeQueryExec.setParameter("deleted", Constants.NOT_DELETED);
            menuTypeQueryExec.setParameter("status", Constants.STATUS_ACTIVE);

            Long menuTypeObjCount = menuTypeQueryExec.uniqueResult();
            transaction.commit(); // Commit the transaction
            return menuTypeObjCount.intValue();
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
    public Long deleteMenuTypeId(Integer menuTypeId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            MenuTypeMaster menuTypeMaster = session.get(MenuTypeMaster.class, menuTypeId);
            menuTypeMaster.setDeleted(Constants.DELETED);
            session.persist(menuTypeMaster);

            transaction.commit(); // Commit the transaction
            
            GlobalFunctionalInterface.allFunction(input->
            GlobalFunctionalExecution.setRedisDataAll(input.getInput1(),input.getInput2(),input.getInput3(),input.getInput4()),
            taskExecutor,redisTemplate,getMenuTypeMasterList(),RedisKey.MENU_TYPE_ALL.getKey());
            ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
            Integer totalCount=getTotalMenuTypeCount(0, 0);
            Runnable firstPagination=()->{
            	PaginationResponse response = new PaginationResponse<>();
              response.setPage(0);
              response.setTotalPages(totalCount);
              response.setData(getAllMenusPagination(0, 10));
            	RedisUtils.refreshRedisDataAll(RedisKey.MENU_TYPE_PAGINATION.getKey(1,10),response, redisTemplate);
            };
            executor.submit(firstPagination).get();
            for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
            	redisTemplate.delete(RedisKey.MENU_TYPE_PAGINATION.getKey(i,10));
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
    public MenuTypeMasterBean getMenuTypeById(Integer menuTypeId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            MenuTypeMaster menuTypeMaster = session.get(MenuTypeMaster.class, menuTypeId);
            MenuTypeMasterBean bean = new MenuTypeMasterBean();
            BeanUtils.copyProperties(menuTypeMaster, bean);

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
}
