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

import com.example.ecommerce.configuration.beans.LocationLevelBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.LocationLevelService;
import com.example.ecommerce.constants.Constants;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/location-level")
public class LocationLevelController {

    @Autowired
    private LocationLevelService locationLevelService;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate; 
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    // Asynchronous Save Location Level
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveLocationLevel(
            @RequestHeader HttpHeaders headers,
            @RequestBody LocationLevelBean bean,
            HttpServletRequest request) {
            try {
                bean.setStatus(Constants.USER_STATUS_ACTIVE);
                bean.setDeleted(Constants.USER_NOT_DELETED);

                Long count = locationLevelService.saveLocationLevelMaster(bean);
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

    // Asynchronous Fetch All Location Levels
    @GetMapping("/fetch-all-location-levels")
    public ResponseEntity<List<LocationLevelBean>> fetchAllLocationLevels() {
            List<LocationLevelBean> locationLevels = locationLevelService.fetchAllLocationLevels();
            return ResponseEntity.ok(locationLevels);
    }

    // Asynchronous Pagination for Location Levels
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<LocationLevelBean>> getAllLocationLevelsPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
            List<LocationLevelBean> locationLevelsList = locationLevelService.getAllLocationLevelsPagination(page, per_page);
            int totalRows = !locationLevelsList.isEmpty() ? locationLevelsList.get(0).getTotalRecords() : 0;

            PaginationResponse<LocationLevelBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(locationLevelsList);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Delete Location Level
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteLocationLevel(@PathVariable("id") Integer locationLevelId) {
            Long count = locationLevelService.deletelocationLevelId(locationLevelId);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Get Location Level by ID
    @GetMapping("/get-location-level-byid/{id}")
    public ResponseEntity<LocationLevelBean> getLocationLevelById(@PathVariable("id") Integer locationLevelId) {
            LocationLevelBean bean = locationLevelService.getLocationLevelById(locationLevelId);
            return ResponseEntity.ok(bean);
    }

    // Asynchronous Get Location Levels by Country ID
    @PostMapping("/get-location-levels-bycountryid/{id}")
    public ResponseEntity<Integer> getLocationLevelsByCountryId(@PathVariable("id") Integer countryId) {
            Integer locationLevels = locationLevelService.getLocationLevelsByCountryId(countryId);
            return ResponseEntity.ok(locationLevels);
    }
}
