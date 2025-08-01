package com.example.ecommerce.seller.inventory.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.repo.ProductDao;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductDao productDao;

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
	
	
}
