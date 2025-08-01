package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.beans.MenuTypeMasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.MenuTypeService;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/menu-type")
public class MenuTypeMasterController {

    private final MenuTypeService menuTypeService;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    public MenuTypeMasterController(MenuTypeService menuTypeService) {
        this.menuTypeService = menuTypeService;
    }

    // ✅ Fix: Wrap List in ResponseEntity for proper JSON formatting
    @GetMapping(value = "/get-menu-type-list")
    public CompletableFuture<ResponseEntity<List<MenuTypeMasterBean>>> getMenuTypeMasterList() {
    	return CompletableFuture.supplyAsync(()->{
    		if(redisTemplate.opsForValue().get(RedisKey.MENU_TYPE_ALL.getKey())==null) {
    		 List<MenuTypeMasterBean> menuTypeList = menuTypeService.getMenuTypeMasterList();
    		 redisTemplate.opsForValue().set(RedisKey.MENU_TYPE_ALL.getKey(), menuTypeList);
    	        return ResponseEntity.ok(menuTypeList);
    		}else {
    			List<MenuTypeMasterBean> menuTypeList=(List<MenuTypeMasterBean>)redisTemplate.opsForValue().get(RedisKey.MENU_TYPE_ALL.getKey());
    			return ResponseEntity.ok(menuTypeList);
    		}
    	},taskExecutor);
    }

    // ✅ Fix: Wrap plain String responses inside a JSON object
    @PostMapping(value = "/save", consumes = "application/json")
    public CompletableFuture<ResponseEntity<Map<String, String>>> saveMenuType(
            @RequestBody MenuTypeMasterBean bean, HttpServletRequest request) {
       return CompletableFuture.supplyAsync(()->{ // Populate necessary fields before saving
        bean.setStatus(Constants.USER_STATUS_ACTIVE);
        bean.setDeleted(Constants.USER_NOT_DELETED);
        bean.setIpAddress(RequestUtils.getClientIpAddress(request));
        bean.setMacAdress(RequestUtils.getMacAddress(bean.getIpAddress()));

        // Save and check result
        Integer count = menuTypeService.saveMenuType(bean);

        // Build response
        Map<String, String> response = new HashMap<>();
        response.put("message", count > 0 ? Constants.SUCCESS_MESSAGE : Constants.FAIL_MESSAGE);

        return ResponseEntity.ok(response);
       },taskExecutor);
    }

    // ✅ Fix: Pagination for Menu Types
    @PostMapping("/pagination")
    public CompletableFuture<ResponseEntity<PaginationResponse<MenuTypeMasterBean>>> getAllMenuTypePagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
        return CompletableFuture.supplyAsync(()->{
        	if(redisTemplate.opsForValue().get(RedisKey.MENU_TYPE_PAGINATION.getKey(page,per_page))==null) {
    	List<MenuTypeMasterBean> menuTypeList = menuTypeService.getAllMenusPagination(page, per_page);
        int totalRows = !menuTypeList.isEmpty() ? menuTypeList.get(0).getTotalRecords() : 0;

        PaginationResponse<MenuTypeMasterBean> response = new PaginationResponse<>();
        response.setPage(page);
        response.setTotalPages(totalRows);
        response.setData(menuTypeList);
        redisTemplate.opsForValue().set(RedisKey.MENU_TYPE_PAGINATION.getKey(page,per_page), response);
        return ResponseEntity.ok(response);
        }else {
        	PaginationResponse<MenuTypeMasterBean> response=
        			(PaginationResponse<MenuTypeMasterBean>)redisTemplate.opsForValue().get(RedisKey.MENU_TYPE_PAGINATION.getKey(page,per_page));
        	return ResponseEntity.ok(response);
        }
        },taskExecutor);
    }

    // ✅ Fix: Deleting Menu Type and wrapping response
    @DeleteMapping("/delete/{id}")
    public CompletableFuture<ResponseEntity<Map<String, String>>> deleteMenuType(@PathVariable("id") Integer menuTypeId) {
       return CompletableFuture.supplyAsync(()->{
    	Long count = menuTypeService.deleteMenuTypeId(menuTypeId);

        // Create response based on deletion result
        Map<String, String> response = new HashMap<>();
        response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);

        return ResponseEntity.ok(response);
       },taskExecutor);
    }

    // ✅ Fix: Returning a single MenuTypeMasterBean by ID
    @GetMapping("/get-menutype-byid/{id}")
    public CompletableFuture<ResponseEntity<MenuTypeMasterBean>> getMenuTypeById(@PathVariable("id") Integer menuTypeId) {
    	return CompletableFuture.supplyAsync(()->{
        MenuTypeMasterBean bean = menuTypeService.getMenuTypeById(menuTypeId);
        return ResponseEntity.ok(bean);
    	},taskExecutor);
    }
}
