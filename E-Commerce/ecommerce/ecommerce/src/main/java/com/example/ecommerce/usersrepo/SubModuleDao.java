package com.example.ecommerce.usersrepo;

import java.util.List;

import com.example.ecommerce.configuration.beans.SubModuleMasterBean;

public interface SubModuleDao {

	Integer saveSubModule(SubModuleMasterBean bean);

	List<SubModuleMasterBean> getSubModuleList();

	List<SubModuleMasterBean> getAllSubmodulesPagination(int page, int per_page);

	Long deleteSubModuleId(Integer subModuleId);

	SubModuleMasterBean getSubmoduleById(Integer subModuleId);

}
