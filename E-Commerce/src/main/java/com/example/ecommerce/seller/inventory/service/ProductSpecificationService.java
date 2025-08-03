package com.example.ecommerce.seller.inventory.service;

import java.util.List;
import java.util.Map;

import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.beans.ProductSpecificationValueBean;
import com.example.ecommerce.configuration.masters.Users;

public interface ProductSpecificationService {

	Long saveProductSpecificationValueMaster(ProductSpecificationValueBean bean);

	List<ProductSpecificationValueBean> getAllProductSpecificationValueMasterPagination(int page, int per_page,Users parsedUser);

	Long deleteProductSpecificationValueId(Integer productSpecificationValueId);

	ProductSpecificationValueBean getProductSpecificationValueById(Integer productSpecificationValueId);

	Map<String,List<ProductSpecificationValueBean>> getProductSpecificationValues(Integer productId);

}
