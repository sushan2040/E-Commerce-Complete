package com.example.ecommerce.seller.inventory.repo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.seller.inventory.beans.ProductFinalCostBean;
import com.example.ecommerce.seller.inventory.masters.ProductFinalCostMaster;

@Repository
public class ProductFinalCostDaoImpl implements ProductFinalCostDao{

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Long saveProductFinalCost(List<ProductFinalCostBean> beanList) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		for(ProductFinalCostBean bean:beanList) {
		ProductFinalCostMaster master=new ProductFinalCostMaster();
		BeanUtils.copyProperties(bean, master);
		session.merge(master);
		}
		session.getTransaction().commit();
		return 0L;
	}
}
