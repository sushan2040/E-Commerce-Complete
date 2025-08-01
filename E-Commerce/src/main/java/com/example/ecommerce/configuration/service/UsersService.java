package com.example.ecommerce.configuration.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.masters.Users;

public interface UsersService {

	public Users findByEmail(String email);

	public Users save(Users user);

	public List<SubModuleMasterBean> getUserAccess(Users parsedUser);

	public List<SubModuleMasterBean> getEmployeeAccess(Users parsedUser);
}
