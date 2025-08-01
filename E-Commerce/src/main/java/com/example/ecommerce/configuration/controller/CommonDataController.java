package com.example.ecommerce.configuration.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.configuration.beans.BrandBean;
import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.BridgeParameter;
import com.example.ecommerce.configuration.masters.CommonData;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.configuration.service.CommonDataService;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

@Controller
@RequestMapping(value = "/api-data/common-data")
public class CommonDataController {

	@Autowired
	CommonDataService commonDataService;
	
    @Autowired private JwtService jwtService;
	
	@Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
	
	@GetMapping(value = "/get-bridge-parameters")
	public ResponseEntity<List<BridgeParameter>> getBridgeParameters(){
		List<BridgeParameter> bridgeParameters=commonDataService.getBrigeParameters();
		return ResponseEntity.ok(bridgeParameters);
	}
	@GetMapping(value = "/get-common-datas")
	public ResponseEntity<List<CommonDataBean>> getCommonDatas(@RequestHeader HttpHeaders headers){
		String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
		List<CommonDataBean> commonDatas=new ArrayList<CommonDataBean>();
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
        List<SubModuleMasterBean> subModuleList = null;
        try {
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
		commonDatas=commonDataService.getCommonDatas(parsedUser.getBusinessId());
        }catch(Exception e) {
        	e.printStackTrace();
        }
		return ResponseEntity.ok(commonDatas);
	}
	@PostMapping(value = "/save")
	public ResponseEntity<AuthResponse> saveCommonData(@RequestBody CommonDataBean bean,
			@RequestHeader HttpHeaders headers){
		String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
        List<SubModuleMasterBean> subModuleList = null;
        try {
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
           bean.setBusinessId(parsedUser.getBusinessId());
		Long commonDataId=commonDataService.saveCommonData(bean);
        }catch(Exception e) {
        	e.printStackTrace();
        }
		AuthResponse response=new AuthResponse();
		response.setMessage(Constants.SUCCESS_MESSAGE);
		response.setStatus("success");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value="/suggestions")
	public ResponseEntity<List<CommonDataBean>> fetchCommonDataSuggestions(@RequestParam("query") String param,
			@RequestHeader HttpHeaders headers){
		String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
	List<CommonDataBean> commonDataBeanList=commonDataService.fetchCommonDataSuggestions(param,parsedUser.getBusinessId());
	return ResponseEntity.ok(commonDataBeanList);
        }catch(Exception e) {
        	e.printStackTrace();
        	return ResponseEntity.ok(null);
        }
	}	
	// ✅ Asynchronous Pagination method
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<CommonDataBean>> getAllCommonDataPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page,
            @RequestHeader HttpHeaders headers) {
    		List<CommonDataBean> commonDataList=new ArrayList<CommonDataBean>();
        	String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(null);
            }
            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
                Users parsedUser = new ObjectMapper().readValue(user, Users.class);
            commonDataList = commonDataService.getAllCommonDataPagination(page, per_page,parsedUser.getBusinessId());
            }catch(Exception e) {
            	e.printStackTrace();
            }
            int totalRows = !commonDataList.isEmpty() ? commonDataList.get(0).getTotalRecords() : 0;
            PaginationResponse<CommonDataBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(commonDataList);
            return ResponseEntity.ok(response);
    }
    @PostMapping(value = "/delete-by-id/{id}")
    public ResponseEntity<AuthResponse> deleteById(@PathVariable("id") Integer id){
    	
    	Long deletedCommonDataId=commonDataService.deleteById(id);
    	AuthResponse authResponse=new AuthResponse();
    	authResponse.setMessage(Constants.DELETE_SUCCESS_MESSAGE);
    	authResponse.setStatus("success");
    	return ResponseEntity.ok(authResponse);
    }
    @PostMapping(value = "/get-by-id/{id}")
    public ResponseEntity<CommonDataBean> getById(@PathVariable("id") Integer id){
    	
    	CommonDataBean commonData=commonDataService.getCommonDataById(id);
    	return ResponseEntity.ok(commonData);
    }
    @GetMapping(value = "/fetch-product-categories")
    public ResponseEntity<List<CommonDataBean>> fetchProductCategoryList(){
    	List<CommonDataBean> productCategoryList=commonDataService.fetchProductCategoryList();
    	return ResponseEntity.ok(productCategoryList);
    }
}
