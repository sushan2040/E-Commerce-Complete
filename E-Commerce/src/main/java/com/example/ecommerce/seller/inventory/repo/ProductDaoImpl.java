package com.example.ecommerce.seller.inventory.repo;

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
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.BrandBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.CountryMaster;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;
import com.example.ecommerce.utils.GlobalFunctionalExecution;
import com.example.ecommerce.utils.GlobalFunctionalInterface;
import com.example.ecommerce.utils.RedisUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class ProductDaoImpl implements ProductDao{

	@Autowired
	SessionFactory sessionFactory;
	
	 @Autowired
	    @Qualifier("taskExecutor")
	    private Executor taskExecutor;
	    
	    @Autowired
	    private RedisTemplate<String,Object> redisTemplate;


	@Override
	public Long saveProductMaster(ProductMasterBean productMasterBean) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		ProductMaster master=new ProductMaster();
		BeanUtils.copyProperties(productMasterBean, master);
		master.setDeleted(Constants.NOT_DELETED);
		master.setStatus(Constants.STATUS_ACTIVE);
		master=session.merge(master);
		session.getTransaction().commit();
		
	    ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
         Integer totalCount=getTotalProductCount();
         Runnable firstPagination=()->{
         	PaginationResponse response = new PaginationResponse<>();
           response.setPage(0);
           response.setTotalPages(totalCount);
           response.setData(getAllProductsPagination(0, 10));
         	RedisUtils.refreshRedisDataAll(RedisKey.PRODUCT_PAGINATION.getKey(1,10),response, redisTemplate);
         };
         try {
			executor.submit(firstPagination).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
         	redisTemplate.delete(RedisKey.PRODUCT_PAGINATION.getKey(i,10));
         }
		
		session.close();
		return master.getProductId().longValue();
	}

	@Override
	public List<ProductMasterBean> getAllProductsPagination(int page, int per_page) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<ProductMasterBean> productMasterQuery=builder.createQuery(ProductMasterBean.class);
		Root<ProductMaster> root=productMasterQuery.from(ProductMaster.class);
		Root<CountryMaster> rootCountry=productMasterQuery.from(CountryMaster.class);
		productMasterQuery.select(builder.construct(ProductMasterBean.class,
				root.get("productId"),
				root.get("productName"),
				root.get("cost"),
				root.get("minPrice"),
				root.get("maxPrice"),
				root.get("currentDiscount"),
				root.get("status"),
				rootCountry.get("countryName")
				));
		Predicate where=builder.equal(root.get("status"),builder.parameter(String.class,"status"));
		where=builder.and(where,builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted")));
		where=builder.and(where,builder.equal(root.get("countryOfOrigin"), rootCountry.get("countryId")));
		productMasterQuery.where(where);
		Query<ProductMasterBean> productquery=session.createQuery(productMasterQuery);
		productquery.setParameter("status",Constants.STATUS_ACTIVE);
		productquery.setParameter("deleted",Constants.NOT_DELETED);
		if((page-1)>=1)
			productquery.setFirstResult((page-1) * per_page); // Calculate the correct offset
			else
				productquery.setFirstResult(0);
		productquery.setMaxResults(per_page);
		List<ProductMasterBean> productBeanList=productquery.getResultList();
		if(!productBeanList.isEmpty()) {
			Integer totalRecords=getTotalProductCount();
			productBeanList.get(0).setTotalRecords(totalRecords);
		}
		session.getTransaction().commit();
		session.close();
		return productBeanList;
	}

	private Integer getTotalProductCount() {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Long> productMasterQuery=builder.createQuery(Long.class);
		Root<ProductMaster> root=productMasterQuery.from(ProductMaster.class);
		productMasterQuery.select(builder.count(root));
		Predicate where=builder.equal(root.get("status"),builder.parameter(String.class,"status"));
		where=builder.and(where,builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted")));
		productMasterQuery.where(where);
		Query<Long> productquery=session.createQuery(productMasterQuery);
		productquery.setParameter("status",Constants.STATUS_ACTIVE);
		productquery.setParameter("deleted",Constants.NOT_DELETED);
		Long productBeanCount=productquery.uniqueResult();
		session.getTransaction().commit();
		session.close();
		return productBeanCount.intValue();
	}

	@Override
	public Long deleteProductId(Integer productId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		ProductMaster master=session.get(ProductMaster.class, productId);
		master.setStatus(Constants.STATUS_INACTIVE);
		master.setDeleted(Constants.DELETED);
		session.merge(master);
		session.getTransaction().commit();
		
		ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
        Integer totalCount=getTotalProductCount();
        Runnable firstPagination=()->{
        	PaginationResponse response = new PaginationResponse<>();
          response.setPage(0);
          response.setTotalPages(totalCount);
          response.setData(getAllProductsPagination(0, 10));
        	RedisUtils.refreshRedisDataAll(RedisKey.PRODUCT_PAGINATION.getKey(1,10),response, redisTemplate);
        };
        try {
			executor.submit(firstPagination).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(int i=2;i<(Math.ceil(totalCount.doubleValue()/10.0)+1);i++) {
        	redisTemplate.delete(RedisKey.PRODUCT_PAGINATION.getKey(i,10));
        }
		
		session.close();
		return 1L;
	}

	@Override
	public ProductMasterBean getProductById(Integer productId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		ProductMaster master=session.get(ProductMaster.class, productId);
		ProductMasterBean bean=new ProductMasterBean();
		BeanUtils.copyProperties(master, bean);
		session.getTransaction().commit();
		session.close();
		return bean;
	}

	@Override
	public List<ProductMasterBean> fetchAllProducts(Integer businessId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<ProductMasterBean> productQuery=builder.createQuery(ProductMasterBean.class);
		Root<ProductMaster> root=productQuery.from(ProductMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"businessId")));
		productQuery.select(builder.construct(ProductMasterBean.class,
				root.get("productId"),
				root.get("productName")
				));
		productQuery.where(where);
		Query<ProductMasterBean> productMasterQuery=session.createQuery(productQuery);
		productMasterQuery.setParameter("deleted",Constants.NOT_DELETED);
		productMasterQuery.setParameter("status",Constants.STATUS_ACTIVE);
		productMasterQuery.setParameter("businessId",businessId);
		List<ProductMasterBean> productList=productMasterQuery.getResultList();
		session.getTransaction().commit();
		return productList;
	}

	@Override
	public List<ProductMasterBean> fetchProductDataSuggestions(String param, Integer businessId) {
		Session session=sessionFactory.openSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<ProductMasterBean> productMasterQuery=builder.createQuery(ProductMasterBean.class);
		Root<ProductMaster> root=productMasterQuery.from(ProductMaster.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"businessId")));
		where=builder.and(where,builder.like(builder.lower(root.get("productName")),builder.parameter(String.class,"productName")));
		productMasterQuery.where(where);
		productMasterQuery.select(builder.construct(ProductMasterBean.class,
				root.get("productId"),
				root.get("productName")
				));
		Query<ProductMasterBean> prodQuery=session.createQuery(productMasterQuery);
		prodQuery.setParameter("deleted",Constants.NOT_DELETED);
		prodQuery.setParameter("status",Constants.STATUS_ACTIVE);
		prodQuery.setParameter("businessId",businessId);
		prodQuery.setParameter("productName","%"+param.toLowerCase()+"%");
		return prodQuery.getResultList();
	}
	
	
}
