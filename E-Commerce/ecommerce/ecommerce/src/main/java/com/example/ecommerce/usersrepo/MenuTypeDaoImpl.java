package com.example.ecommerce.usersrepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.configuration.beans.MenuTypeMasterBean;
import com.example.ecommerce.configuration.masters.CountryMaster;
import com.example.ecommerce.configuration.masters.MenuTypeMaster;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.StringHelperUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class MenuTypeDaoImpl implements MenuTypeDao{

	private SessionFactory sessionFactory;
	
	@Autowired
	public MenuTypeDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory=sessionFactory;
	}

	@Override
	public List<MenuTypeMasterBean> getMenuTypeMasterList() {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Object[]> menuTypeMasterQuery=builder.createQuery(Object[].class);
		Root<MenuTypeMaster> root=menuTypeMasterQuery.from(MenuTypeMaster.class);
		menuTypeMasterQuery.multiselect(
				root.get("menuTypeId"),
				root.get("menuTypeName")
				);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		menuTypeMasterQuery.where(where);
		Query<Object[]> menuTypeQuery=session.createQuery(menuTypeMasterQuery);
		menuTypeQuery.setParameter("deleted",Constants.NOT_DELETED);
		menuTypeQuery.setParameter("status",Constants.STATUS_ACTIVE);
		List<Object[]> menuTypeObjList=menuTypeQuery.getResultList();
		return menuTypeObjList.stream().map(obj->{
			MenuTypeMasterBean bean=new MenuTypeMasterBean();
			bean.setMenuTypeId(StringHelperUtils.isNullInt(obj[0]));
			bean.setMenuTypeName(StringHelperUtils.isNullString(obj[1]));
			return bean;
		}).collect(Collectors.toList());
	}

	@Override
	public Integer saveMenuType(MenuTypeMasterBean bean) {
		try {
		MenuTypeMaster master=new MenuTypeMaster();
		BeanUtils.copyProperties(bean,master);
			Session session=sessionFactory.getCurrentSession();
			session.persist(master);
			return 1;
		}catch(Exception e) {
			return 0;
		}
	}

	@Override
	public List<MenuTypeMasterBean> getAllMenusPagination(int page, int per_page) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<MenuTypeMaster> menuTypeQuery=builder.createQuery(MenuTypeMaster.class);
		Root<MenuTypeMaster> root=menuTypeQuery.from(MenuTypeMaster.class);
		menuTypeQuery.select(root);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		menuTypeQuery.where(where);
		Query<MenuTypeMaster> menuTypequery=session.createQuery(menuTypeQuery);
		menuTypequery.setParameter("deleted",Constants.NOT_DELETED);
		menuTypequery.setParameter("status",Constants.STATUS_ACTIVE);
		if((page-1)>=0)
			menuTypequery.setFirstResult((page-1) * per_page); // Calculate the correct offset
			else
				menuTypequery.setFirstResult(0);
		menuTypequery.setMaxResults(per_page);
		List<MenuTypeMaster> menuTypeObjList=menuTypequery.getResultList();
		List<MenuTypeMasterBean> menuTypeBeanList=menuTypeObjList.stream().map(obj->{
			MenuTypeMasterBean bean=new MenuTypeMasterBean();
			BeanUtils.copyProperties(obj, bean);
			return bean;
		}).collect(Collectors.toList());
		if(!menuTypeBeanList.isEmpty()) {
			int totalRecords=getTotalMenuTypeCount(page,per_page);
			menuTypeBeanList.get(0).setTotalRecords(totalRecords);
		}
		return menuTypeBeanList;
	}

	private int getTotalMenuTypeCount(int page, int per_page) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Long> menuTypeQuery=builder.createQuery(Long.class);
		Root<MenuTypeMaster> root=menuTypeQuery.from(MenuTypeMaster.class);
		menuTypeQuery.select(builder.count(root));
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		menuTypeQuery.where(where);
		Query<Long> menuTypequery=session.createQuery(menuTypeQuery);
		menuTypequery.setParameter("deleted",Constants.NOT_DELETED);
		menuTypequery.setParameter("status",Constants.STATUS_ACTIVE);
		if((page-1)>=0)
			menuTypequery.setFirstResult((page-1) * per_page); // Calculate the correct offset
			else
				menuTypequery.setFirstResult(0);
		menuTypequery.setMaxResults(per_page);
		Long menuTypeObjCount=menuTypequery.uniqueResult();
		return menuTypeObjCount.intValue();
		
	}
	@Override
	public Long deleteMenuTypeId(Integer menuTypeId) {
		Session session=sessionFactory.getCurrentSession();
		try {
		MenuTypeMaster menuTypeMaster=session.get(MenuTypeMaster.class, menuTypeId);
		menuTypeMaster.setDeleted(Constants.DELETED);
		session.persist(menuTypeMaster);
		return 1L;
		}catch(Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}
	@Override
	public MenuTypeMasterBean getMenuTypeById(Integer menuTypeId) {
		Session session=sessionFactory.getCurrentSession();
		MenuTypeMaster menuTypeMaster=session.get(MenuTypeMaster.class, menuTypeId);
		MenuTypeMasterBean bean=new MenuTypeMasterBean();
		BeanUtils.copyProperties(menuTypeMaster, bean);
		return bean;
	}
	
}
