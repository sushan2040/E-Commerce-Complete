package com.example.ecommerce.seller.inventory.service;

import java.util.List;

import com.example.ecommerce.seller.inventory.beans.ProductFinalCostBean;

public interface ProductFinalCostService{

	Long saveProductFinalCost(List<ProductFinalCostBean> beanList);

}
