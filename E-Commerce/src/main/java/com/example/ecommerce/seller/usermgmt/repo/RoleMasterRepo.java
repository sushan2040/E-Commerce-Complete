package com.example.ecommerce.seller.usermgmt.repo;

import java.util.List;

import com.example.ecommerce.configuration.beans.BrandBean;
import com.example.ecommerce.seller.usermgmt.beans.RoleMasterBean;

public interface RoleMasterRepo {

	Integer saveRoleMaster(RoleMasterBean bean);

	List<RoleMasterBean> getAllRolesPagination(int page, int per_page, Integer businessId);

	Long deleteRoleId(Integer roleId);

	RoleMasterBean getRoleById(Integer roleId);

	List<RoleMasterBean> getRoles(Integer businessId);

}
