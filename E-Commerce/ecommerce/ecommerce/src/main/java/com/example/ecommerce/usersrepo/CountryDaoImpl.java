package com.example.ecommerce.usersrepo;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.configuration.masters.CountryMaster;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.StringHelperUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CountryDaoImpl implements CountryDao{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Long saveCountryMaster(CountryMasterBean bean) {
		try {
		CountryMaster countryMaster=new CountryMaster();
		BeanUtils.copyProperties(bean,countryMaster);
		Session session=sessionFactory.getCurrentSession();
		session.merge(countryMaster);
		return 1L;
		}catch(Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	@Override
	public List<CountryMasterBean> fetchAllCountries() {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<CountryMaster> countryQuery=builder.createQuery(CountryMaster.class);
		Root<CountryMaster> root=countryQuery.from(CountryMaster.class);
		Predicate where=builder.equal(root.get("status"),builder.parameter(String.class,"status"));
		where=builder.and(where,builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted")));
		countryQuery.where(where);
		Query<CountryMaster> countryquery=session.createQuery(countryQuery);
		countryquery.setParameter("status",Constants.STATUS_ACTIVE);
		countryquery.setParameter("deleted",Constants.NOT_DELETED);
		List<CountryMaster> countryList;
		countryList=countryquery.getResultList();
		List<CountryMasterBean> countryMapList=countryList.stream().map(obj->{
			CountryMasterBean bean=new CountryMasterBean();
			BeanUtils.copyProperties(obj, bean);
			return bean;
		}).collect(Collectors.toList());
		return countryMapList;
	}

	@Override
	public List<CountryMasterBean> getAllCountriesPagination(int page, int per_page) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<CountryMaster> countryQuery=builder.createQuery(CountryMaster.class);
		Root<CountryMaster> root=countryQuery.from(CountryMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		countryQuery.where(where);
		Query<CountryMaster> countryquery=session.createQuery(countryQuery);
		countryquery.setParameter("deleted",Constants.NOT_DELETED);
		countryquery.setParameter("status",Constants.STATUS_ACTIVE);
		if((page-1)>=0)
			countryquery.setFirstResult((page-1) * per_page); // Calculate the correct offset
			else
				countryquery.setFirstResult(0);
		countryquery.setMaxResults(per_page);
		List<CountryMaster> countryObjList=countryquery.getResultList();
		List<CountryMasterBean> countryBeanList=countryObjList.stream().map(obj->{
			CountryMasterBean bean=new CountryMasterBean();
			BeanUtils.copyProperties(obj, bean);
			return bean;
		}).collect(Collectors.toList());
		if(!countryBeanList.isEmpty()) {
			int totalRecords=getTotalCountriesCount(page,per_page);
			countryBeanList.get(0).setTotalRecords(totalRecords);
		}
		return countryBeanList;
	}

	private int getTotalCountriesCount(int page, int per_page) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Long> countryQuery=builder.createQuery(Long.class);
		Root<CountryMaster> root=countryQuery.from(CountryMaster.class);
		countryQuery.select(builder.count(root));
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		countryQuery.where(where);
		Query<Long> countryquery=session.createQuery(countryQuery);
		countryquery.setParameter("deleted",Constants.NOT_DELETED);
		countryquery.setParameter("status",Constants.STATUS_ACTIVE);
		Long countryObjCount=countryquery.uniqueResult();
		return countryObjCount.intValue();
	}

	@Override
	public CountryMasterBean getCountryById(Integer countryId) {
		Session session=sessionFactory.getCurrentSession();
		CountryMaster countryMaster=session.get(CountryMaster.class, countryId);
		CountryMasterBean bean=new CountryMasterBean();
		BeanUtils.copyProperties(countryMaster, bean);
		return bean;
	}

	@Override
	public Long deleteCountryId(Integer countryId) {
		Session session=sessionFactory.getCurrentSession();
		try {
		CountryMaster countryMaster=session.get(CountryMaster.class, countryId);
		countryMaster.setDeleted(Constants.DELETED);
		session.persist(countryMaster);
		return 1L;
		}catch(Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}
	

}
