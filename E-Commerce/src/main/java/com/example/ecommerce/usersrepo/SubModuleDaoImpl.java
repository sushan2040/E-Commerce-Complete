package com.example.ecommerce.usersrepo;

import java.util.ArrayList;
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

import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.MenuTypeMaster;
import com.example.ecommerce.configuration.masters.SubModuleMaster;
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
public class SubModuleDaoImpl implements SubModuleDao {

    private SessionFactory sessionFactory;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    public SubModuleDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Integer saveSubModule(SubModuleMasterBean bean) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            SubModuleMaster master = new SubModuleMaster();
            BeanUtils.copyProperties(bean, master);

            session.merge(master);
            transaction.commit(); // Commit the transaction
            
            Integer totalCount=getTotalSubmodulesCount(0, 0);
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
    public List<SubModuleMasterBean> getSubModuleList() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> subModuleMasterQuery = builder.createQuery(Object[].class);
            Root<SubModuleMaster> root = subModuleMasterQuery.from(SubModuleMaster.class);
            subModuleMasterQuery.multiselect(
                    root.get("subModuleId"),
                    root.get("subModuleName")
            );

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            subModuleMasterQuery.where(where);

            Query<Object[]> subModuleQuery = session.createQuery(subModuleMasterQuery);
            subModuleQuery.setParameter("deleted", Constants.NOT_DELETED);
            subModuleQuery.setParameter("status", Constants.STATUS_ACTIVE);
            subModuleQuery.setCacheable(true);
            subModuleQuery.setCacheRegion("subModuleMasterCache");
            List<Object[]> subModuleObjList = subModuleQuery.getResultList();
            List<SubModuleMasterBean> subModuleBeanList = subModuleObjList.stream().map(obj -> {
                SubModuleMasterBean bean = new SubModuleMasterBean();
                bean.setSubModuleId(StringHelperUtils.isNullInt(obj[0]));
                bean.setSubModuleName(StringHelperUtils.isNullString(obj[1]));
                return bean;
            }).collect(Collectors.toList());

            transaction.commit(); // Commit the transaction
            return subModuleBeanList;
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
    public List<SubModuleMasterBean> getAllSubmodulesPagination(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> subModuleMasterQuery = builder.createQuery(Object[].class);
            Root<SubModuleMaster> root = subModuleMasterQuery.from(SubModuleMaster.class);
            Root<MenuTypeMaster> rootMenuType = subModuleMasterQuery.from(MenuTypeMaster.class);

            subModuleMasterQuery.multiselect(
                    root.get("subModuleId"),
                    root.get("subModuleName"),
                    root.get("parentId"),
                    rootMenuType.get("menuTypeName"),
                    root.get("status")
            );

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
           // where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
           where = builder.and(where, builder.equal(root.get("menuTypeId"), rootMenuType.get("menuTypeId")));
            subModuleMasterQuery.where(where);

            Query<Object[]> subModuleQuery = session.createQuery(subModuleMasterQuery);
            subModuleQuery.setParameter("deleted", Constants.NOT_DELETED);
            //subModuleQuery.setParameter("status", Constants.STATUS_ACTIVE);
            subModuleQuery.setCacheable(true);
            subModuleQuery.setCacheRegion("subModuleMasterCache");
            if ((page - 1) >= 0)
                subModuleQuery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
            else
                subModuleQuery.setFirstResult(0);

            subModuleQuery.setMaxResults(per_page);

            List<Object[]> subModuleList = subModuleQuery.getResultList();
            List<SubModuleMasterBean> subModuleBeanList = new ArrayList<>();
            for (Object[] obj : subModuleList) {
                SubModuleMasterBean bean = new SubModuleMasterBean();
                bean.setSubModuleId(StringHelperUtils.isNullInt(obj[0]));
                bean.setSubModuleName(StringHelperUtils.isNullString(obj[1]));
                bean.setMenuTypeName(StringHelperUtils.isNullString(obj[3]));
                
                if (StringHelperUtils.isNullInt(obj[2]) != 0) {
                    SubModuleMaster master = session.get(SubModuleMaster.class, StringHelperUtils.isNullInt(obj[2]));
                    bean.setParentName(master.getSubModuleName());
                }
                
                bean.setStatus(StringHelperUtils.isNullString(obj[4]));
                subModuleBeanList.add(bean);
            }


            if (!subModuleBeanList.isEmpty()) {
                int totalRecords = getTotalSubmodulesCount(page, per_page);
                subModuleBeanList.get(0).setTotalRecords(totalRecords);
            }

            transaction.commit(); // Commit the transaction
            return subModuleBeanList;
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

    private int getTotalSubmodulesCount(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> subModuleMasterQuery = builder.createQuery(Long.class);
            Root<SubModuleMaster> root = subModuleMasterQuery.from(SubModuleMaster.class);
            Root<MenuTypeMaster> rootMenuType = subModuleMasterQuery.from(MenuTypeMaster.class);

            subModuleMasterQuery.select(builder.count(root));

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
           // where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where = builder.and(where, builder.equal(root.get("menuTypeId"), rootMenuType.get("menuTypeId")));
            subModuleMasterQuery.where(where);

            Query<Long> subModuleQuery = session.createQuery(subModuleMasterQuery);
            subModuleQuery.setParameter("deleted", Constants.NOT_DELETED);
           // subModuleQuery.setParameter("status", Constants.STATUS_ACTIVE);

            Long subModuleCount = subModuleQuery.uniqueResult();
            transaction.commit(); // Commit the transaction
            return subModuleCount.intValue();
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
    public Long deleteSubModuleId(Integer subModuleId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            SubModuleMaster master = session.get(SubModuleMaster.class, subModuleId);
            master.setDeleted(Constants.DELETED);
            session.persist(master);

            transaction.commit(); // Commit the transaction
            
            Integer totalCount=getTotalSubmodulesCount(0, 0);
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
    public SubModuleMasterBean getSubmoduleById(Integer subModuleId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            SubModuleMaster master = session.get(SubModuleMaster.class, subModuleId);
            SubModuleMasterBean bean = new SubModuleMasterBean();
            BeanUtils.copyProperties(master, bean);

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
