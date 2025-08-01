package com.example.ecommerce.seller.inventory.service;

import java.util.List;


import com.example.ecommerce.seller.inventory.beans.ProductImagesBean;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.masters.ProductImages;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;

public interface ProductService {

	Long saveProductMaster(ProductMasterBean productMasterBean);

	List<ProductMasterBean> getAllProductsPagination(int page, int per_page);

	Long deleteProductId(Integer productId);

	ProductMasterBean getProductById(Integer productId);

	List<ProductMasterBean> fetchAllProducts(Integer businessId);

	List<ProductMasterBean> fetchProductDataSuggestions(String param, Integer businessId);

	List<ProductMasterBean> fetchFourRandomByCategories(String currencyCode);

	void saveProductImage(ProductImages image);
	
	public List<ProductImagesBean> fetchProductImages(Integer productId);

}
