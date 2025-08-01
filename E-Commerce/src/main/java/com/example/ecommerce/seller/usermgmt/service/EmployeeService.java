package com.example.ecommerce.seller.usermgmt.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.seller.usermgmt.beans.EmployeeMasterBean;

public interface EmployeeService {

	List<EmployeeMasterBean> getAllEmployeesPagination(int page, int per_page, Integer businessId);

	Integer saveEmloyeeMaster(EmployeeMasterBean bean);

	EmployeeMasterBean findByUsername(String username, String password);

}
