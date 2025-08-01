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

import com.example.ecommerce.configuration.beans.BrandBean;
import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.CountryCurrencyMaster;
import com.example.ecommerce.configuration.masters.CountryMaster;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CountryDaoImpl implements CountryDao {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    @Override
    public Long saveCountryMaster(CountryMasterBean bean) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CountryMaster countryMaster = new CountryMaster();
            BeanUtils.copyProperties(bean, countryMaster);

            session.merge(countryMaster); // Save or update the entity
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
    public List<CountryMasterBean> fetchAllCountries() {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<CountryMasterBean> countryQuery = builder.createQuery(CountryMasterBean.class);
            Root<CountryMaster> root = countryQuery.from(CountryMaster.class);
            Root<CountryCurrencyMaster> currencyMaster=countryQuery.from(CountryCurrencyMaster.class);
            Predicate where = builder.equal(root.get("status"), builder.parameter(String.class, "status"));
            where = builder.and(where, builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted")));
            where=builder.and(where,builder.equal(root.get("countryId"), currencyMaster.get("countryId")));
            countryQuery.where(where);
            countryQuery.select(builder.construct(CountryMasterBean.class, 
            		root.get("countryId"),
            		root.get("countryName"),
            		root.get("countryFlag"),
            		currencyMaster.get("currencyCode")
            		));
            Query<CountryMasterBean> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setCacheable(true);
            countryquery.setCacheRegion("countryMasterCache");
            List<CountryMasterBean> countryList = countryquery.getResultList();
           

            transaction.commit(); // Commit the transaction
            return countryList;
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
    public List<CountryMasterBean> getAllCountriesPagination(int page, int per_page) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<CountryMaster> countryQuery = builder.createQuery(CountryMaster.class);
            Root<CountryMaster> root = countryQuery.from(CountryMaster.class);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            countryQuery.where(where);

            Query<CountryMaster> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);

            if ((page - 1) >= 0)
                countryquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
            else
                countryquery.setFirstResult(0);

            countryquery.setMaxResults(per_page);

            List<CountryMaster> countryObjList = countryquery.getResultList();
            List<CountryMasterBean> countryBeanList = countryObjList.stream().map(obj -> {
                CountryMasterBean bean = new CountryMasterBean();
                BeanUtils.copyProperties(obj, bean);
                return bean;
            }).collect(Collectors.toList());

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
            Root<CountryMaster> root = countryQuery.from(CountryMaster.class);
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
    public CountryMasterBean getCountryById(Integer countryId) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CountryMaster countryMaster = session.get(CountryMaster.class, countryId);
            CountryMasterBean bean = new CountryMasterBean();
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
    public Long deleteCountryId(Integer countryId) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CountryMaster countryMaster = session.get(CountryMaster.class, countryId);
            countryMaster.setDeleted(Constants.DELETED);
            session.persist(countryMaster);

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
}
