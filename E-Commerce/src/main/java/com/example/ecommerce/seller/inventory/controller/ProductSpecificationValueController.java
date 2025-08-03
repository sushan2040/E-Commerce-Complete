package com.example.ecommerce.seller.inventory.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductSpecificationValueBean;
import com.example.ecommerce.seller.inventory.service.ProductSpecificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api-data/product-specification")
public class ProductSpecificationValueController {
	
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    @Autowired private JwtService jwtService;
	
	@Autowired
	ProductSpecificationService productSpecificationService;

	@PostMapping(value = "/save")
	public ResponseEntity<AuthResponse> saveProductSpecificationMaster(@RequestBody ProductSpecificationValueBean bean){
		
		Long productSpecificationId=productSpecificationService.saveProductSpecificationValueMaster(bean);
		AuthResponse authResponse=new AuthResponse();
		authResponse.setMessage(Constants.SUCCESS_MESSAGE);
		authResponse.setStatus("success");
		return ResponseEntity.ok(authResponse);
	}
	 // ✅ Asynchronous Pagination method
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<ProductSpecificationValueBean>> getAllProductSpecificationValueMasterPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page,
             @RequestHeader HttpHeaders headers,
            HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
                 String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
               
            }
            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
            List<ProductSpecificationValueBean> productSpecificationList = null;
            productSpecificationList = productSpecificationService.getAllProductSpecificationValueMasterPagination(page, per_page,parsedUser);
            int totalRows = !productSpecificationList.isEmpty() ? productSpecificationList.get(0).getTotalRecords() : 0;

            PaginationResponse<ProductSpecificationValueBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(productSpecificationList);
            return ResponseEntity.ok(response);
    }
    
 // Delete product by ID (Asynchronous)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("id") Integer productSpecificationValueId) {
            try {
                Long count = productSpecificationService.deleteProductSpecificationValueId(productSpecificationValueId);
                Map<String, String> response = new HashMap<>();
                response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
                response.put("status", count > 0 ? "success" : "fail");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Error deleting product: " + e.getMessage());
                errorResponse.put("status", "error");
                return ResponseEntity.status(500).body(errorResponse);
            }
    }

    // Get product by ID (Asynchronous)
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable("id") Integer productSpecificationValueId) {
            try {
            	ProductSpecificationValueBean bean = productSpecificationService.getProductSpecificationValueById(productSpecificationValueId);
                Map<String, Object> response = new HashMap<>();
                if (bean != null) {
                    response.put("data", bean);
                    response.put("message", "Product found");
                    response.put("status", "success");
                } else {
                    response.put("message", "Product not found");
                    response.put("status", "fail");
                }
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Error fetching product: " + e.getMessage());
                errorResponse.put("status", "error");
                return ResponseEntity.status(500).body(errorResponse);
            }
    }
    
    @GetMapping(value = "/get-products-specification-and-values")
    public ResponseEntity<String> getProductSpecificationAndValues(@RequestParam("productId") Integer productId){
    	Map<String,List<ProductSpecificationValueBean>> productSpecificationValues=productSpecificationService.getProductSpecificationValues(productId);
    	ObjectMapper mapper=new ObjectMapper();
    	String result = null;
		try {
			result = mapper.writeValueAsString(productSpecificationValues);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ResponseEntity.ok(result);
    }
}
