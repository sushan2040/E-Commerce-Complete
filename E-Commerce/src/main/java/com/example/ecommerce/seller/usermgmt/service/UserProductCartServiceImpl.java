package com.example.ecommerce.seller.usermgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.usermgmt.repo.UserProductRepo;

import jakarta.transaction.Transactional;

@Service
public class UserProductCartServiceImpl implements UserProductCartService {

	@Autowired
	UserProductRepo userProductRepo;
	
	@Override
	@Transactional
	public Integer addProductToCart(Integer productFinalCostMasterId, Integer quantity,Users users) {
		return userProductRepo.addProductToCart(productFinalCostMasterId,quantity,users);
	}

	@Override
	public Integer fetchUsersCartQuantity(Users parsedUser) {
		return userProductRepo.fetchUsersCartQuantity(parsedUser);
	}

	@Override
	@Transactional
	public Integer substractProductFromCart(Users parsedUser, Integer productFinalCostMasterId, Integer quantity) {
		return userProductRepo.substractProductFromCart(parsedUser,productFinalCostMasterId,quantity);
	}

	@Override
	public List<ProductMasterBean> fetchUsersCartProducts(Users parsedUser, String currencyCode) {
		return userProductRepo.fetchUsersCartProducts(parsedUser,currencyCode);
	}

	
}
