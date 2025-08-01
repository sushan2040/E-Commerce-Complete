package com.example.ecommerce.usersrepo;

import java.util.List;

import com.example.ecommerce.configuration.beans.Level3MasterBean;

public interface LocationLevel3Repo {

	Long saveLevel3Master(Level3MasterBean bean);

	Level3MasterBean getLevelBy3ById(Integer level3Id);

	Long deleteLevel3Id(Integer level3Id);

	List<Level3MasterBean> fetchAllLevel3s();

	List<Level3MasterBean> getAllLevel3sPagination(int page, int per_page);

	List<Level3MasterBean> getLocationChildByParent(Integer parentId);

}
