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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.configuration.beans.Level4MasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.LocationLevel4Service;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/location-level4")
public class LocationLevel4Controller {

    @Autowired
    private LocationLevel4Service level4Service;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // Asynchronous Save Level 4 method
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> savelevel4(
            @RequestHeader HttpHeaders headers,
            @RequestBody Level4MasterBean bean,
            HttpServletRequest request) {
            try {
                bean.setStatus(Constants.USER_STATUS_ACTIVE);
                bean.setDeleted(Constants.USER_NOT_DELETED);
                bean.setIpAddress(RequestUtils.getClientIpAddress(request));
                bean.setMacId(RequestUtils.getMacAddress(bean.getIpAddress()));

                Long count = level4Service.savelevel4Master(bean);
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

    // Asynchronous Fetch All Level 4 List method
    @GetMapping("/fetch-all-level4s")
    public ResponseEntity<List<Level4MasterBean>> fetchAlllevel4s() {
            List<Level4MasterBean> level4s = level4Service.fetchAlllevel4s();
            return ResponseEntity.ok(level4s);
    }

    // Asynchronous Pagination method for Level 4
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<Level4MasterBean>> getAlllevel4sPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
            List<Level4MasterBean> level4List = level4Service.getAlllevel4sPagination(page, per_page);
            int totalRows = !level4List.isEmpty() ? level4List.get(0).getTotalRecords() : 0;
            PaginationResponse<Level4MasterBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(level4List);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Delete Level 4 method
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deletelevel4(@PathVariable("id") Integer level4Id) {
            Long count = level4Service.deletelevel4Id(level4Id);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Get Level 4 by ID method
    @GetMapping("/get-level4-byid/{id}")
    public ResponseEntity<Level4MasterBean> getlevel4ById(@PathVariable("id") Integer level4Id) {
            Level4MasterBean bean = level4Service.getlevel4ById(level4Id);
            return ResponseEntity.ok(bean);
    }

    // Asynchronous Get Location Level 4 Child by Parent ID
    @GetMapping(value = "/get-location-levelchild-bylevel-parentid/{id}")
    public ResponseEntity<List<Level4MasterBean>> getLocationLevel4ByLevel1(@PathVariable("id") Integer parentId) {
            List<Level4MasterBean> list = level4Service.getLocationChildByParent(parentId);
            return ResponseEntity.ok(list);
    }
}
