package com.example.ecommerce.configuration.service;

import java.util.List;


import com.example.ecommerce.configuration.beans.LocationLevelBean;

public interface LocationLevelService {

	Long saveLocationLevelMaster(LocationLevelBean bean);

	List<LocationLevelBean> fetchAllLocationLevels();

	List<LocationLevelBean> getAllLocationLevelsPagination(int page, int per_page);

	Long deletelocationLevelId(Integer locationLevelId);

	LocationLevelBean getLocationLevelById(Integer locationLevelId);

	Integer getLocationLevelsByCountryId(Integer countryId);

}
