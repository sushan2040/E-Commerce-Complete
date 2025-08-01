package com.example.ecommerce.usersrepo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.bcel.Const;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.masters.MenuTypeMaster;
import com.example.ecommerce.configuration.masters.SubModuleMaster;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.constants.Constants;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class UsersRepoImpl implements UsersRepository {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Users findByEmail(String email) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Users> usersQuery=builder.createQuery(Users.class);
		Root<Users> root=usersQuery.from(Users.class);
		Predicate where=builder.equal(root.get("status"),builder.parameter(String.class,"status"));
		where=builder.and(where,builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted")));
		where=builder.and(where,builder.equal(root.get("email"),builder.parameter(String.class,"email")));
		//where=builder.and(where,builder.equal(root.get("isuserseller"),Constants.USER_NOT_SELLER));
		usersQuery.where(where);
		Query<Users> userquery=session.createQuery(usersQuery);
		userquery.setParameter("status",Constants.USER_STATUS_ACTIVE);
		userquery.setParameter("deleted",Constants.USER_NOT_DELETED);
		userquery.setParameter("email",email);
		Users user=userquery.uniqueResult();
		return user;
	}

	@Override
	public Users save(Users user) {
		try {
			Session session=sessionFactory.getCurrentSession();
		Serializable savedUser=(Serializable)session.merge(user);
		return (Users)savedUser;
		}catch(Exception e) {
			e.printStackTrace();
			return new Users();
		}
	}

	@Override
	public List<SubModuleMasterBean> getUserAccess(Users parsedUser) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<SubModuleMaster> subModuleMasterQuery=builder.createQuery(SubModuleMaster.class);
		Root<SubModuleMaster> root=subModuleMasterQuery.from(SubModuleMaster.class);
		Root<MenuTypeMaster> rootMenuType=subModuleMasterQuery.from(MenuTypeMaster.class);
		subModuleMasterQuery.select(root);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("menuTypeId"),rootMenuType.get("menuTypeId")));
		where=builder.and(where,builder.equal(rootMenuType.get("menuTypeName"),builder.parameter(String.class,"menuTypeName")));
		subModuleMasterQuery.where(where);
		Query<SubModuleMaster> subModulemasterquery=session.createQuery(subModuleMasterQuery);
		subModulemasterquery.setParameter("deleted",Constants.NOT_DELETED);
		subModulemasterquery.setParameter("status",Constants.STATUS_ACTIVE);
		
		if(parsedUser.getIsUserSeller().equals(Constants.USER_SELLER)) {
			subModulemasterquery.setParameter("menuTypeName",Constants.SELLER_USERTYPE);
		}else if(parsedUser.getIsUserSeller().equals(Constants.USER_NOT_SELLER) && parsedUser.getIsAdmin().equals(Constants.IS_ADMIN)) {
			subModulemasterquery.setParameter("menuTypeName",Constants.ADMIN_USERTYPE);
		}else {
			subModulemasterquery.setParameter("menuTypeName",Constants.CUSTOMER_USERTYPE);
		}
		List<SubModuleMaster> subModuleList=subModulemasterquery.getResultList();
		List<SubModuleMasterBean> subModuleBeanList=subModuleList.stream().map(obj->{
			SubModuleMasterBean bean=new SubModuleMasterBean();
			BeanUtils.copyProperties(obj,bean);
			return bean;
		}).collect(Collectors.toList());
		return subModuleBeanList;
	}

	
}
