package com.example.ecommerce.usersrepo;

import java.util.List;

import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.masters.Users;

public interface UsersRepository {

	public Users findByEmail(String email);

	public Users save(Users user);

	public List<SubModuleMasterBean> getUserAccess(Users parsedUser);

	public List<SubModuleMasterBean> getEmployeeAccess(Users parsedUser);
}
