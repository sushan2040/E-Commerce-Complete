package com.example.ecommerce.seller.usermgmt.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.seller.usermgmt.beans.EmployeeMasterBean;
import com.example.ecommerce.seller.usermgmt.repo.EmployeeRepo;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepo employeeRepo;

	@Override
	public List<EmployeeMasterBean> getAllEmployeesPagination(int page, int per_page,Integer businessId) {
		return employeeRepo.getAllEmployeesPagination(page,per_page,businessId);
	}

	@Override
	public Integer saveEmloyeeMaster(EmployeeMasterBean bean) {
		return employeeRepo.saveEmployeeMaster(bean);
	}

	@Override
	public EmployeeMasterBean findByUsername(String username,String password) {
		return employeeRepo.findByUsername(username,password);
	}
}
