package com.example.ecommerce.usersrepo;

import java.util.List;
import java.util.concurrent.Executor;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.CountryCurrencyBean;
import com.example.ecommerce.configuration.beans.Level2MasterBean;
import com.example.ecommerce.configuration.masters.CountryCurrencyMaster;
import com.example.ecommerce.configuration.masters.CountryMaster;
import com.example.ecommerce.configuration.masters.Level2Master;
import com.example.ecommerce.constants.Constants;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CountryCurrencyDaoImpl implements CountryCurrencyDao{

	@Autowired
	SessionFactory sessionFactory;
	
	

	@Override
	public Long saveCountryCurrencyMaster(CountryCurrencyBean bean) {
		Session session=sessionFactory.getCurrentSession();
		CountryCurrencyMaster master=new CountryCurrencyMaster();
		BeanUtils.copyProperties(bean, master);
		session.merge(master);
		return 1L;
	}



	@Override
	public List<CountryCurrencyBean> getAllCountryCurrencyPagination(int page, int per_page) {
		 Session session = null;
	        Transaction transaction = null;
	        try {
	            // Open session and start transaction
	            session = sessionFactory.openSession();
	            transaction = session.beginTransaction();

	            CriteriaBuilder builder = session.getCriteriaBuilder();
	            CriteriaQuery<CountryCurrencyBean> countryQuery = builder.createQuery(CountryCurrencyBean.class);
	            Root<CountryCurrencyMaster> root = countryQuery.from(CountryCurrencyMaster.class);
	            Root<CountryMaster> rootCountry = countryQuery.from(CountryMaster.class);

	            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
	            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
	            where = builder.and(where, builder.equal(root.get("countryId"), rootCountry.get("countryId")));
	            countryQuery.where(where);

	            countryQuery.select(builder.construct(CountryCurrencyBean.class,
	                    root.get("countryCurrencyMasterId"),
	                    root.get("countryId"),
	                    root.get("currencyName"),
	                    root.get("currencySymbol"),
	                    rootCountry.get("countryName"),
	                    root.get("currencyCode"),
	                    root.get("status")
	            ));

	            Query<CountryCurrencyBean> countryquery = session.createQuery(countryQuery);
	            countryquery.setParameter("deleted", Constants.NOT_DELETED);
	            countryquery.setParameter("status", Constants.STATUS_ACTIVE);

	            if ((page - 1) >= 0)
	                countryquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
	            else
	                countryquery.setFirstResult(0);

	            countryquery.setMaxResults(per_page);

	            List<CountryCurrencyBean> countryBeanList = countryquery.getResultList();

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
            Root<CountryCurrencyMaster> root = countryQuery.from(CountryCurrencyMaster.class);
            Root<CountryMaster> rootCountry = countryQuery.from(CountryMaster.class);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where = builder.and(where, builder.equal(root.get("countryId"), rootCountry.get("countryId")));
            countryQuery.where(where);

            countryQuery.select(builder.count(root));

            Query<Long> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);

            if ((page - 1) >= 0)
                countryquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
            else
                countryquery.setFirstResult(0);

            countryquery.setMaxResults(per_page);

            Long countryCurrencyCount = countryquery.uniqueResult();
            transaction.commit(); // Commit the transaction
            return countryCurrencyCount.intValue();
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
	public Long deleteCountryCurrencyId(Integer countryCurrencyMasterId) {
		Session session=sessionFactory.getCurrentSession();
		CountryCurrencyMaster master=session.get(CountryCurrencyMaster.class, countryCurrencyMasterId);
		master.setDeleted(Constants.DELETED);
		master.setStatus(Constants.STATUS_INACTIVE);
		session.merge(master);
		return 1L;
	}



	@Override
	public CountryCurrencyBean getCountryCurrencyById(Integer countryCurrencyMasterId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CountryCurrencyMaster countryCurrencyMaster=session.get(CountryCurrencyMaster.class, countryCurrencyMasterId);
		session.getTransaction().commit();
		session.close();
		CountryCurrencyBean bean=new CountryCurrencyBean();
		BeanUtils.copyProperties(countryCurrencyMaster, bean);
		return bean;
	}



	@Override
	public Long checkCombination(Integer countryId) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Long> currencyQuery=builder.createQuery(Long.class);
		Root<CountryCurrencyMaster> root=currencyQuery.from(CountryCurrencyMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("countryId"),builder.parameter(Integer.class,"countryId")));
		currencyQuery.where(where);
		currencyQuery.select(builder.count(root));
		Query<Long> currencyquery=session.createQuery(currencyQuery);
		currencyquery.setParameter("deleted",Constants.NOT_DELETED);
		currencyquery.setParameter("status",Constants.STATUS_ACTIVE);
		currencyquery.setParameter("countryId", countryId);
		return currencyquery.uniqueResult();
	}
	
	
}
