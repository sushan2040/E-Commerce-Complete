package com.example.ecommerce.configuration.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.ecommerce.configuration.beans.SubModuleMasterBean;

public interface SubModuleService {

	Integer saveSubModule(SubModuleMasterBean bean);

	List<SubModuleMasterBean> getSubModuleList();

	List<SubModuleMasterBean> getAllSubmodulesPagination(int page, int per_page);

	Long deleteSubModuleId(Integer subModuleId);

	SubModuleMasterBean getSubmoduleById(Integer subModuleId);

}
