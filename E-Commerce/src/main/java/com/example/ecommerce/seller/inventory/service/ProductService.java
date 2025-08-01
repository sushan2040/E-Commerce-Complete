package com.example.ecommerce.seller.inventory.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;

public interface ProductService {

	Long saveProductMaster(ProductMasterBean productMasterBean);

	List<ProductMasterBean> getAllProductsPagination(int page, int per_page);

	Long deleteProductId(Integer productId);

	ProductMasterBean getProductById(Integer productId);

	List<ProductMasterBean> fetchAllProducts(Integer businessId);

	List<ProductMasterBean> fetchProductDataSuggestions(String param, Integer businessId);

}
