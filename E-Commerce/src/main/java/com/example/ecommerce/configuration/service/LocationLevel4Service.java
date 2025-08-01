package com.example.ecommerce.configuration.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.ecommerce.configuration.beans.Level4MasterBean;

public interface LocationLevel4Service {

	Long savelevel4Master(Level4MasterBean bean);

	List<Level4MasterBean> fetchAlllevel4s();

	List<Level4MasterBean> getAlllevel4sPagination(int page, int per_page);

	Long deletelevel4Id(Integer level4Id);

	Level4MasterBean getlevel4ById(Integer level4Id);

	List<Level4MasterBean> getLocationChildByParent(Integer parentId);

}
