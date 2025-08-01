package com.example.ecommerce.configuration.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.Level5MasterBean;
import com.example.ecommerce.usersrepo.LocationLevel5Repo;

import jakarta.transaction.Transactional;

@Service
public class LocationLevel5ServiceImpl implements LocationLevel5Service{

	@Autowired
	LocationLevel5Repo locationLevel5Repo;

	@Override
	@Transactional
	 
	public Long savelevel5Master(Level5MasterBean bean) {
		return locationLevel5Repo.savelevel5Master(bean);
	}

	@Override
	public List<Level5MasterBean> fetchAlllevel5s() {
		return locationLevel5Repo.fetchAlllevel5s();
	}

	@Override
	public List<Level5MasterBean> getAlllevel5sPagination(int page, int per_page) {
		return locationLevel5Repo.getAlllevel5sPagination(page,per_page);
	}

	@Override
	public Level5MasterBean getlevel5ById(Integer level5Id) {
		return locationLevel5Repo.getlevel5ById(level5Id);
	}

	@Override
	@Transactional
	public Long deletelevel5Id(Integer level5Id) {
		return locationLevel5Repo.deletelevel5Id(level5Id);
	}

	@Override
	public List<Level5MasterBean> getLocationChildByParent(Integer parentId) {
		return locationLevel5Repo.getLocationChildByParent(parentId);
	}
}
