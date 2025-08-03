package com.example.ecommerce.seller.usermgmt.repo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.masters.UserProductCart;
import com.example.ecommerce.seller.inventory.repo.ProductFinalCostDao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class UserProductRepoImpl implements UserProductRepo{
	
	private static final Logger LOGGER=LoggerFactory.getLogger(UserProductRepoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	ProductFinalCostDao finalCostDao;

	@Override
	public Integer addProductToCart(Integer productFinalCostMasterId, Integer quantity,Users users) {
		try {
			Session session=sessionFactory.getCurrentSession();
			CriteriaBuilder cb=session.getCriteriaBuilder();
			CriteriaQuery<UserProductCart> userCartQuery=cb.createQuery(UserProductCart.class);
			Root<UserProductCart> root=userCartQuery.from(UserProductCart.class);
			Predicate where=cb.equal(root.get("productFinalCostMasterId"),cb.parameter(Integer.class,"productFinalCostMasterId"));
			where=cb.and(where,cb.equal(root.get("userId"),cb.parameter(Integer.class,"userId")));
			userCartQuery.where(where);
			userCartQuery.select(root);
			Query<UserProductCart> userproductQuery=session.createQuery(userCartQuery);
			userproductQuery.setParameter("productFinalCostMasterId",productFinalCostMasterId);
			userproductQuery.setParameter("userId",users.getUserId());
			UserProductCart userProductCart=userproductQuery.uniqueResult();
			if(userProductCart==null) {
				userProductCart=new UserProductCart();
			}else {
				
			}
		userProductCart.setProductFinalCostMasterId(productFinalCostMasterId);
		userProductCart.setUserId(users.getUserId());
		
		session.merge(userProductCart);
		return 1;
		}catch(Exception e) {
			LOGGER.info("Error occured while adding product in the cart");
			return 0;
		}
	}

	@Override
	public Integer fetchUsersCartQuantity(Users parsedUser) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder cb=session.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery=cb.createQuery(Long.class);
		Root<UserProductCart> root=countQuery.from(UserProductCart.class);
		Predicate where=cb.equal(root.get("userId"),cb.parameter(Integer.class,"userId"));
		countQuery.where(where);
		Query<Long> countquery=session.createQuery(countQuery);
		countquery.setParameter("userId",parsedUser.getUserId());
		Long count=countquery.uniqueResult();
		if(count==null) {
			return 0;
		}else {
			return count.intValue();
		}
	}

	@Override
	public Integer substractProductFromCart(Users parsedUser, Integer productFinalCostMasterId, Integer quantity) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder cb=session.getCriteriaBuilder();
		CriteriaQuery<UserProductCart> userCartQuery=cb.createQuery(UserProductCart.class);
		Root<UserProductCart> root=userCartQuery.from(UserProductCart.class);
		Predicate where=cb.equal(root.get("productFinalCostMasterId"),cb.parameter(Integer.class,"productFinalCostMasterId"));
		where=cb.and(where,cb.equal(root.get("userId"),cb.parameter(Integer.class,"userId")));
		userCartQuery.where(where);
		userCartQuery.select(root);
		Query<UserProductCart> userproductQuery=session.createQuery(userCartQuery);
		userproductQuery.setParameter("productFinalCostMasterId",productFinalCostMasterId);
		userproductQuery.setParameter("userId",parsedUser.getUserId());
		UserProductCart userProductCart=userproductQuery.uniqueResult();
		int count=0;
		if(userProductCart!=null) {
			if(quantity<=0) {
				String sql=" delete from user_product_cart where product_final_cost_master_id=:productFinalCostMasterId and user_id=:userId";
				NativeQuery nativeQuery=session.createNativeQuery(sql);
				nativeQuery.setParameter("productFinalCostMasterId",productFinalCostMasterId);
				nativeQuery.setParameter("userId",parsedUser.getUserId());
				count=nativeQuery.executeUpdate();
			}else {
				String sql=" update user_product_cart set quantity=:quantity where product_final_cost_master_id=:productFinalCostMasterId and user_id=:userId";
				NativeQuery nativeQuery=session.createNativeQuery(sql);
				nativeQuery.setParameter("productFinalCostMasterId",productFinalCostMasterId);
				nativeQuery.setParameter("quantity",quantity);
				nativeQuery.setParameter("userId",parsedUser.getUserId());
				count=nativeQuery.executeUpdate();
			}
		}
		return count;
	}

	@Override
	public List<ProductMasterBean> fetchUsersCartProducts(Users parsedUser,String currencyCode) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder cb=session.getCriteriaBuilder();
		CriteriaQuery<Integer> userCartQuery=cb.createQuery(Integer.class);
		Root<UserProductCart> root=userCartQuery.from(UserProductCart.class);
		Predicate where=cb.equal(root.get("userId"),cb.parameter(Integer.class,"userId"));
		userCartQuery.where(where);
		userCartQuery.select(root.get("productFinalCostMasterId"));
		Query<Integer> userproductQuery=session.createQuery(userCartQuery);
		userproductQuery.setParameter("userId",parsedUser.getUserId());
		List<Integer> finalCostIds=userproductQuery.getResultList();
		List<ProductMasterBean> productsOfUser=new ArrayList<ProductMasterBean>();
		for(Integer productFinalCostMasterId:finalCostIds) {
			productsOfUser.add(finalCostDao.fetchProductById(productFinalCostMasterId,currencyCode));
		}
		return productsOfUser;
	}
	
	
}
