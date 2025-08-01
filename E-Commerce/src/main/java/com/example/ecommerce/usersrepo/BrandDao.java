package com.example.ecommerce.usersrepo;

import java.util.List;

import com.example.ecommerce.configuration.beans.BrandBean;

public interface BrandDao {

	Long saveBrandMaster(BrandBean bean);

	List<BrandBean> fetchAllBrands();

	List<BrandBean> getAllBrandsPagination(int page, int per_page);

	Long deletebrandId(Integer brandId);

	BrandBean getBrandById(Integer brandId);

}
