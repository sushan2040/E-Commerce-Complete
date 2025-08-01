package com.example.ecommerce.configuration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.configuration.masters.BridgeParameter;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.usersrepo.CommonDataDao;

import jakarta.transaction.Transactional;

@Service
public class CommonDataServiceImpl implements CommonDataService{

	@Autowired
	CommonDataDao commonDataDao;

	@Override
	public List<BridgeParameter> getBrigeParameters() {
		return commonDataDao.getBridgeParameters();
	}

	@Override
	public List<CommonDataBean> getCommonDatas(Integer businessId) {
		return commonDataDao.getCommonDatas(businessId);
	}

	@Override
	@Transactional
	public Long saveCommonData(CommonDataBean bean) {
		return commonDataDao.saveCommonData(bean);
	}

	@Override
	public List<CommonDataBean> getAllCommonDataPagination(int page, int per_page,Integer businessId) {
		return commonDataDao.getAllCommonDataPagination(page,per_page,businessId);
	}

	@Override
	@Transactional
	public Long deleteById(Integer id) {
		return commonDataDao.deleteById(id);
	}

	@Override
	public CommonDataBean getCommonDataById(Integer id) {
		return commonDataDao.getCommonDataById(id);
	}

	@Override
	public List<CommonDataBean> fetchCommonDataSuggestions(String param,Integer businessId) {
		return commonDataDao.fetchCommonDataSuggestions(param,businessId);
	}

	@Override
	public List<CommonDataBean> fetchProductCategoryList() {
		return commonDataDao.fetchProductCategoryList();
	}

}
