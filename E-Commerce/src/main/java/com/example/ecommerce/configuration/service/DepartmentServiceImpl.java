package com.example.ecommerce.configuration.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.DepartmentBean;
import com.example.ecommerce.seller.usermgmt.repo.DepartmentRepo;

import jakarta.transaction.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService{

	@Autowired
	DepartmentRepo departmentRepo;
	
	@Override
	public List<DepartmentBean> getDepartmentMasterList() {
		return departmentRepo.getDepartmentMasterList();
	}

	@Override
	@Transactional
	public Integer saveDepartmentMaster(DepartmentBean bean) {
		return departmentRepo.saveDepartmentMaster(bean);
	}

	@Override
	public List<DepartmentBean> getAllDepartmentsPagination(int page, int per_page) {
		return departmentRepo.getAllDepartmentsPagination(page,per_page);
	}

	@Override
	@Transactional
	public Long deleteDepartmentMasterId(Integer departmentId) {
		return departmentRepo.deleteDepartmentMaster(departmentId);
	}

	@Override
	public DepartmentBean getDepartmentMasterById(Integer departmentId) {
		return departmentRepo.getDepartmentMasterById(departmentId);
	}

}
