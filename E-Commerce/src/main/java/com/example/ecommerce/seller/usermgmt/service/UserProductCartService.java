package com.example.ecommerce.seller.usermgmt.service;

import java.util.List;

import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;

public interface UserProductCartService {

	Integer addProductToCart(Integer productFinalCostMasterId, Integer quantity, Users parsedUser);

	Integer fetchUsersCartQuantity(Users parsedUser);

	Integer substractProductFromCart(Users parsedUser, Integer productFinalCostMasterId, Integer quantity);

	List<ProductMasterBean> fetchUsersCartProducts(Users parsedUser, String currencyCode);

}
