package com.example.ecommerce.seller.usermgmt.repo;

import java.util.List;

import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;

public interface UserProductRepo {

	Integer addProductToCart(Integer productId, Integer quantity, Users users);

	Integer fetchUsersCartQuantity(Users parsedUser);

	Integer substractProductFromCart(Users parsedUser, Integer productFinalCostMasterId, Integer quantity);

	List<ProductMasterBean> fetchUsersCartProducts(Users parsedUser, String currencyCode);

}
