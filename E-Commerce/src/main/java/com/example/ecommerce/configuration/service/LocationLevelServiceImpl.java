package com.example.ecommerce.configuration.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.LocationLevelBean;
import com.example.ecommerce.usersrepo.LocationLevelDao;

import jakarta.transaction.Transactional;

@Service
public class LocationLevelServiceImpl implements LocationLevelService{

	 @Autowired
	 LocationLevelDao locationLevelDao;

	@Override
	@Transactional
	public Long saveLocationLevelMaster(LocationLevelBean bean) {
		return locationLevelDao.saveLocationLevelMaster(bean);
	}

	@Override
	public List<LocationLevelBean> fetchAllLocationLevels() {
		return locationLevelDao.fetchAllLocationLevels();
	}

	@Override
	public List<LocationLevelBean> getAllLocationLevelsPagination(int page, int per_page) {
		return locationLevelDao.getAllLocationLevelsPagination(page,per_page);
	}

	@Override
	@Transactional
	public Long deletelocationLevelId(Integer locationLevelId) {
		return locationLevelDao.deletelocationLevelId(locationLevelId);
	}

	@Override
	 
	public LocationLevelBean getLocationLevelById(Integer locationLevelId) {
		return locationLevelDao.getLocationLevelById(locationLevelId);
	}

	@Override
	public Integer getLocationLevelsByCountryId(Integer countryId) {
		return locationLevelDao.getLocationLevelsByCountryId(countryId);
	}
}
