package com.example.ecommerce.seller.inventory.repo;

import java.util.List;
import java.util.Map;

import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.seller.inventory.beans.ProductSpecificationValueBean;

public interface ProductSpecificationDao {

	Long saveProductSpecificationValueMaster(ProductSpecificationValueBean bean);

	List<ProductSpecificationValueBean> getAllProductSpecificationValueMasterPagination(int page, int per_page);

	Long deleteProductSpecificationValueId(Integer productSpecificationValueId);

	ProductSpecificationValueBean getProductSpecificationValueById(Integer productSpecificationValueId);

	Map<String,List<ProductSpecificationValueBean>> getProductSpecificationValues(Integer productId);

}
