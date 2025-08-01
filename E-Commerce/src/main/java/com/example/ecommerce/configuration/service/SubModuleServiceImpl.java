package com.example.ecommerce.configuration.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.usersrepo.SubModuleDao;

@Service
public class SubModuleServiceImpl implements SubModuleService {

	private SubModuleDao subModuleDao;
	
	@Autowired
	public SubModuleServiceImpl(SubModuleDao subModuleDao) {
		this.subModuleDao=subModuleDao;
	}

	@Override
	@Transactional 
	public Integer saveSubModule(SubModuleMasterBean bean) {
		return subModuleDao.saveSubModule(bean);
	}

	@Override 
	public List<SubModuleMasterBean> getSubModuleList() {
		return subModuleDao.getSubModuleList();
	}

	@Override
	public List<SubModuleMasterBean> getAllSubmodulesPagination(int page, int per_page) {
		return subModuleDao.getAllSubmodulesPagination(page,per_page);
	}

	@Override
	@Transactional
	public Long deleteSubModuleId(Integer subModuleId) {
		return subModuleDao.deleteSubModuleId(subModuleId);
	}

	@Override
	public SubModuleMasterBean getSubmoduleById(Integer subModuleId) {
		return subModuleDao.getSubmoduleById(subModuleId);
	}
	
	
}
