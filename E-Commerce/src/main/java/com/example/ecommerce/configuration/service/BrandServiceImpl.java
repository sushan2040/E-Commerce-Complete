package com.example.ecommerce.configuration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.BrandBean;
import com.example.ecommerce.usersrepo.BrandDao;

import jakarta.transaction.Transactional;

@Service
public class BrandServiceImpl implements BrandService{

	@Autowired
	BrandDao brandDao;

	@Override
	@Transactional 
	public Long saveBrandMaster(BrandBean bean) {
		return brandDao.saveBrandMaster(bean);
	}

	@Override
	public List<BrandBean> fetchAllBrands() {
		return brandDao.fetchAllBrands();
	}

	@Override 
	public List<BrandBean> getAllBrandsPagination(int page, int per_page) {
		return brandDao.getAllBrandsPagination(page,per_page);
	}

	@Override
	@Transactional 	
	public Long deletebrandId(Integer brandId) {
		return brandDao.deletebrandId(brandId);
	}

	@Override
	public BrandBean getBrandById(Integer brandId) {
		return brandDao.getBrandById(brandId);
	}
	
}
