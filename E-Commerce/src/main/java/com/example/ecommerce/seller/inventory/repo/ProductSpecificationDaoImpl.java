package com.example.ecommerce.seller.inventory.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.BrandBean;
import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.configuration.masters.BrandMaster;
import com.example.ecommerce.configuration.masters.CommonData;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.beans.ProductSpecificationValueBean;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;
import com.example.ecommerce.seller.inventory.masters.ProductSpecificationValueMaster;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class ProductSpecificationDaoImpl implements ProductSpecificationDao{

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Long saveProductSpecificationValueMaster(ProductSpecificationValueBean bean) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		ProductSpecificationValueMaster master;
		for(String value:bean.getValues()) {
			if(bean.getProductSpecificationValueMasterId()!=null) {
				master=session.get(ProductSpecificationValueMaster.class,bean.getProductSpecificationValueMasterId());
			}else {
				master=new ProductSpecificationValueMaster();
			}
			master.setProductId(bean.getProductId());
			master.setSpecificationId(bean.getSpecificationId());
			master.setValue(value);
			master.setDeleted(Constants.NOT_DELETED);
			master.setStatus(Constants.STATUS_ACTIVE);
			session.merge(master);
		}
		session.getTransaction().commit();;
		return 0L;
	}

	@Override
	public List<ProductSpecificationValueBean> getAllProductSpecificationValueMasterPagination(int page, int per_page) {
		 Session session = null;
	        Transaction transaction = null;
	        try {
	            // Open session and start transaction
	            session = sessionFactory.openSession();
	            transaction = session.beginTransaction();

	            CriteriaBuilder builder = session.getCriteriaBuilder();
	            CriteriaQuery<ProductSpecificationValueBean> brandQuery = builder.createQuery(ProductSpecificationValueBean.class);
	            Root<ProductSpecificationValueMaster> root = brandQuery.from(ProductSpecificationValueMaster.class);
	            Root<ProductMaster> rootProduct=brandQuery.from(ProductMaster.class);
	            Root<CommonData> rootSpecification=brandQuery.from(CommonData.class);
	            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
	            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
	            where=builder.and(where,builder.equal(rootProduct.get("productId"),root.get("productId")));
	            where=builder.and(where,builder.equal(rootSpecification.get("commonDataId"),root.get("specificationId")));
	            brandQuery.where(where);
	            brandQuery.select(builder.construct(ProductSpecificationValueBean.class,
	            		root.get("productSpecificationValueMasterId"),
	            		rootProduct.get("productName"),
	            		rootSpecification.get("commonDataName"),
	            		root.get("value"),
	            		root.get("status")
	            		));
	            Query<ProductSpecificationValueBean> brandquery = session.createQuery(brandQuery);
	            brandquery.setParameter("deleted", Constants.NOT_DELETED);
	            brandquery.setParameter("status", Constants.STATUS_ACTIVE);
	            brandquery.setCacheable(true);
	            if ((page - 1) >= 0)
	                brandquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
	            else
	                brandquery.setFirstResult(0);

	            brandquery.setMaxResults(per_page);
	            List<ProductSpecificationValueBean> brandBeanList = brandquery.getResultList();

	            if (!brandBeanList.isEmpty()) {
	                int totalRecords = getTotalBrandsCount(page, per_page);
	                brandBeanList.get(0).setTotalRecords(totalRecords);
	            }

	            transaction.commit(); // Commit the transaction
	            return brandBeanList;
	        } catch (Exception e) {
	            if (transaction != null) {
	                transaction.rollback(); // Rollback if error occurs
	            }
	            e.printStackTrace();
	            return null;
	        } finally {
	            if (session != null) {
	                session.close(); // Close the session
	            }
	        }
	}

	private int getTotalBrandsCount(int page, int per_page) {
		Session session = null;
        Transaction transaction = null;
        try {
            // Open session and start transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> brandQuery = builder.createQuery(Long.class);
            Root<ProductSpecificationValueMaster> root = brandQuery.from(ProductSpecificationValueMaster.class);
            Root<ProductMaster> rootProduct=brandQuery.from(ProductMaster.class);
            Root<CommonData> rootSpecification=brandQuery.from(CommonData.class);
            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            where=builder.and(where,builder.equal(rootProduct.get("productId"),root.get("productId")));
            where=builder.and(where,builder.equal(rootSpecification.get("commonDataId"),root.get("specificationId")));
            brandQuery.where(where);
            brandQuery.select(builder.count(root));
            Query<Long> brandquery = session.createQuery(brandQuery);
            brandquery.setParameter("deleted", Constants.NOT_DELETED);
            brandquery.setParameter("status", Constants.STATUS_ACTIVE);
            brandquery.setCacheable(true);
            if ((page - 1) >= 0)
                brandquery.setFirstResult((page - 1) * per_page); // Calculate the correct offset
            else
                brandquery.setFirstResult(0);

            brandquery.setMaxResults(per_page);
            Long brandBeanCount = brandquery.uniqueResult();
            return brandBeanCount.intValue();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback if error occurs
            }
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
	}

	@Override
	public Long deleteProductSpecificationValueId(Integer productSpecificationValueId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		ProductSpecificationValueMaster master=session.get(ProductSpecificationValueMaster.class, productSpecificationValueId);
		master.setDeleted(Constants.DELETED);
		master.setStatus(Constants.STATUS_INACTIVE);
		session.merge(master);
		session.getTransaction().commit();
		return 0L;
	}

	@Override
	public ProductSpecificationValueBean getProductSpecificationValueById(Integer productSpecificationValueId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		ProductSpecificationValueMaster master=session.get(ProductSpecificationValueMaster.class, productSpecificationValueId);
		ProductSpecificationValueBean bean=new ProductSpecificationValueBean();
		BeanUtils.copyProperties(master,bean);
		session.getTransaction().commit();
		
		ProductMaster master2=session.get(ProductMaster.class,bean.getProductId());
		bean.setProductName(master2.getProductName());
		
		CommonData specification=session.get(CommonData.class,bean.getSpecificationId());
		bean.setSpecificationName(specification.getCommonDataName());
		
		
		return bean;
	}

	@Override
	public Map<String,List<ProductSpecificationValueBean>> getProductSpecificationValues(Integer productId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<ProductSpecificationValueBean> productSpecQuery=builder.createQuery(ProductSpecificationValueBean.class);
		Root<ProductSpecificationValueMaster> root=productSpecQuery.from(ProductSpecificationValueMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("productId"),builder.parameter(Integer.class,"productId")));
		where=builder.and(where,root.get("specificationId").isNotNull());
		productSpecQuery.select(builder.construct(ProductSpecificationValueBean.class, 
				root.get("productSpecificationValueMasterId"),
				root.get("productId"),
				root.get("specificationId"),
				root.get("value")
				));
		productSpecQuery.where(where);
		Query<ProductSpecificationValueBean> prodQuery=session.createQuery(productSpecQuery);
		prodQuery.setParameter("deleted",Constants.NOT_DELETED);
		prodQuery.setParameter("status",Constants.STATUS_ACTIVE);
		prodQuery.setParameter("productId",productId);
		List<ProductSpecificationValueBean> specifications=prodQuery.getResultList();
		specifications=specifications.stream().filter(obj->obj.getSpecificationId()!=0).collect(Collectors.toList());
		Map<Integer,List<ProductSpecificationValueBean>> mapOfSpecifications=specifications.stream().collect(Collectors.groupingBy(obj->obj.getSpecificationId()));
		CommonDataBean bean;
		Map<String,List<ProductSpecificationValueBean>> finalMap=new HashMap<String, List<ProductSpecificationValueBean>>();
		for(Map.Entry<Integer,List<ProductSpecificationValueBean>> map:mapOfSpecifications.entrySet()) {
			bean=new CommonDataBean();
			CommonData commonData=session.get(CommonData.class,map.getKey());
			BeanUtils.copyProperties(commonData, bean);
			finalMap.put(bean.getCommonDataDesc(),map.getValue());
		}
		return finalMap;
	}
}
