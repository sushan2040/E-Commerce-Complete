package com.example.ecommerce.seller.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductFinalCostBean;
import com.example.ecommerce.seller.inventory.service.ProductFinalCostService;

@Controller
@RequestMapping(value = "/product-final-cost")
public class ProductFinalCostController {

	@Autowired
	ProductFinalCostService productFinalCostService;
	
	@PostMapping(value="/save")
	public ResponseEntity<AuthResponse> saveProductFinalCost(@RequestBody List<ProductFinalCostBean> beanList){
		Long productFinalCostId=productFinalCostService.saveProductFinalCost(beanList);
		AuthResponse authResponse=new AuthResponse();
		authResponse.setMessage(Constants.SUCCESS_MESSAGE);
		authResponse.setStatus("success");
		return ResponseEntity.ok(authResponse);
	}
}
