package com.example.ecommerce.seller.usermgmt.repo;

import java.util.List;

import com.example.ecommerce.configuration.beans.DepartmentBean;

public interface DepartmentRepo {

	List<DepartmentBean> getDepartmentMasterList();

	Integer saveDepartmentMaster(DepartmentBean bean);

	List<DepartmentBean> getAllDepartmentsPagination(int page, int per_page);

	Long deleteDepartmentMaster(Integer departmentId);

	DepartmentBean getDepartmentMasterById(Integer departmentId);

}
