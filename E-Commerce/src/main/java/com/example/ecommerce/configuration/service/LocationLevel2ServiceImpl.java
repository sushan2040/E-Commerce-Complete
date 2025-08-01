package com.example.ecommerce.configuration.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.Level2MasterBean;
import com.example.ecommerce.usersrepo.LocationLevel2Dao;

import jakarta.transaction.Transactional;

@Service
public class LocationLevel2ServiceImpl implements LocationLevel2Service{

	@Autowired
	private LocationLevel2Dao level2Dao;

	@Override
	@Transactional
	public Long savelevel2Master(Level2MasterBean bean) {
		return level2Dao.saveLevel2Master(bean);
	}

	@Override
	public List<Level2MasterBean> fetchAlllevel2s() {
		return level2Dao.fetchAllLevel2s();
	}

	@Override
	public List<Level2MasterBean> getAlllevel2sPagination(int page, int per_page) {
		return level2Dao.getAllLevel2sPagination(page,per_page);
	}

	@Override
	@Transactional
	public Long deletelevel2Id(Integer level2Id) {
		return level2Dao.deleteLevel2Id(level2Id);
	}

	@Override
	public Level2MasterBean getlevel2ById(Integer level2Id) {
		return level2Dao.getLevel2ById(level2Id);
	}

	@Override
	public List<Level2MasterBean> getLocationChildByParent(Integer parentId) {
		return level2Dao.getLocationChildByParent(parentId);
	}
	
	
}
