package com.example.ecommerce.seller.usermgmt.repo;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import com.example.ecommerce.seller.usermgmt.beans.RoleMasterBean;
import com.example.ecommerce.seller.usermgmt.masters.RoleMaster;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class RoleMasterRepoImpl implements RoleMasterRepo{

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

	@Override
	public Integer saveRoleMaster(RoleMasterBean bean) {
		ObjectMapper mapper=new ObjectMapper();
		
		String viewAccess=null;
		String editAccess=null;
		String deleteAccess=null;
		try {
			viewAccess = mapper.writeValueAsString(bean.getViewAccess());
			editAccess= mapper.writeValueAsString(bean.getEditAccess());
			 deleteAccess= mapper.writeValueAsString(bean.getDeleteAccess());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		RoleMaster master=new RoleMaster();
		BeanUtils.copyProperties(bean, master);
		master.setDeleted(Constants.NOT_DELETED);
		master.setStatus(Constants.STATUS_ACTIVE);
		master.setBusinessId(bean.getBusinessId());
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		master.setViewAccess(viewAccess);
		master.setEditAccess(editAccess);
		master.setDeleteAccess(deleteAccess);
		session.merge(master);
		session.getTransaction().commit();
		
        Integer totalCount=getTotalRolesCount(0, 0,bean.getBusinessId());
		session.close();
		return master.getRoleId();
	}

	@Override
	public List<RoleMasterBean> getAllRolesPagination(int page, int per_page,Integer businessId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<RoleMasterBean> roleMasterQuery=builder.createQuery(RoleMasterBean.class);
		Root<RoleMaster> root=roleMasterQuery.from(RoleMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"businessId")));
		roleMasterQuery.where(where);
		roleMasterQuery.select(builder.construct(RoleMasterBean.class,root.get("roleId"),
				root.get("roleName"),
				root.get("status")
				));
		Query<RoleMasterBean> Brandquery=session.createQuery(roleMasterQuery);
		Brandquery.setParameter("deleted",Constants.NOT_DELETED);
		Brandquery.setParameter("status",Constants.STATUS_ACTIVE);
		Brandquery.setParameter("businessId",businessId);
		if((page-1)>=0)
			Brandquery.setFirstResult((page-1) * per_page); // Calculate the correct offset
			else
				Brandquery.setFirstResult(0);
		Brandquery.setMaxResults(per_page);
		List<RoleMasterBean> roleMasterBeanList=Brandquery.getResultList();
		if(!roleMasterBeanList.isEmpty()) {
			int totalRecords=getTotalRolesCount(page,per_page,businessId);
			roleMasterBeanList.get(0).setTotalRecords(totalRecords);
		}
		session.getTransaction().commit();;
		session.close();
		return roleMasterBeanList;
	}

	private int getTotalRolesCount(int page, int per_page, Integer businessId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Long> roleMasterQuery=builder.createQuery(Long.class);
		Root<RoleMaster> root=roleMasterQuery.from(RoleMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"businessId")));
		roleMasterQuery.where(where);
		roleMasterQuery.select(builder.count(root));
		Query<Long> Brandquery=session.createQuery(roleMasterQuery);
		Brandquery.setParameter("deleted",Constants.NOT_DELETED);
		Brandquery.setParameter("status",Constants.STATUS_ACTIVE);
		Brandquery.setParameter("businessId",businessId);
		Long roleMasterCount=Brandquery.uniqueResult();
		session.getTransaction().commit();
		session.close();
		return roleMasterCount.intValue();
	}

	@Override
	public Long deleteRoleId(Integer roleId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		RoleMaster master=session.get(RoleMaster.class, roleId);
		master.setDeleted(Constants.DELETED);
		master.setStatus(Constants.STATUS_ACTIVE);
		session.merge(master);
		session.getTransaction().commit();
		
        Integer totalCount=getTotalRolesCount(0, 0,master.getBusinessId());
		session.close();
		return 1L;
	}

	@Override
	public RoleMasterBean getRoleById(Integer roleId) {
		RoleMasterBean bean=new RoleMasterBean();
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<RoleMasterBean> roleMasterQuery=builder.createQuery(RoleMasterBean.class);
		Root<RoleMaster> root=roleMasterQuery.from(RoleMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("roleId"),builder.parameter(Integer.class,"roleId")));
		roleMasterQuery.select(builder.construct(RoleMasterBean.class,
				root.get("roleId"),
				root.get("roleName"),
				root.get("viewAccess"),
				root.get("editAccess"),
				root.get("deleteAccess"),
				root.get("status")
				));
		roleMasterQuery.where(where);
		Query<RoleMasterBean> rolequery=session.createQuery(roleMasterQuery);
		rolequery.setParameter("deleted",Constants.NOT_DELETED);
		rolequery.setParameter("status",Constants.STATUS_ACTIVE);
		rolequery.setParameter("roleId",roleId);
		RoleMasterBean bean2=rolequery.uniqueResult();
		session.getTransaction().commit();
		session.close();
		return bean2;
	}

	@Override
	public List<RoleMasterBean> getRoles(Integer businessId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		try {
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<RoleMasterBean> roleBeanQuery=builder.createQuery(RoleMasterBean.class);
		Root<RoleMaster> root=roleBeanQuery.from(RoleMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"businessId")));
		roleBeanQuery.where(where);
		roleBeanQuery.select(builder.construct(RoleMasterBean.class,
				root.get("roleId"),
				root.get("roleName"),
				root.get("status")
				));
		Query<RoleMasterBean> roleMasterQuery=session.createQuery(roleBeanQuery);
		roleMasterQuery.setParameter("deleted",Constants.NOT_DELETED);
		roleMasterQuery.setParameter("status",Constants.STATUS_ACTIVE);
		roleMasterQuery.setParameter("businessId",businessId);
		session.getTransaction().commit();
		return roleMasterQuery.getResultList();
		}finally {
			session.close();	
		}
		
	}
}
