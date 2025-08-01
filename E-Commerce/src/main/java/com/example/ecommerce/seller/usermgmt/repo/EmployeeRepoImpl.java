package com.example.ecommerce.seller.usermgmt.repo;

import java.util.List;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.usermgmt.beans.EmployeeMasterBean;
import com.example.ecommerce.seller.usermgmt.masters.EmployeeMaster;
import com.example.ecommerce.seller.usermgmt.masters.RoleMaster;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class EmployeeRepoImpl implements EmployeeRepo {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
	
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

	@Override
	public List<EmployeeMasterBean> getAllEmployeesPagination(int page, int per_page,Integer businessId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<EmployeeMasterBean> employeeQuery=builder.createQuery(EmployeeMasterBean.class);
		Root<EmployeeMaster> root=employeeQuery.from(EmployeeMaster.class);
		Root<RoleMaster> rootRole=employeeQuery.from(RoleMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("roleId"),rootRole.get("roleId")));
		where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"business_id")));
		employeeQuery.where(where);
		employeeQuery.select(builder.construct(EmployeeMasterBean.class,
				root.get("empId"),
				root.get("firstName"),
				root.get("middleName"),
				root.get("lastName"),
				root.get("username"),
				rootRole.get("roleName"),
				root.get("status")
				));
		Query<EmployeeMasterBean> empquery=session.createQuery(employeeQuery);
		empquery.setParameter("deleted",Constants.NOT_DELETED);
		empquery.setParameter("status",Constants.STATUS_ACTIVE);
		empquery.setParameter("business_id",businessId);
		if((page-1)>=0)
			empquery.setFirstResult((page-1) * per_page); // Calculate the correct offset
			else
				empquery.setFirstResult(0);
		empquery.setMaxResults(per_page);
		List<EmployeeMasterBean> empList=empquery.getResultList();
		
		if(!empList.isEmpty()) {
			Integer totalRecords=getAllEmloyeeTotalCount(businessId);
			empList.get(0).setTotalRecords(totalRecords);
		}
		
		session.getTransaction().commit();
		session.close();
		return empList;
	}

	private Integer getAllEmloyeeTotalCount(Integer businessId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Long> employeeQuery=builder.createQuery(Long.class);
		Root<EmployeeMaster> root=employeeQuery.from(EmployeeMaster.class);
		Root<RoleMaster> rootRole=employeeQuery.from(RoleMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("roleId"),rootRole.get("roleId")));
		where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"business_id")));
		employeeQuery.where(where);
		employeeQuery.select(builder.count(root));
		Query<Long> empquery=session.createQuery(employeeQuery);
		empquery.setParameter("deleted",Constants.NOT_DELETED);
		empquery.setParameter("status",Constants.STATUS_ACTIVE);
		empquery.setParameter("business_id",businessId);
		Long empCount=empquery.uniqueResult();
		return empCount.intValue();
	}

	@Override
	public Integer saveEmployeeMaster(EmployeeMasterBean bean) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		EmployeeMaster employeeMaster=new EmployeeMaster();
		BeanUtils.copyProperties(bean,employeeMaster);
		employeeMaster=session.merge(employeeMaster);
		
		
		Users savedUser=new Users();
		savedUser.setEmail(bean.getEmail());
		savedUser.setPassword(bean.getPassword());
		savedUser.setBusinessId(bean.getBusinessId());
		savedUser.setCountryId(bean.getLevel1Id());
		savedUser.setFirstName(bean.getFirstName());
		savedUser.setLastName(bean.getLastName());
		savedUser.setIsAdmin(Constants.IS_NOT_ADMIN);
		savedUser.setIsUserSeller(Constants.USER_NOT_SELLER);
		savedUser.setPhoneNumber(bean.getMobile());
		savedUser.setStatus(Constants.STATUS_ACTIVE);
		savedUser.setDeleted(Constants.NOT_DELETED);
		savedUser=session.merge(savedUser);
		
		employeeMaster.setUserId(savedUser.getUserId());
		session.merge(employeeMaster);
		
		session.getTransaction().commit();
		
		ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
        Integer totalCount=getAllEmloyeeTotalCount(bean.getBusinessId());
		session.close();
		return employeeMaster.getEmpId();
	}

	@Override
	public EmployeeMasterBean findByUsername(String username, String password) {
		Session session=sessionFactory.openSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<EmployeeMasterBean> empQuery=builder.createQuery(EmployeeMasterBean.class);
		Root<EmployeeMaster> root=empQuery.from(EmployeeMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("username"),builder.parameter(String.class,"username")));
		//where=builder.and(where,builder.equal(root.get("password"),builder.parameter(String.class,"password")));
		empQuery.where(where);
		empQuery.select(builder.construct(EmployeeMasterBean.class,
				root.get("empId"),
				root.get("businessId"),
				root.get("roleId"),
				root.get("username"),
				root.get("firstName"),
				root.get("middleName"),
				root.get("lastName"),
				root.get("password")
				));
		Query<EmployeeMasterBean> empquery=session.createQuery(empQuery);
		empquery.setParameter("deleted",Constants.NOT_DELETED);
		empquery.setParameter("status",Constants.STATUS_ACTIVE);
		empquery.setParameter("username",username);
		//empquery.setParameter("password",passwordEncoder.matches(password, password));
		EmployeeMasterBean bean=empquery.uniqueResult();
//		if(passwordEncoder.matches(password,bean.getPassword())) {
//			bean.setPassword(null);
//			return bean;
//		}else {
//			return new EmployeeMasterBean();
//		}
		return bean;
	}
}
