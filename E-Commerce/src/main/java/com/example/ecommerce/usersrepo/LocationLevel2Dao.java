package com.example.ecommerce.usersrepo;

import java.util.List;

import com.example.ecommerce.configuration.beans.Level2MasterBean;

public interface LocationLevel2Dao {

	Long saveLevel2Master(Level2MasterBean bean);

	List<Level2MasterBean> fetchAllLevel2s();

	List<Level2MasterBean> getAllLevel2sPagination(int page, int per_page);

	Long deleteLevel2Id(Integer level2Id);

	Level2MasterBean getLevel2ById(Integer level2Id);

	List<Level2MasterBean> getLocationChildByParent(Integer parentId);

}
