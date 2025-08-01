package com.example.ecommerce.configuration.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.configuration.beans.MenuTypeMasterBean;

public interface MenuTypeService {

	List<MenuTypeMasterBean> getMenuTypeMasterList();

	Integer saveMenuType(MenuTypeMasterBean bean);

	List<MenuTypeMasterBean> getAllMenusPagination(int page, int per_page);

	Long deleteMenuTypeId(Integer menuTypeId);

	MenuTypeMasterBean getMenuTypeById(Integer countryId);

}
