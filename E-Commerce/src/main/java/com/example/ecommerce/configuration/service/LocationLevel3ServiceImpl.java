package com.example.ecommerce.configuration.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.Level3MasterBean;
import com.example.ecommerce.usersrepo.LocationLevel3Repo;

import jakarta.transaction.Transactional;

@Service
public class LocationLevel3ServiceImpl implements LocationLevel3Service{

	@Autowired
	LocationLevel3Repo locationLevel3Repo;

	@Override
	@Transactional
	 
	public Long savelevel3Master(Level3MasterBean bean) {
		return locationLevel3Repo.saveLevel3Master(bean);
	}

	@Override
	public Level3MasterBean getlevel3ById(Integer level3Id) {
		return locationLevel3Repo.getLevelBy3ById(level3Id);
	}

	@Override
	@Transactional
	public Long deletelevel3Id(Integer level3Id) {
		return locationLevel3Repo.deleteLevel3Id(level3Id);
	}

	@Override
	public List<Level3MasterBean> fetchAlllevel3s() {
		return locationLevel3Repo.fetchAllLevel3s();
	}

	@Override	 
	public List<Level3MasterBean> getAlllevel3sPagination(int page, int per_page) {
		return locationLevel3Repo.getAllLevel3sPagination(page,per_page);
	}

	@Override
	public List<Level3MasterBean> getLocationChildByParent(Integer parentId) {
		return locationLevel3Repo.getLocationChildByParent(parentId);
	}
}
