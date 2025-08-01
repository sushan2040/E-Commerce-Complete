package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<MenuTypeMasterBean>> getMenuTypeMasterList() {
    		 List<MenuTypeMasterBean> menuTypeList = menuTypeService.getMenuTypeMasterList();
    	        return ResponseEntity.ok(menuTypeList);
    }

    	        
    // ✅ Fix: Wrap plain String responses inside a JSON object
    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<Map<String, String>> saveMenuType(
            @RequestBody MenuTypeMasterBean bean, HttpServletRequest request) {
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
    }

    // ✅ Fix: Pagination for Menu Types
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<MenuTypeMasterBean>> getAllMenuTypePagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
    	List<MenuTypeMasterBean> menuTypeList = menuTypeService.getAllMenusPagination(page, per_page);
        int totalRows = !menuTypeList.isEmpty() ? menuTypeList.get(0).getTotalRecords() : 0;

        PaginationResponse<MenuTypeMasterBean> response = new PaginationResponse<>();
        response.setPage(page);
        response.setTotalPages(totalRows);
        response.setData(menuTypeList);
        return ResponseEntity.ok(response);
    }

    // ✅ Fix: Deleting Menu Type and wrapping response
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteMenuType(@PathVariable("id") Integer menuTypeId) {
    	Long count = menuTypeService.deleteMenuTypeId(menuTypeId);

        // Create response based on deletion result
        Map<String, String> response = new HashMap<>();
        response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);

        return ResponseEntity.ok(response);
    }

    // ✅ Fix: Returning a single MenuTypeMasterBean by ID
    @GetMapping("/get-menutype-byid/{id}")
    public ResponseEntity<MenuTypeMasterBean> getMenuTypeById(@PathVariable("id") Integer menuTypeId) {
        MenuTypeMasterBean bean = menuTypeService.getMenuTypeById(menuTypeId);
        return ResponseEntity.ok(bean);
    }
}
