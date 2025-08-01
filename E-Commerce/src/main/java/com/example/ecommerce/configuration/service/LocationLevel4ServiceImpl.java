package com.example.ecommerce.configuration.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.Level4MasterBean;
import com.example.ecommerce.usersrepo.LocationLevel4Repo;

import jakarta.transaction.Transactional;

@Service
public class LocationLevel4ServiceImpl implements LocationLevel4Service{

	@Autowired
	LocationLevel4Repo locationLevel4Repo;

	@Override
	@Transactional
	 
	public Long savelevel4Master(Level4MasterBean bean) {
		return locationLevel4Repo.saveLevel4Master(bean);
	}

	@Override
	 
	public List<Level4MasterBean> fetchAlllevel4s() {
		return locationLevel4Repo.fetchAllLevel4s();
	}

	@Override
	 
	public List<Level4MasterBean> getAlllevel4sPagination(int page, int per_page) {
		return locationLevel4Repo.getAllLevel4sPagination(page,per_page);
	}

	@Override
	@Transactional
	 
	public Long deletelevel4Id(Integer level4Id) {
		return locationLevel4Repo.deleteLevel4Id(level4Id);
	}

	@Override	 
	public Level4MasterBean getlevel4ById(Integer level4Id) {
		return locationLevel4Repo.getLevel4ById(level4Id);
	}

	@Override
	public List<Level4MasterBean> getLocationChildByParent(Integer parentId) {
		return locationLevel4Repo.getLocationChildByParent(parentId);
	}
}
