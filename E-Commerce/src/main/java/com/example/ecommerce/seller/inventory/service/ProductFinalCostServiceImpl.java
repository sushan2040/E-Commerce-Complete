package com.example.ecommerce.seller.inventory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.seller.inventory.beans.ProductFinalCostBean;
import com.example.ecommerce.seller.inventory.repo.ProductFinalCostDao;

@Service
public class ProductFinalCostServiceImpl implements ProductFinalCostService{

	@Autowired
	ProductFinalCostDao productFinalCostDao;

	@Override
	public Long saveProductFinalCost(List<ProductFinalCostBean> beanList) {
		return productFinalCostDao.saveProductFinalCost(beanList);
	}
}
