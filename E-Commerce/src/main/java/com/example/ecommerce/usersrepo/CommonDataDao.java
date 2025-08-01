package com.example.ecommerce.usersrepo;

import java.util.List;

import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.configuration.masters.BridgeParameter;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;

public interface CommonDataDao {

	List<BridgeParameter> getBridgeParameters();

	List<CommonDataBean> getCommonDatas(Integer businessId);

	Long saveCommonData(CommonDataBean bean);

	List<CommonDataBean> getAllCommonDataPagination(int page, int per_page, Integer businessId);

	Long deleteById(Integer id);

	CommonDataBean getCommonDataById(Integer id);

	List<CommonDataBean> fetchCommonDataSuggestions(String param, Integer businessId);

	List<CommonDataBean> fetchProductCategoryList();

}
