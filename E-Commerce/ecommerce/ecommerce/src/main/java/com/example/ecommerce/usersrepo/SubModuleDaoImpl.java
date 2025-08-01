package com.example.ecommerce.usersrepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.tomcat.util.bcel.Const;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.masters.MenuTypeMaster;
import com.example.ecommerce.configuration.masters.SubModuleMaster;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.StringHelperUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class SubModuleDaoImpl implements SubModuleDao{

	private SessionFactory sessionFactory;
	
	@Autowired
	public SubModuleDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory=sessionFactory;
	}

	@Override
	public Integer saveSubModule(SubModuleMasterBean bean) {
		try {
		Session session=sessionFactory.getCurrentSession();
		SubModuleMaster master=new SubModuleMaster();
		BeanUtils.copyProperties(bean, master);
		session.merge(master);
		return 1;
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		}

	@Override
	public List<SubModuleMasterBean> getSubModuleList() {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder  builder=session.getCriteriaBuilder();
		CriteriaQuery<Object[]> subModuleMasterQuery=builder.createQuery(Object[].class);
		Root<SubModuleMaster> root=subModuleMasterQuery.from(SubModuleMaster.class);
		subModuleMasterQuery.multiselect(
				root.get("subModuleId"),
				root.get("subModuleName")
				);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		subModuleMasterQuery.where(where);
		Query<Object[]> subModulequery=session.createQuery(subModuleMasterQuery);
		subModulequery.setParameter("deleted",Constants.NOT_DELETED);
		subModulequery.setParameter("status",Constants.STATUS_ACTIVE);
		List<Object[]> subModuleObjList=subModulequery.getResultList();
		return subModuleObjList.stream().map(obj->{
			SubModuleMasterBean bean=new SubModuleMasterBean();
			bean.setSubModuleId(StringHelperUtils.isNullInt(obj[0]));
			bean.setSubModuleName(StringHelperUtils.isNullString(obj[1]));
			return bean;
		}).collect(Collectors.toList());
		
	}

	@Override
	public List<SubModuleMasterBean> getAllSubmodulesPagination(int page, int per_page) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Object[]> subModuleMasterQuery=builder.createQuery(Object[].class);
		Root<SubModuleMaster> root=subModuleMasterQuery.from(SubModuleMaster.class);
		Root<MenuTypeMaster> rootMenuType=subModuleMasterQuery.from(MenuTypeMaster.class);
		subModuleMasterQuery.multiselect(
				root.get("subModuleId"),
				root.get("subModuleName"),
				root.get("parentId"),
				rootMenuType.get("menuTypeName"),
				root.get("status")
				);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("menuTypeId"),rootMenuType.get("menuTypeId")));
		subModuleMasterQuery.where(where);
		Query<Object[]> subModulequery=session.createQuery(subModuleMasterQuery);
		subModulequery.setParameter("deleted",Constants.NOT_DELETED);
		subModulequery.setParameter("status",Constants.STATUS_ACTIVE);
		if((page-1)>=0)
		subModulequery.setFirstResult((page-1) * per_page); // Calculate the correct offset
		else
			subModulequery.setFirstResult(0);
		subModulequery.setMaxResults(per_page);
		List<Object[]> subModuleList=subModulequery.getResultList();
		List<SubModuleMasterBean> subModuleBeanList=subModuleList.stream().map(obj->{
			SubModuleMasterBean bean=new SubModuleMasterBean();
			bean.setSubModuleId(StringHelperUtils.isNullInt(obj[0]));
			bean.setSubModuleName(StringHelperUtils.isNullString(obj[1]));
			bean.setMenuTypeName(StringHelperUtils.isNullString(obj[3]));
			if(StringHelperUtils.isNullInt(obj[2])!=0) {
			SubModuleMaster master=session.get(SubModuleMaster.class,StringHelperUtils.isNullInt(obj[2]));
			bean.setParentName(master.getSubModuleName());
			}
			bean.setStatus(StringHelperUtils.isNullString(obj[4]));
			return bean;
		}).collect(Collectors.toList());
		if(!subModuleBeanList.isEmpty()) {
			int totalRecords=getTotalSubmodulesCount(page,per_page);
			subModuleBeanList.get(0).setTotalRecords(totalRecords);
		}
		return subModuleBeanList;
	}

	private int getTotalSubmodulesCount(int page, int per_page) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Long> subModuleMasterQuery=builder.createQuery(Long.class);
		Root<SubModuleMaster> root=subModuleMasterQuery.from(SubModuleMaster.class);
		Root<MenuTypeMaster> rootMenuType=subModuleMasterQuery.from(MenuTypeMaster.class);
		subModuleMasterQuery.select(builder.count(root));
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("menuTypeId"),rootMenuType.get("menuTypeId")));
		subModuleMasterQuery.where(where);
		Query<Long> subModulequery=session.createQuery(subModuleMasterQuery);
		subModulequery.setParameter("deleted",Constants.NOT_DELETED);
		subModulequery.setParameter("status",Constants.STATUS_ACTIVE);
		Long subModuleCount=subModulequery.uniqueResult();
		return subModuleCount.intValue();
	}

	@Override
	public Long deleteSubModuleId(Integer subModuleId) {
		try {
		Session session=sessionFactory.getCurrentSession();
		SubModuleMaster master=session.get(SubModuleMaster.class, subModuleId);
		master.setDeleted(Constants.DELETED);
		session.persist(master);
		return 1L;
		}catch(Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	@Override
	public SubModuleMasterBean getSubmoduleById(Integer subModuleId) {
		Session session=sessionFactory.getCurrentSession();
		SubModuleMaster master=session.get(SubModuleMaster.class, subModuleId);
		SubModuleMasterBean bean=new SubModuleMasterBean();
		BeanUtils.copyProperties(master,bean);
		return bean;
	}
	
}
