package com.example.ecommerce.configuration.service;

import java.util.List;


import com.example.ecommerce.configuration.beans.DepartmentBean;

public interface DepartmentService {

	List<DepartmentBean> getDepartmentMasterList();

	Integer saveDepartmentMaster(DepartmentBean bean);

	List<DepartmentBean> getAllDepartmentsPagination(int page, int per_page);

	Long deleteDepartmentMasterId(Integer departmentId);

	DepartmentBean getDepartmentMasterById(Integer departmentId);

}
