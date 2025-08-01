package com.example.ecommerce.seller.inventory.repo;

import java.util.List;

import com.example.ecommerce.seller.inventory.beans.ProductImagesBean;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.masters.ProductImages;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;

public interface ProductDao {

	Long saveProductMaster(ProductMasterBean productMasterBean);

	List<ProductMasterBean> getAllProductsPagination(int page, int per_page);

	Long deleteProductId(Integer productId);

	ProductMasterBean getProductById(Integer productId);

	List<ProductMasterBean> fetchAllProducts(Integer businessId);

	List<ProductMasterBean> fetchProductDataSuggestions(String param, Integer businessId);

	List<ProductMasterBean> fetchProductsByCateoryId(Integer commonDataId, String currencyCode);

	void saveProductImage(ProductImages image);

	List<ProductImagesBean> fetchProductImages(Integer productId);

	

}
