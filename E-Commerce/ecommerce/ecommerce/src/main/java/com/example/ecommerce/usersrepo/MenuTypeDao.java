package com.example.ecommerce.usersrepo;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.ecommerce.configuration.beans.MenuTypeMasterBean;

public interface MenuTypeDao {

	List<MenuTypeMasterBean> getMenuTypeMasterList();

	Integer saveMenuType(MenuTypeMasterBean bean);

	List<MenuTypeMasterBean> getAllMenusPagination(int page, int per_page);

	Long deleteMenuTypeId(Integer menuTypeId);

	MenuTypeMasterBean getMenuTypeById(Integer menuTypeId);

}
