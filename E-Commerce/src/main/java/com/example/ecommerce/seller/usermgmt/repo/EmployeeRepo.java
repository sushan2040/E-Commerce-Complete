package com.example.ecommerce.seller.usermgmt.repo;

import java.util.List;

import com.example.ecommerce.seller.usermgmt.beans.EmployeeMasterBean;

public interface EmployeeRepo {

	List<EmployeeMasterBean> getAllEmployeesPagination(int page, int per_page, Integer businessId);

	Integer saveEmployeeMaster(EmployeeMasterBean bean);

	EmployeeMasterBean findByUsername(String username, String password);

}
