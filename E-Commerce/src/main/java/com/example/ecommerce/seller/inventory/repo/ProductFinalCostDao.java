package com.example.ecommerce.seller.inventory.repo;

import java.util.List;

import com.example.ecommerce.seller.inventory.beans.ProductFinalCostBean;

public interface ProductFinalCostDao {

	Long saveProductFinalCost(List<ProductFinalCostBean> beanList);

}
