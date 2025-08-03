package com.example.ecommerce.seller.usermgmt.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.usermgmt.service.UserProductCartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

@Controller
@RequestMapping(value = "/customer")
public class CustomerController {
	
	@Autowired
	UserProductCartService productCartService;
	
	 @Autowired private JwtService jwtService;

	@PostMapping(value = "/add-product-to-cart")
	public ResponseEntity<AuthResponse> addProductToCart(@RequestParam("productFinalCostMasterId") Integer productFinalCostMasterId,
			@RequestParam("quantity") Integer quantity,
			@RequestHeader HttpHeaders headers) throws JsonMappingException, JsonProcessingException{
		String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
		Integer count=productCartService.addProductToCart(productFinalCostMasterId,quantity,parsedUser);
		AuthResponse authResponse=new AuthResponse();
		if(count>0) {
			authResponse.setMessage(Constants.SUCCESS_MESSAGE);
			authResponse.setStatus("success");
		}else {
			authResponse.setMessage(Constants.FAIL_MESSAGE);
			authResponse.setStatus("error");
		}
		return ResponseEntity.ok(authResponse);
	}
	
	@GetMapping(value = "/fetch-users-cart-count")
	public ResponseEntity<Integer> fetchUsersCartQuantity(@RequestHeader HttpHeaders headers) throws JsonMappingException, JsonProcessingException{
		String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
		Integer count=productCartService.fetchUsersCartQuantity(parsedUser);
		return ResponseEntity.ok(count);
	}
	
	@PostMapping(value = "/substract-product-from-cart")
	public ResponseEntity<AuthResponse> substractProductFromCart(@RequestParam("productFinalCostMasterId") Integer productFinalCostMasterId,
			@RequestParam("quantity") Integer quantity,
			@RequestHeader HttpHeaders headers) throws JsonMappingException, JsonProcessingException{
		String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
		Integer count=productCartService.substractProductFromCart(parsedUser,productFinalCostMasterId,quantity);
		AuthResponse authResponse=new AuthResponse();
		if(count>0) {
			authResponse.setMessage(Constants.SUCCESS_MESSAGE);
			authResponse.setStatus("success");
		}else {
			authResponse.setMessage(Constants.FAIL_MESSAGE);
			authResponse.setStatus("error");
		}
		return ResponseEntity.ok(authResponse);	
	}
	@GetMapping(value = "/fetch-users-cart-products")
	public ResponseEntity<List<ProductMasterBean>> fetchUsersCartProducts(
			@RequestParam("currencyCode") String currencyCode
			,@RequestHeader HttpHeaders headers)
			throws JsonMappingException, JsonProcessingException{
		String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
        List<ProductMasterBean> productsList=productCartService.fetchUsersCartProducts(parsedUser,currencyCode);    
        return ResponseEntity.ok(productsList);	
		
	}
}
