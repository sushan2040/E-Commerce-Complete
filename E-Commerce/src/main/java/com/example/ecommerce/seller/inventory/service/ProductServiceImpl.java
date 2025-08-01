package com.example.ecommerce.seller.inventory.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.seller.inventory.beans.ProductImagesBean;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.masters.ProductImages;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;
import com.example.ecommerce.seller.inventory.repo.ProductDao;
import com.example.ecommerce.usersrepo.CommonDataDao;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductDao productDao;

	@Autowired
	CommonDataDao commonDataDao;
	
	@Override
	@Transactional
	public Long saveProductMaster(ProductMasterBean productMasterBean) {
		return productDao.saveProductMaster(productMasterBean);
	}

	@Override
	public List<ProductMasterBean> getAllProductsPagination(int page, int per_page) {
		return productDao.getAllProductsPagination(page,per_page);
	}

	@Override
	@Transactional
	public Long deleteProductId(Integer productId) {
		return productDao.deleteProductId(productId);
	}

	@Override 
	public ProductMasterBean getProductById(Integer productId) {
		return productDao.getProductById(productId);
	}

	@Override
	public List<ProductMasterBean> fetchAllProducts(Integer businessId) {
		return productDao.fetchAllProducts(businessId);
	}

	@Override
	public List<ProductMasterBean> fetchProductDataSuggestions(String param, Integer businessId) {
		return productDao.fetchProductDataSuggestions(param,businessId);
	}

	@Override
	public List<ProductMasterBean> fetchFourRandomByCategories(String currencyCode) {
		List<CommonDataBean> categoryList=commonDataDao.fetchProductCategoryList();
		
		List<ProductMasterBean> productMasterList=new ArrayList<ProductMasterBean>();
		ProductMasterBean productCategory;
		for(CommonDataBean bean:categoryList) {
			List<ProductMasterBean> products=productDao.fetchProductsByCateoryId(bean.getCommonDataId(),currencyCode);
			productCategory=new ProductMasterBean();
			productCategory.setProducts(products);
			productCategory.setProductCategoryId(bean.getCommonDataId());
			productCategory.setProductCategoryCode(bean.getCommonDataName());
			productCategory.setProductCategoryDesc(bean.getCommonDataDesc());
			productMasterList.add(productCategory);
		}
		return productMasterList;
	}

	@Override
	@Transactional
	public void saveProductImage(ProductImages image) {
		productDao.saveProductImage(image);
	}

	@Override
	public List<ProductImagesBean> fetchProductImages(Integer productId) {
		return productDao.fetchProductImages(productId);
	}
	
	
	
}
