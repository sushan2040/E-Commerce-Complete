package com.example.ecommerce.usersrepo;

import java.util.List;

import com.example.ecommerce.configuration.beans.Level4MasterBean;

public interface LocationLevel4Repo {

	Long saveLevel4Master(Level4MasterBean bean);

	List<Level4MasterBean> fetchAllLevel4s();

	List<Level4MasterBean> getAllLevel4sPagination(int page, int per_page);

	Long deleteLevel4Id(Integer level4Id);

	Level4MasterBean getLevel4ById(Integer level4Id);

	List<Level4MasterBean> getLocationChildByParent(Integer parentId);

}
