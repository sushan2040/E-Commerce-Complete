package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

import com.example.ecommerce.configuration.beans.BrandBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.BrandService;
import com.example.ecommerce.constants.Constants;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api-data/brand")
public class BrandController {

    @Autowired
    BrandService brandService;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // ✅ Asynchronous Save method
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveBrand(
            @RequestHeader HttpHeaders headers,
            @RequestBody BrandBean bean,
            HttpServletRequest request) {
        //return CompletableFuture.supplyAsync(() ->{
            try {
                bean.setStatus(Constants.USER_STATUS_ACTIVE);
                bean.setDeleted(Constants.USER_NOT_DELETED);
                Long count = brandService.saveBrandMaster(bean);
                Map<String, String> response = new HashMap<>();
                response.put("message", count == 1 ? "success" : "error");

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, String> response = new HashMap<>();
                response.put("message", "error");
                return ResponseEntity.ok(response);
            }
       // },taskExecutor);
    }

    // ✅ Asynchronous Get All Brands method
    @GetMapping("/fetch-all-Brands")
    public CompletableFuture<ResponseEntity<List<BrandBean>>> fetchAllBrands() {
        return CompletableFuture.supplyAsync(() ->{
        	if(redisTemplate.opsForValue().get(RedisKey.BRANDS_ALL.getKey())==null) {
            List<BrandBean> Brands = null;
            Brands = brandService.fetchAllBrands();
            redisTemplate.opsForValue().set(RedisKey.BRANDS_ALL.getKey(), Brands);
            return ResponseEntity.ok(Brands);
        	}else {
        		List<BrandBean> brands=(List<BrandBean>)redisTemplate.opsForValue().get(RedisKey.BRANDS_ALL.getKey());
        		return ResponseEntity.ok(brands);
        	}
        },taskExecutor);
    }

    // ✅ Asynchronous Pagination method
    @PostMapping("/pagination")
    public CompletableFuture<ResponseEntity<PaginationResponse<BrandBean>>> getAllBrandsPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
        return CompletableFuture.supplyAsync(() ->{
        	if(redisTemplate.opsForValue().get(RedisKey.BRANDS_PAGINATION.getKey(page,per_page))==null) {
            List<BrandBean> BrandList = null;
            BrandList = brandService.getAllBrandsPagination(page, per_page);
            int totalRows = !BrandList.isEmpty() ? BrandList.get(0).getTotalRecords() : 0;

            PaginationResponse<BrandBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(BrandList);
            redisTemplate.opsForValue().set(RedisKey.BRANDS_PAGINATION.getKey(page,per_page),response);
            return ResponseEntity.ok(response);
        	}else {
        		PaginationResponse<BrandBean> response=(PaginationResponse<BrandBean>)redisTemplate.opsForValue().get(RedisKey.BRANDS_PAGINATION.getKey(page,per_page));
        		return ResponseEntity.ok(response);
        	}
        },taskExecutor);
    }

    // ✅ Asynchronous Delete method
    @DeleteMapping("/delete/{id}")
    public CompletableFuture<ResponseEntity<Map<String, String>>> deleteBrand(@PathVariable("id") Integer brandId) {
        return CompletableFuture.supplyAsync(() ->{
            Long count = 0L;
            count = brandService.deletebrandId(brandId);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
        },taskExecutor);
    }

    // ✅ Asynchronous Get Brand by ID method
    @GetMapping("/get-Brand-byid/{id}")
    public CompletableFuture<ResponseEntity<BrandBean>> getBrandById(@PathVariable("id") Integer brandId) {
        return CompletableFuture.supplyAsync(() ->{
            BrandBean bean = null;
            bean = brandService.getBrandById(brandId);
            return ResponseEntity.ok(bean);
        },taskExecutor);
    }
}
