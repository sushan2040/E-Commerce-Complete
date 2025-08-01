package com.example.ecommerce.configuration.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.ecommerce.configuration.beans.BrandBean;

public interface BrandService {

	Long saveBrandMaster(BrandBean bean);

	List<BrandBean> fetchAllBrands();
	
	List<BrandBean> getAllBrandsPagination(int page, int per_page);

	Long deletebrandId(Integer brandId);

	BrandBean getBrandById(Integer brandId);

}
