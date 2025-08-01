package com.example.ecommerce.configuration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.usersrepo.UsersRepository;

import jakarta.transaction.Transactional;

@Service
public class UsersServiceImpl implements UsersService{

	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	@Transactional
	public Users findByEmail(String email) {
		return usersRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public Users save(Users user) {
		return usersRepository.save(user);
	}

	@Override
	public List<SubModuleMasterBean> getUserAccess(Users parsedUser) {
		return usersRepository.getUserAccess(parsedUser);
	}

	
}
