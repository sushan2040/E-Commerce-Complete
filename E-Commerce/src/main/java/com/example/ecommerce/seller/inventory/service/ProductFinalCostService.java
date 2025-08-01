package com.example.ecommerce.seller.inventory.service;

import java.util.List;

import com.example.ecommerce.seller.inventory.beans.ProductFinalCostBean;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;

public interface ProductFinalCostService{

	Long saveProductFinalCost(List<ProductFinalCostBean> beanList);

	List<ProductFinalCostBean> getAllProductFinalCostPagination(int page, int per_page);

	Long checkCombinationExists(ProductFinalCostBean bean);

	ProductMasterBean fetchProductById(Integer productId, String currencyCode);

}
