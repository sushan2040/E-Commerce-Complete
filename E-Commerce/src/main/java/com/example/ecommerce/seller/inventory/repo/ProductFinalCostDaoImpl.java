package com.example.ecommerce.seller.inventory.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.configuration.masters.CommonData;
import com.example.ecommerce.configuration.masters.CountryCurrencyMaster;
import com.example.ecommerce.configuration.masters.CountryMaster;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductFinalCostBean;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.beans.ProductSpecificationValueBean;
import com.example.ecommerce.seller.inventory.masters.ProductFinalCostMaster;
import com.example.ecommerce.seller.inventory.masters.ProductImages;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;
import com.example.ecommerce.seller.inventory.masters.ProductSpecificationValueMaster;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class ProductFinalCostDaoImpl implements ProductFinalCostDao{

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Long saveProductFinalCost(List<ProductFinalCostBean> beanList) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		
		String productSpecifications=beanList.get(0).getProductSpecList().stream().collect(Collectors.joining(","));
		
		for(ProductFinalCostBean bean:beanList) {
		ProductFinalCostMaster master=new ProductFinalCostMaster();
		BeanUtils.copyProperties(bean, master);
		master.setProductSpecifications(productSpecifications);
		master.setDeleted(Constants.NOT_DELETED);
		master.setStatus(Constants.STATUS_ACTIVE);
		session.merge(master);
		}
		session.getTransaction().commit();
		return 0L;
	}

	@Override
	public List<ProductFinalCostBean> getAllProductFinalCostPagination(int page, int per_page) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<ProductFinalCostBean> productFinalQuery=builder.createQuery(ProductFinalCostBean.class);
		Root<ProductFinalCostMaster> root=productFinalQuery.from(ProductFinalCostMaster.class);
		Root<CountryMaster> rootCountry=productFinalQuery.from(CountryMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("countryId"), rootCountry.get("countryId")));
		productFinalQuery.where(where);
		productFinalQuery.select(builder.construct(ProductFinalCostBean.class,
				root.get("productFinalCostMasterId"),
				root.get("productId"),
				root.get("productSpecifications"),
				root.get("countryId"),
				root.get("cost"),
				rootCountry.get("countryName")
				));
		Query<ProductFinalCostBean> prodQuery=session.createQuery(productFinalQuery);
		prodQuery.setParameter("deleted",Constants.NOT_DELETED);
		prodQuery.setParameter("status",Constants.STATUS_ACTIVE);
		prodQuery.setFirstResult(page);
		prodQuery.setMaxResults(per_page);
		List<ProductFinalCostBean> list=prodQuery.getResultList();
		if(!list.isEmpty()) {
			int totalRecords=getTotalProductFinalCost();
			list.get(0).setTotalRecords(totalRecords);
		}
		ProductSpecificationValueMaster commonData;
		for(ProductFinalCostBean productSpecification:list) {
			List<String> specifications=new ArrayList<String>();
			for(String specification:productSpecification.getProductSpecifications().split(",")) {
				commonData=session.get(ProductSpecificationValueMaster.class,Integer.parseInt(specification));
				specifications.add(commonData.getValue());
			}
			productSpecification.setProductSpecList(specifications);
		}
		
		return list;
	}

	private int getTotalProductFinalCost() {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Long> productFinalQuery=builder.createQuery(Long.class);
		Root<ProductFinalCostMaster> root=productFinalQuery.from(ProductFinalCostMaster.class);
		Root<CountryMaster> rootCountry=productFinalQuery.from(CountryMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("countryId"), rootCountry.get("countryId")));
		productFinalQuery.where(where);
		productFinalQuery.select(builder.count(root));
		Query<Long> prodQuery=session.createQuery(productFinalQuery);
		prodQuery.setParameter("deleted",Constants.NOT_DELETED);
		prodQuery.setParameter("status",Constants.STATUS_ACTIVE);
		Long count=prodQuery.uniqueResult();
		return count.intValue();
	}

	@Override
	public Long checkCombinationExists(ProductFinalCostBean bean) {
		String productSpecifications=bean.getProductSpecList().stream().collect(Collectors.joining(","));
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		Long count=0L;
		try {
		String sql="select count(product_final_cost_master_id) from ecommerce.product_final_cost_master pfcm where country_id=:countryId and product_id=:productId\n"
				+"AND (\n"
				+ "    array(\n"
				+ "      SELECT unnest(string_to_array(product_specifications, ',')::text[])\n"
				+ "      ORDER BY unnest\n"
				+ "    ) = array(\n"
				+ "      SELECT unnest(string_to_array(:specifications, ',')::text[])\n"
				+ "      ORDER BY unnest\n"
				+ "    )\n"
				+ "  );";
		NativeQuery query=session.createNativeQuery(sql);
		query.setParameter("countryId",bean.getCountryId());
		query.setParameter("productId",bean.getProductId());
		query.setParameter("specifications",productSpecifications);
		count=(Long)query.getSingleResult();
		session.getTransaction().commit();
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(count>0) {
		return 1L;
		}else {
			return 0L;
		}
	}

	@Override
	public ProductMasterBean fetchProductById(Integer productId,String currencyCode) {
		Session session = sessionFactory.getCurrentSession();
		
		ProductFinalCostMaster finalCostMaster=session.get(ProductFinalCostMaster.class, productId);
		String[] specificationArray=finalCostMaster.getProductSpecifications().split(",");
		List<Integer> specificationsList=Arrays.stream(specificationArray).map(obj->Integer.parseInt(obj)).collect(Collectors.toList());
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<ProductMasterBean> prodQuery = builder.createQuery(ProductMasterBean.class);
		Root<ProductMaster> root = prodQuery.from(ProductMaster.class);
		Root<ProductFinalCostMaster> rootFinalCost = prodQuery.from(ProductFinalCostMaster.class);
		Root<CountryMaster> rootCountry = prodQuery.from(CountryMaster.class);
		Root<CountryCurrencyMaster> rootCurrency = prodQuery.from(CountryCurrencyMaster.class);
		Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
		where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
		where = builder.and(where, builder.equal(rootFinalCost.get("productId"), builder.parameter(Integer.class, "productId")));
		where = builder.and(where, builder.equal(rootFinalCost.get("productId"), root.get("productId")));
		where = builder.and(where, builder.equal(rootFinalCost.get("countryId"), rootCurrency.get("countryId")));
		where = builder.and(where, builder.equal(rootCountry.get("countryId"), rootFinalCost.get("countryId")));
		where = builder.and(where, builder.equal(rootCurrency.get("currencyCode"), builder.parameter(String.class, "currencyCode")));

		prodQuery.where(where);
		prodQuery.orderBy(builder.asc(builder.function("RANDOM", Double.class)));

		// Selecting the desired fields and constructing the ProductMasterBean
		prodQuery.select(builder.construct(ProductMasterBean.class,
		        root.get("productId"),
		        root.get("productName"),
		        rootFinalCost.get("cost"),
		        root.get("productDesc"),
		        rootCurrency.get("currencySymbol")
				)
				);

		// Execute the query
		Query<ProductMasterBean> productQuery = session.createQuery(prodQuery);
		productQuery.setParameter("deleted", Constants.NOT_DELETED);
		productQuery.setParameter("status", Constants.STATUS_ACTIVE);
		productQuery.setParameter("productId", finalCostMaster.getProductId());
		productQuery.setParameter("currencyCode", currencyCode);
		productQuery.setMaxResults(1);

		// Fetch the result list
		 ProductMasterBean bean=productQuery.uniqueResult();
		 
		 //fetching product images
		 CriteriaBuilder builder3=session.getCriteriaBuilder();
		 CriteriaQuery<ProductImages> productImagesQuery=builder3.createQuery(ProductImages.class);
		 Root<ProductImages> rootImage=productImagesQuery.from(ProductImages.class);
		 Predicate whereImages=builder3.equal(rootImage.get("deleted"),builder3.parameter(String.class,"deleted"));
		 whereImages=builder3.and(whereImages,builder3.equal(rootImage.get("status"),builder3.parameter(String.class,"status")));
		 whereImages=builder3.and(whereImages,builder3.equal(rootImage.get("productMaster").get("productId"),builder3.parameter(Integer.class,"productId")));
		 productImagesQuery.where(whereImages);
		 Query<ProductImages> prodImgQuery=session.createQuery(productImagesQuery);
		 prodImgQuery.setParameter("deleted",Constants.NOT_DELETED);
		 prodImgQuery.setParameter("status",Constants.STATUS_ACTIVE);
		 prodImgQuery.setParameter("productId",finalCostMaster.getProductId());
		 List<ProductImages> imagesList=prodImgQuery.getResultList();
		 bean.setProductImages(imagesList);
		 
		 if(bean!=null) {
		 CriteriaBuilder builder2=session.getCriteriaBuilder();
		 CriteriaQuery<ProductSpecificationValueBean> commonDataQuery=builder2.createQuery(ProductSpecificationValueBean.class);
		 Root<ProductSpecificationValueMaster> rootSpecificationValue=commonDataQuery.from(ProductSpecificationValueMaster.class);
		 Root<CommonData> rootCommonData=commonDataQuery.from(CommonData.class);
		 commonDataQuery.select(builder2.construct(ProductSpecificationValueBean.class,
					rootSpecificationValue.get("productSpecificationValueMasterId"),
					rootCommonData.get("commonDataDesc"),
					rootSpecificationValue.get("value")
				 ));
		 Predicate whereSpec=builder2.equal(rootSpecificationValue.get("productId"),builder2.parameter(Integer.class,"productId"));
		 whereSpec=builder2.and(whereSpec,builder2.equal(rootSpecificationValue.get("deleted"),builder2.parameter(String.class,"deleted")));
		 whereSpec=builder2.and(whereSpec,builder2.equal(rootSpecificationValue.get("status"),builder2.parameter(String.class,"status")));
		 whereSpec=builder2.and(whereSpec,builder2.equal(rootCommonData.get("commonDataId"),rootSpecificationValue.get("specificationId")));
		 whereSpec=builder2.and(whereSpec,rootSpecificationValue.get("productSpecificationValueMasterId").in(specificationsList));
		 commonDataQuery.where(whereSpec);
		 Query<ProductSpecificationValueBean> prodSpecQuery=session.createQuery(commonDataQuery);
		 prodSpecQuery.setParameter("productId",bean.getProductId());
		 prodSpecQuery.setParameter("deleted",Constants.NOT_DELETED);
		 prodSpecQuery.setParameter("status",Constants.STATUS_ACTIVE);
		 List<ProductSpecificationValueBean> specificationValueBeanList=prodSpecQuery.getResultList();
		 bean.setSpecificationList(specificationValueBeanList);
		 return bean;
		 }else {
			 return new ProductMasterBean();
		 }
	}
}
