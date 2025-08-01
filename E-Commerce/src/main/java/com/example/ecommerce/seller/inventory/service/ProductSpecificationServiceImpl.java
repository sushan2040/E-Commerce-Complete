package com.example.ecommerce.seller.inventory.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.beans.ProductSpecificationValueBean;
import com.example.ecommerce.seller.inventory.repo.ProductSpecificationDao;

import jakarta.transaction.Transactional;

@Service
public class ProductSpecificationServiceImpl implements ProductSpecificationService{

	@Autowired
	ProductSpecificationDao productSpecificationDao;

	@Override
	@Transactional
	public Long saveProductSpecificationValueMaster(ProductSpecificationValueBean bean) {
		return productSpecificationDao.saveProductSpecificationValueMaster(bean);
	}

	@Override
	public List<ProductSpecificationValueBean> getAllProductSpecificationValueMasterPagination(int page, int per_page) {
		return productSpecificationDao.getAllProductSpecificationValueMasterPagination(page,per_page);
	}

	@Override
	@Transactional
	public Long deleteProductSpecificationValueId(Integer productSpecificationValueId) {
		return productSpecificationDao.deleteProductSpecificationValueId(productSpecificationValueId);
	}

	@Override
	public ProductSpecificationValueBean getProductSpecificationValueById(Integer productSpecificationValueId) {
		return productSpecificationDao.getProductSpecificationValueById(productSpecificationValueId);
	}

	@Override
	public Map<String,List<ProductSpecificationValueBean>> getProductSpecificationValues(Integer productId) {
		Map<String,List<ProductSpecificationValueBean>>specifications=productSpecificationDao.getProductSpecificationValues(productId);
		
		return specifications;
	}
}
