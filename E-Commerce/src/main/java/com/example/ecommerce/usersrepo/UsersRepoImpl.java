package com.example.ecommerce.usersrepo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.masters.BusinessMaster;
import com.example.ecommerce.configuration.masters.MenuTypeMaster;
import com.example.ecommerce.configuration.masters.SubModuleMaster;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.usermgmt.beans.RoleMasterBean;
import com.example.ecommerce.seller.usermgmt.masters.EmployeeMaster;
import com.example.ecommerce.seller.usermgmt.masters.RoleMaster;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class UsersRepoImpl implements UsersRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Users findByEmail(String email) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Users> usersQuery = builder.createQuery(Users.class);
            Root<Users> root = usersQuery.from(Users.class);

            Predicate where = builder.equal(root.get("status"), builder.parameter(String.class, "status"));
            where = builder.and(where, builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted")));
            where = builder.and(where, builder.equal(root.get("email"), builder.parameter(String.class, "email")));
            usersQuery.where(where);

            Query<Users> userQuery = session.createQuery(usersQuery);
            userQuery.setParameter("status", Constants.USER_STATUS_ACTIVE);
            userQuery.setParameter("deleted", Constants.USER_NOT_DELETED);
            userQuery.setParameter("email", email);

            Users user = userQuery.uniqueResult();
            transaction.commit(); // Commit the transaction
            return user;
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
    public Users save(Users user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            BusinessMaster businessMaster = null;
            if (user.getBusinessName() != null && user.getIsUserSeller().equals(Constants.USER_SELLER)) {
                businessMaster = new BusinessMaster();
                businessMaster.setBusinessName(user.getBusinessName());
                businessMaster.setDeleted(Constants.NOT_DELETED);
                businessMaster.setStatus(Constants.STATUS_ACTIVE);

                businessMaster = (BusinessMaster) session.merge(businessMaster);
                user.setBusinessId(businessMaster.getBusinessId());
            }

            Serializable savedUser = session.merge(user); // Save or update user
            transaction.commit(); // Commit the transaction

            return (Users) savedUser;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            return new Users(); // Return a new empty user object in case of failure
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed after completion
            }
        }
    }

    @Override
    public List<SubModuleMasterBean> getUserAccess(Users parsedUser) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SubModuleMasterBean> subModuleMasterQuery = builder.createQuery(SubModuleMasterBean.class);
            Root<SubModuleMaster> root = subModuleMasterQuery.from(SubModuleMaster.class);
            Root<MenuTypeMaster> rootMenuType = subModuleMasterQuery.from(MenuTypeMaster.class);

            subModuleMasterQuery.select(
            		builder.construct(SubModuleMasterBean.class, 
            		root.get("subModuleId"),
            		root.get("subModuleName"),
            		root.get("requestMapping"),
            		root.get("parentId"),
            		root.get("status"),
            		root.get("icon")
            		)
            		);

            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where = builder.and(where, builder.equal(root.get("menuTypeId"), rootMenuType.get("menuTypeId")));
            where = builder.and(where, builder.equal(rootMenuType.get("menuTypeName"), builder.parameter(String.class, "menuTypeName")));

            subModuleMasterQuery.where(where);

            Query<SubModuleMasterBean> subModuleMasterQueryExecution = session.createQuery(subModuleMasterQuery);
            subModuleMasterQueryExecution.setParameter("deleted", Constants.NOT_DELETED);
            subModuleMasterQueryExecution.setParameter("status", Constants.STATUS_ACTIVE);

            // Set user type based on the parsed user's role
            if (parsedUser.getIsUserSeller().equals(Constants.USER_SELLER)) {
                subModuleMasterQueryExecution.setParameter("menuTypeName", Constants.SELLER_USERTYPE);
            } else if (parsedUser.getIsUserSeller().equals(Constants.USER_NOT_SELLER) && parsedUser.getIsAdmin().equals(Constants.IS_ADMIN)) {
                subModuleMasterQueryExecution.setParameter("menuTypeName", Constants.ADMIN_USERTYPE);
            } 
            else {
                subModuleMasterQueryExecution.setParameter("menuTypeName", Constants.CUSTOMER_USERTYPE);
            }
           // subModuleMasterQueryExecution.setCacheable(true);
            List<SubModuleMasterBean> subModuleList = subModuleMasterQueryExecution.getResultList();
            transaction.commit(); // Commit the transaction
            return subModuleList;
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
	public List<SubModuleMasterBean> getEmployeeAccess(Users parsedUser) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<RoleMasterBean> roleMasterQuery=builder.createQuery(RoleMasterBean.class);
		Root<EmployeeMaster> rootEmployee=roleMasterQuery.from(EmployeeMaster.class);
		Root<Users> rootUsers=roleMasterQuery.from(Users.class);
		Root<RoleMaster> rootRole=roleMasterQuery.from(RoleMaster.class);
		Predicate where=builder.equal(rootEmployee.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(rootUsers.get("deleted"),builder.parameter(String.class,"deleted")));
		where=builder.and(where,builder.equal(rootRole.get("deleted"),builder.parameter(String.class,"deleted")));
		where=builder.and(where,builder.equal(rootRole.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(rootEmployee.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(rootUsers.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(rootEmployee.get("userId"),rootUsers.get("userId")));
		where=builder.and(where,builder.equal(rootEmployee.get("roleId"),rootRole.get("roleId")));
		where=builder.and(where,builder.equal(rootUsers.get("userId"),builder.parameter(Integer.class,"userId")));
		roleMasterQuery.where(where);
		roleMasterQuery.select(builder.construct(RoleMasterBean.class,
				rootRole.get("roleId"),
				rootRole.get("roleName"),
				rootRole.get("viewAccess"),
				rootRole.get("editAccess"),
				rootRole.get("deleteAccess"),
				rootRole.get("status")
				));
		Query<RoleMasterBean> roleQuery=session.createQuery(roleMasterQuery);
		roleQuery.setParameter("deleted", Constants.NOT_DELETED);
		roleQuery.setParameter("status",Constants.STATUS_ACTIVE);
		roleQuery.setParameter("userId",parsedUser.getUserId());
		RoleMasterBean roleMasterBean=roleQuery.uniqueResult();
		session.getTransaction().commit();
		session.close();
		
		Session session2=sessionFactory.openSession();
		session2.beginTransaction();
		CriteriaBuilder builder2=session2.getCriteriaBuilder();
		CriteriaQuery<SubModuleMasterBean> subModuleQuery=builder2.createQuery(SubModuleMasterBean.class);
		Root<SubModuleMaster> root=subModuleQuery.from(SubModuleMaster.class);
		Predicate whereSubModule=builder2.equal(root.get("deleted"),builder2.parameter(String.class,"deleted"));
		whereSubModule=builder2.and(whereSubModule,builder2.equal(root.get("status"),builder2.parameter(String.class,"status")));
		whereSubModule=builder2.and(whereSubModule,root.get("subModuleId").in(builder2.parameter(List.class,"access")));
		subModuleQuery.where(whereSubModule);
		subModuleQuery.select(builder2.construct(SubModuleMasterBean.class,
				root.get("subModuleId"),
        		root.get("subModuleName"),
        		root.get("requestMapping"),
        		root.get("parentId"),
        		root.get("status"),
        		root.get("icon")
				));
		Query<SubModuleMasterBean> submoduleQuery=session2.createQuery(subModuleQuery);
		submoduleQuery.setParameter("deleted",Constants.NOT_DELETED);
		submoduleQuery.setParameter("status",Constants.STATUS_ACTIVE);
		submoduleQuery.setParameter("access",roleMasterBean.getViewAccess());
		List<SubModuleMasterBean> subModuleList=submoduleQuery.getResultList();
		session2.getTransaction().commit();
		return subModuleList;
	}
}
