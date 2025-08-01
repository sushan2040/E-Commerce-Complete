package com.example.ecommerce.seller.usermgmt.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.seller.usermgmt.beans.RoleMasterBean;
import com.example.ecommerce.seller.usermgmt.repo.RoleMasterRepo;

import jakarta.transaction.Transactional;

@Service
public class RoleMasterServiceImpl implements RoleMasterService{

	@Autowired
	RoleMasterRepo roleMasterRepo;

	@Override
	@Transactional
	public Integer saveRoleMaster(RoleMasterBean bean) {
		return roleMasterRepo.saveRoleMaster(bean);
	}

	@Override
	public List<RoleMasterBean> getAllRolesPagination(int page, int per_page,Integer businessId) {
		return roleMasterRepo.getAllRolesPagination(page,per_page,businessId);
	}

	@Override
	@Transactional	 
	public Long deleteRoleId(Integer roleId) {
		return roleMasterRepo.deleteRoleId(roleId);
	}

	@Override	 
	public RoleMasterBean getRoleById(Integer roleId) {
		return roleMasterRepo.getRoleById(roleId);
	}

	@Override	 
	public List<RoleMasterBean> getRoles(Integer businessId) {
		return roleMasterRepo.getRoles(businessId);
	}
}
