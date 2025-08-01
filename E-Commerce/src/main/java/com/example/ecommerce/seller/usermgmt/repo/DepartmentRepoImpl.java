package com.example.ecommerce.seller.usermgmt.repo;

import java.util.List;
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

import com.example.ecommerce.configuration.beans.DepartmentBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.DepartmentMaster;
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
public class DepartmentRepoImpl implements DepartmentRepo {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

	@Override
	public List<DepartmentBean> getDepartmentMasterList() {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Object[]> menuTypeMasterQuery=builder.createQuery(Object[].class);
		Root<DepartmentMaster> root=menuTypeMasterQuery.from(DepartmentMaster.class);
		menuTypeMasterQuery.multiselect(
				root.get("departmentId"),
				root.get("departmentName")
				);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		menuTypeMasterQuery.where(where);
		Query<Object[]> menuTypeQuery=session.createQuery(menuTypeMasterQuery);
		menuTypeQuery.setParameter("deleted",Constants.NOT_DELETED);
		menuTypeQuery.setParameter("status",Constants.STATUS_ACTIVE);
		List<Object[]> menuTypeObjList=menuTypeQuery.getResultList();
		session.getTransaction().commit();
		session.close();
		return menuTypeObjList.stream().map(obj->{
			DepartmentBean bean=new DepartmentBean();
			bean.setDepartmentId(StringHelperUtils.isNullInt(obj[0]));
			bean.setDepartmentName(StringHelperUtils.isNullString(obj[1]));
			return bean;
		}).collect(Collectors.toList());
	}

	@Override
	public Integer saveDepartmentMaster(DepartmentBean bean) {
		try {
		DepartmentMaster master=new DepartmentMaster();
		BeanUtils.copyProperties(bean,master);
			Session session=sessionFactory.openSession();
			session.beginTransaction();
			session.persist(master);
			session.getTransaction().commit();
			
            Integer totalCount=getTotalDepartmentCount(0, 0);
			session.close();
			return 1;
		}catch(Exception e) {
			return 0;
		}
	}

	@Override
	public List<DepartmentBean> getAllDepartmentsPagination(int page, int per_page) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<DepartmentMaster> departmentQuery=builder.createQuery(DepartmentMaster.class);
		Root<DepartmentMaster> root=departmentQuery.from(DepartmentMaster.class);
		departmentQuery.select(root);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		departmentQuery.where(where);
		Query<DepartmentMaster> departmentquery=session.createQuery(departmentQuery);
		departmentquery.setParameter("deleted",Constants.NOT_DELETED);
		departmentquery.setParameter("status",Constants.STATUS_ACTIVE);
		if((page-1)>=0)
			departmentquery.setFirstResult((page-1) * per_page); // Calculate the correct offset
			else
				departmentquery.setFirstResult(0);
		departmentquery.setMaxResults(per_page);
		List<DepartmentMaster> menuTypeObjList=departmentquery.getResultList();
		List<DepartmentBean> menuTypeBeanList=menuTypeObjList.stream().map(obj->{
			DepartmentBean bean=new DepartmentBean();
			BeanUtils.copyProperties(obj, bean);
			return bean;
		}).collect(Collectors.toList());
		if(!menuTypeBeanList.isEmpty()) {
			int totalRecords=getTotalDepartmentCount(page,per_page);
			menuTypeBeanList.get(0).setTotalRecords(totalRecords);
		}
		session.getTransaction().commit();
		session.close();
		return menuTypeBeanList;
	}

	private int getTotalDepartmentCount(int page, int per_page) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Long> departmentQuery=builder.createQuery(Long.class);
		Root<DepartmentMaster> root=departmentQuery.from(DepartmentMaster.class);
		departmentQuery.select(builder.count(root));
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		departmentQuery.where(where);
		Query<Long> departmentquery=session.createQuery(departmentQuery);
		departmentquery.setParameter("deleted",Constants.NOT_DELETED);
		departmentquery.setParameter("status",Constants.STATUS_ACTIVE);
		if((page-1)>=0)
			departmentquery.setFirstResult((page-1) * per_page); // Calculate the correct offset
			else
				departmentquery.setFirstResult(0);
		departmentquery.setMaxResults(per_page);
		Long menuTypeObjCount=departmentquery.uniqueResult();
		session.getTransaction().commit();
		session.close();
		return menuTypeObjCount.intValue();
		
		
	}
	@Override
	public Long deleteDepartmentMaster(Integer menuTypeId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		try {
		DepartmentMaster departmentMaster=session.get(DepartmentMaster.class, menuTypeId);
		departmentMaster.setDeleted(Constants.DELETED);
		session.persist(departmentMaster);
		session.getTransaction().commit();
        Integer totalCount=getTotalDepartmentCount(0, 0);
		session.close();
		return 1L;
		}catch(Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}
	@Override
	public DepartmentBean getDepartmentMasterById(Integer menuTypeId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		DepartmentMaster departmentMaster=session.get(DepartmentMaster.class, menuTypeId);
		DepartmentBean bean=new DepartmentBean();
		BeanUtils.copyProperties(departmentMaster, bean);
		session.getTransaction();
		session.close();
		return bean;
	}
	
}
