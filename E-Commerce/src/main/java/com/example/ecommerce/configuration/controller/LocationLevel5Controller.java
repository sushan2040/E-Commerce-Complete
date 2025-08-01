package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.beans.Level5MasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.LocationLevel5Service;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/location-level5")
public class LocationLevel5Controller {

    @Autowired
    private LocationLevel5Service level5Service;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    // Asynchronous Save Level 5 method
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> savelevel5(
            @RequestHeader HttpHeaders headers,
            @RequestBody Level5MasterBean bean,
            HttpServletRequest request) {
            try {
                bean.setStatus(Constants.USER_STATUS_ACTIVE);
                bean.setDeleted(Constants.USER_NOT_DELETED);
                bean.setIpAddress(RequestUtils.getClientIpAddress(request));
                bean.setMacId(RequestUtils.getMacAddress(bean.getIpAddress()));

                Long count = level5Service.savelevel5Master(bean);
                Map<String, String> response = new HashMap<>();
                response.put("message", count == 1 ? "success" : "error");

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, String> response = new HashMap<>();
                response.put("message", "error");
                return ResponseEntity.ok(response);
            }
    }

    // Asynchronous Fetch All Level 5 List method
    @GetMapping("/fetch-all-level5s")
    public ResponseEntity<List<Level5MasterBean>> fetchAlllevel5s() {
            List<Level5MasterBean> level5s = level5Service.fetchAlllevel5s();
            return ResponseEntity.ok(level5s);
    }

    // Asynchronous Pagination method for Level 5
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<Level5MasterBean>> getAlllevel5sPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
            List<Level5MasterBean> level5List = level5Service.getAlllevel5sPagination(page, per_page);
            int totalRows = !level5List.isEmpty() ? level5List.get(0).getTotalRecords() : 0;

            PaginationResponse<Level5MasterBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(level5List);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Delete Level 5 method
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deletelevel5(@PathVariable("id") Integer level5Id) {
            Long count = level5Service.deletelevel5Id(level5Id);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Get Level 5 by ID method
    @GetMapping("/get-level5-byid/{id}")
    public ResponseEntity<Level5MasterBean> getlevel5ById(@PathVariable("id") Integer level5Id) {
            Level5MasterBean bean = level5Service.getlevel5ById(level5Id);
            return ResponseEntity.ok(bean);
    }

    // Asynchronous Get Location Level 5 Child by Parent ID method
    @GetMapping(value = "/get-location-levelchild-bylevel-parentid/{id}")
    public ResponseEntity<List<Level5MasterBean>> getLocationLevel5ByLevel4(@PathVariable("id") Integer parentId) {
            List<Level5MasterBean> list = level5Service.getLocationChildByParent(parentId);
            return ResponseEntity.ok(list);
    }
}
