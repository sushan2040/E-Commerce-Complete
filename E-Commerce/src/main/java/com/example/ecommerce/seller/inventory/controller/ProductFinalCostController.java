package com.example.ecommerce.seller.inventory.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductFinalCostBean;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.service.ProductFinalCostService;

@Controller
@RequestMapping(value = "/product-final-cost")
public class ProductFinalCostController {

	@Autowired
	ProductFinalCostService productFinalCostService;
	
	@PostMapping(value="/save")
	public ResponseEntity<AuthResponse> saveProductFinalCost(@RequestBody List<ProductFinalCostBean> bean){
		Long productFinalCostId=productFinalCostService.saveProductFinalCost(bean);
		AuthResponse authResponse=new AuthResponse();
		authResponse.setMessage(Constants.SUCCESS_MESSAGE);
		authResponse.setStatus("success");
		return ResponseEntity.ok(authResponse);
	}
	@PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<ProductFinalCostBean>> getAllCountriesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
            List<ProductFinalCostBean> countryList = productFinalCostService.getAllProductFinalCostPagination(page, per_page);
            int totalRows = !countryList.isEmpty() ? countryList.get(0).getTotalRecords() : 0;
            PaginationResponse<ProductFinalCostBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(countryList);
            return ResponseEntity.ok(response);
    }
	@PostMapping(value="/check-combination")
	public ResponseEntity<Long> checkCombinationExists(@RequestBody ProductFinalCostBean bean){
		try {
			Long count=productFinalCostService.checkCombinationExists(bean);
			return ResponseEntity.ok(count);
		}catch(Exception e) {
			return ResponseEntity.ok(0L);
		}
		
	}
	@GetMapping(value = "/fetch-final-product-by-id-contrywise")
	public ResponseEntity<ProductMasterBean> fetchProductFinalCostMaster(@RequestParam("productId") Integer productId,
			@RequestParam("currencyCode") String currencyCode){
		ProductMasterBean bean=productFinalCostService.fetchProductById(productId,currencyCode);
		return ResponseEntity.ok(bean);
	}
}
