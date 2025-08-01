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

import com.example.ecommerce.configuration.beans.Level2MasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.LocationLevel2Service;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/location-level2")
public class LocationLevel2Controller {

    @Autowired
    private LocationLevel2Service level2Service;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // Asynchronous Save Level 2 method
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> savelevel2(
            @RequestHeader HttpHeaders headers,
            @RequestBody Level2MasterBean bean,
            HttpServletRequest request) {
            try {
                bean.setStatus(Constants.USER_STATUS_ACTIVE);
                bean.setDeleted(Constants.USER_NOT_DELETED);
                bean.setIpAddress(RequestUtils.getClientIpAddress(request));
                bean.setMacId(RequestUtils.getMacAddress(bean.getIpAddress()));

                Long count = level2Service.savelevel2Master(bean);
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

    // Asynchronous Fetch All Level 2 List method
    @GetMapping("/fetch-all-level2s")
    public ResponseEntity<List<Level2MasterBean>> fetchAlllevel2s() {
            List<Level2MasterBean> level2s = level2Service.fetchAlllevel2s();
            return ResponseEntity.ok(level2s);
    }

    // Asynchronous Pagination method for Level 2
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<Level2MasterBean>> getAlllevel2sPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
            List<Level2MasterBean> level2List = level2Service.getAlllevel2sPagination(page, per_page);
            int totalRows = !level2List.isEmpty() ? level2List.get(0).getTotalRecords() : 0;

            PaginationResponse<Level2MasterBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(level2List);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Delete Level 2 method
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deletelevel2(@PathVariable("id") Integer level2Id) {
            Long count = level2Service.deletelevel2Id(level2Id);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Get Level 2 by ID method
    @GetMapping("/get-level2-byid/{id}")
    public ResponseEntity<Level2MasterBean> getlevel2ById(@PathVariable("id") Integer level2Id) {
            Level2MasterBean bean = level2Service.getlevel2ById(level2Id);
            return ResponseEntity.ok(bean);
    }

    // Asynchronous Get Location Level Child by Parent ID
    @GetMapping(value = "/get-location-levelchild-bylevel-parentid/{id}")
    public ResponseEntity<List<Level2MasterBean>> getLocationLevel2ByLevel1(@PathVariable("id") Integer parentId) {
            List<Level2MasterBean> list = level2Service.getLocationChildByParent(parentId);
            return ResponseEntity.ok(list);
    }
}
