package com.example.ecommerce.seller.inventory.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.config.WebPCompressor;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.service.ProductService;
import com.example.ecommerce.utils.RequestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RequestMapping(value = "/api-data/product")
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired private JwtService jwtService;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    Environment environment;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    // Save product (Asynchronous)
    @PostMapping(value = "/save")
    public ResponseEntity<Map<String, String>> saveProductMaster(HttpServletRequest request,
            @RequestPart("productMasterBean") String productMasterBean,
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("primary") MultipartFile primaryImage,
            @RequestHeader HttpHeaders headers) {
        	String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new HashMap());
            }
            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            List<SubModuleMasterBean> subModuleList = null;
            ObjectMapper mapper=new ObjectMapper();
            ProductMasterBean bean = null;
			try {
				bean = mapper.readValue(productMasterBean,ProductMasterBean.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
            		bean.setIpAddress(RequestUtils.getClientIpAddress(request));
            bean.setMacId(RequestUtils.getMacAddress(RequestUtils.getClientIpAddress(request)));
            try {
                String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
                Users parsedUser = new ObjectMapper().readValue(user, Users.class);
                bean.setBusinessId(parsedUser.getBusinessId());
                Long productId = productService.saveProductMaster(bean);
                //code to save uploaded product images
                int i = 0;
                for (MultipartFile file : files) {
                	
                	if (file != null) {
        				// saving file in the desired location
        				LocalDateTime now = LocalDateTime.now();
        				String year = String.valueOf(now.getYear());
        				String month = String.format("%02d", now.getMonthValue()); // Ensure two-digit month
        				// Construct the photo upload path
        				String basePath = environment.getProperty("product.images.path");
        				String randomPath = WebPCompressor.generateFixedRandomString(productId.toString(),
        						Integer.parseInt("7"));
        				String photoUploadPath = Paths.get(basePath, randomPath).toString();
        				// Create directories if they don't exist
        				File photoDir = new File(photoUploadPath);
        				if (!photoDir.exists()) {
        					photoDir.mkdirs();
        				}
        				// Save the file
        				File photoFile = new File(photoDir, "productPhoto_"+i+".webp");
        				try {
        					file.transferTo(photoFile);
        					System.out.println("File uploaded successfully: " + photoFile.getAbsolutePath());
        					WebPCompressor.compressWebP(photoFile, photoFile, 0.5f, photoUploadPath, "productPhoto_"+i+"");

        					// saving relative path in patients photo
//        					String photoRelPath = Paths.get(year, month, randomPath, CommonCode.PATIENT_PHOTO).toString();
//        					PatientRegDocUploadDto docUploadDto = new PatientRegDocUploadDto();
//        					docUploadDto.setPatientId(patientId.intValue());
//        					docUploadDto.setDocumentName(CommonCode.PATIENT_PHOTO);
//        					docUploadDto.setDocumentPath(photoRelPath);
//        					docUploadDto.setStatus(CommonCode.IS_NOT_DELETED);
//        					docUploadDto.setUnitId(Integer.parseInt(CommonUtils.getUserBean(request).getUserUnitId()));
//        					regService.savePatientRegDocsInfo(docUploadDto);
        				} catch (IllegalStateException | IOException e) {
        					e.printStackTrace();
        				}
                	}
                	i++;
                }

                Map<String, String> response = new HashMap<>();
                if (productId > 0) {
                    response.put("message", Constants.SUCCESS_MESSAGE);
                    response.put("status", "success");
                } else {
                    response.put("message", Constants.FAIL_MESSAGE);
                    response.put("status", "fail");
                }
                return ResponseEntity.ok(response);
            } catch (Exception e) {
            	e.printStackTrace();
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Error saving product: " + e.getMessage());
                errorResponse.put("status", "error");
                return ResponseEntity.status(500).body(errorResponse);
            }
    }

    // Fetch products with pagination (Asynchronous)
    @PostMapping("/pagination")
    public CompletableFuture<ResponseEntity<PaginationResponse<ProductMasterBean>>> getAllProductsPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
        return CompletableFuture.supplyAsync(() -> {
        	//if(redisTemplate.opsForValue().get(RedisKey.PRODUCT_PAGINATION.getKey(page,per_page))==null) {
            try {
                List<ProductMasterBean> productList = productService.getAllProductsPagination(page, per_page);
                int totalRows = !productList.isEmpty() ? productList.get(0).getTotalRecords() : 0;
                PaginationResponse<ProductMasterBean> response = new PaginationResponse<>();
                response.setPage(page);
                response.setTotalPages(totalRows);
                response.setData(productList);
                //redisTemplate.opsForValue().set(RedisKey.PRODUCT_PAGINATION.getKey(page,per_page), response);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Error fetching products: " + e.getMessage());
                errorResponse.put("status", "error");
                return ResponseEntity.status(500).body(new PaginationResponse());
            }
//        	}else {
//        		PaginationResponse<ProductMasterBean> response=(PaginationResponse<ProductMasterBean>)redisTemplate.opsForValue().get(RedisKey.PRODUCT_PAGINATION.getKey(page,per_page));
//        		return ResponseEntity.ok(response);
//        				
//        	}
        },taskExecutor);
    }

    // Delete product by ID (Asynchronous)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("id") Integer productId) {
            try {
                Long count = productService.deleteProductId(productId);
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
    @GetMapping("/get-product-byid/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getProductById(@PathVariable("id") Integer productId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ProductMasterBean bean = productService.getProductById(productId);
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
        },taskExecutor);
    }
    
    @GetMapping(value = "/fetch-all-products")
    public ResponseEntity<List<ProductMasterBean>> getAllProductsOfABusiness(@RequestHeader HttpHeaders headers){
    	String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        Users parsedUser=null;
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            parsedUser = new ObjectMapper().readValue(user, Users.class);
        }catch(Exception e) {
        	e.printStackTrace();
        }
    	List<ProductMasterBean> productList=productService.fetchAllProducts(parsedUser.getBusinessId());
    	return ResponseEntity.ok(productList);
    }
    
    @GetMapping(value="/suggestions")
	public ResponseEntity<List<ProductMasterBean>> fetchCommonDataSuggestions(@RequestParam("query") String param,
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
	List<ProductMasterBean> commonDataBeanList=productService.fetchProductDataSuggestions(param,parsedUser.getBusinessId());
	return ResponseEntity.ok(commonDataBeanList);
        }catch(Exception e) {
        	e.printStackTrace();
        	return ResponseEntity.ok(null);
        }
	}
    @GetMapping(value = "/fetch-four-random-by-categories")
    public ResponseEntity<>
    
    
}
