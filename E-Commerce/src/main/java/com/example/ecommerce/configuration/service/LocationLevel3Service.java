package com.example.ecommerce.configuration.service;

import java.util.List;


import com.example.ecommerce.configuration.beans.Level3MasterBean;

public interface LocationLevel3Service {

	Long savelevel3Master(Level3MasterBean bean);

	Level3MasterBean getlevel3ById(Integer level3Id);

	Long deletelevel3Id(Integer level3Id);

	List<Level3MasterBean> fetchAlllevel3s();

	List<Level3MasterBean> getAlllevel3sPagination(int page, int per_page);

	List<Level3MasterBean> getLocationChildByParent(Integer parentId);

}
