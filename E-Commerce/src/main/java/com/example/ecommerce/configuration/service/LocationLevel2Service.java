package com.example.ecommerce.configuration.service;

import java.util.List;


import com.example.ecommerce.configuration.beans.Level2MasterBean;

public interface LocationLevel2Service {

	Long savelevel2Master(Level2MasterBean bean);

	List<Level2MasterBean> fetchAlllevel2s();

	List<Level2MasterBean> getAlllevel2sPagination(int page, int per_page);

	Long deletelevel2Id(Integer level2Id);

	Level2MasterBean getlevel2ById(Integer level2Id);

	List<Level2MasterBean> getLocationChildByParent(Integer parentId);

}
