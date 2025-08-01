package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.beans.Level3MasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.LocationLevel3Service;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/location-level3")
public class LocationLevel3Controller {

    @Autowired
    private LocationLevel3Service level3Service;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // Asynchronous Save Level 3 method
    @PostMapping("/save")
    public CompletableFuture<ResponseEntity<Map<String, String>>> savelevel3(
            @RequestHeader HttpHeaders headers,
            @RequestBody Level3MasterBean bean,
            HttpServletRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                bean.setStatus(Constants.USER_STATUS_ACTIVE);
                bean.setDeleted(Constants.USER_NOT_DELETED);
                bean.setIpAddress(RequestUtils.getClientIpAddress(request));
                bean.setMacId(RequestUtils.getMacAddress(bean.getIpAddress()));

                Long count = level3Service.savelevel3Master(bean);
                Map<String, String> response = new HashMap<>();
                response.put("message", count == 1 ? "success" : "error");

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, String> response = new HashMap<>();
                response.put("message", "error");
                return ResponseEntity.ok(response);
            }
        },taskExecutor);
    }

    // Asynchronous Fetch All Level 3 List method
    @GetMapping("/fetch-all-level3s")
    public CompletableFuture<ResponseEntity<List<Level3MasterBean>>> fetchAlllevel3s() {
        return CompletableFuture.supplyAsync(() -> {
        	if(redisTemplate.opsForValue().get(RedisKey.LOCATION_LEVEL3_ALL.getKey())==null) {
            List<Level3MasterBean> level3s = level3Service.fetchAlllevel3s();
            redisTemplate.opsForValue().set(RedisKey.LOCATION_LEVEL3_ALL.getKey(), level3s);
            return ResponseEntity.ok(level3s);
        	}else {
        		List<Level3MasterBean> level3=(List<Level3MasterBean>)redisTemplate.opsForValue().get(RedisKey.LOCATION_LEVEL3_ALL.getKey());
        		return ResponseEntity.ok(level3);
        	}
        },taskExecutor);
    }

    // Asynchronous Pagination method for Level 3
    @PostMapping("/pagination")
    public CompletableFuture<ResponseEntity<PaginationResponse<Level3MasterBean>>> getAlllevel3sPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
        return CompletableFuture.supplyAsync(() -> {
        	if(redisTemplate.opsForValue().get(RedisKey.LOCATION_LEVEL3_PAGINATION.getKey(page,per_page))==null) {
            List<Level3MasterBean> level3List = level3Service.getAlllevel3sPagination(page, per_page);
            int totalRows = !level3List.isEmpty() ? level3List.get(0).getTotalRecords() : 0;

            PaginationResponse<Level3MasterBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(level3List);
            redisTemplate.opsForValue().set(RedisKey.LOCATION_LEVEL3_PAGINATION.getKey(page,per_page), response);
            return ResponseEntity.ok(response);
        	}else {
        		PaginationResponse<Level3MasterBean> locationLevel3=(PaginationResponse<Level3MasterBean>)redisTemplate.opsForValue().get(RedisKey.LOCATION_LEVEL3_PAGINATION.getKey(page,per_page));
        		return ResponseEntity.ok(locationLevel3);
        	}
        },taskExecutor);
    }

    // Asynchronous Delete Level 3 method
    @DeleteMapping("/delete/{id}")
    public CompletableFuture<ResponseEntity<Map<String, String>>> deletelevel3(@PathVariable("id") Integer level3Id) {
        return CompletableFuture.supplyAsync(() -> {
            Long count = level3Service.deletelevel3Id(level3Id);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
        },taskExecutor);
    }

    // Asynchronous Get Level 3 by ID method
    @GetMapping("/get-level3-byid/{id}")
    public CompletableFuture<ResponseEntity<Level3MasterBean>> getlevel3ById(@PathVariable("id") Integer level3Id) {
        return CompletableFuture.supplyAsync(() -> {
            Level3MasterBean bean = level3Service.getlevel3ById(level3Id);
            return ResponseEntity.ok(bean);
        },taskExecutor);
    }

    // Asynchronous Get Location Level 3 Child by Parent ID
    @GetMapping(value = "/get-location-levelchild-bylevel-parentid/{id}")
    public CompletableFuture<ResponseEntity<List<Level3MasterBean>>> getLocationLevel3ByLevel1(@PathVariable("id") Integer parentId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Level3MasterBean> list = level3Service.getLocationChildByParent(parentId);
            return ResponseEntity.ok(list);
        },taskExecutor);
    }
}
