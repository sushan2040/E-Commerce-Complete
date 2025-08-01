package com.example.ecommerce.configuration.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.ecommerce.configuration.beans.Level5MasterBean;

public interface LocationLevel5Service {

	Long savelevel5Master(Level5MasterBean bean);

	List<Level5MasterBean> fetchAlllevel5s();

	List<Level5MasterBean> getAlllevel5sPagination(int page, int per_page);

	Level5MasterBean getlevel5ById(Integer level4Id);

	Long deletelevel5Id(Integer level4Id);

	List<Level5MasterBean> getLocationChildByParent(Integer parentId);

}
