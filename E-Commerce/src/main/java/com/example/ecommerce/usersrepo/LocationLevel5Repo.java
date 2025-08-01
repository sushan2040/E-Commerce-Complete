package com.example.ecommerce.usersrepo;

import java.util.List;

import com.example.ecommerce.configuration.beans.Level5MasterBean;

public interface LocationLevel5Repo {

	Long savelevel5Master(Level5MasterBean bean);

	List<Level5MasterBean> fetchAlllevel5s();

	List<Level5MasterBean> getAlllevel5sPagination(int page, int per_page);

	Level5MasterBean getlevel5ById(Integer level5Id);

	Long deletelevel5Id(Integer level5Id);

	List<Level5MasterBean> getLocationChildByParent(Integer parentId);

}
