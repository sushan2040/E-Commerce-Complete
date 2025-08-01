package com.example.ecommerce.configuration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce.configuration.beans.MenuTypeMasterBean;
import com.example.ecommerce.usersrepo.MenuTypeDao;

@Service
public class MenuTypeServiceImpl implements MenuTypeService{

	private MenuTypeDao menuTypeDao;
	
	@Autowired
	public MenuTypeServiceImpl(MenuTypeDao menuTypeDao) {
		this.menuTypeDao=menuTypeDao;
	}

	@Override
	public List<MenuTypeMasterBean> getMenuTypeMasterList() {
		return menuTypeDao.getMenuTypeMasterList();
	}

	@Override
	@Transactional
	public Integer saveMenuType(MenuTypeMasterBean bean) {
		return menuTypeDao.saveMenuType(bean);
	}

	@Override
	public List<MenuTypeMasterBean> getAllMenusPagination(int page, int per_page) {
		return menuTypeDao.getAllMenusPagination(page,per_page);
	}
	@Override
	@Transactional
	public Long deleteMenuTypeId(Integer menuTypeId) {
		return menuTypeDao.deleteMenuTypeId(menuTypeId);
	}
	@Override
	public MenuTypeMasterBean getMenuTypeById(Integer menuTypeId) {
		return menuTypeDao.getMenuTypeById(menuTypeId);
	}
}
