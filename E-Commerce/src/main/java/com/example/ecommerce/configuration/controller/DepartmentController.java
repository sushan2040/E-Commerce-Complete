package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.beans.DepartmentBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.DepartmentService;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/api-data/department-master")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    // Asynchronous Get Department Master List
    @GetMapping(value="/get-department-master-list")
    public ResponseEntity<List<DepartmentBean>> getDepartmentMasterList() {
            List<DepartmentBean> departmentMasterList = departmentService.getDepartmentMasterList();
        	return ResponseEntity.ok(departmentMasterList);
    }

    // Asynchronous Save Department Master
    @PostMapping(value="/save", consumes = "application/json")
    public ResponseEntity<Map<String, String>> saveDepartmentMaster(
            @RequestBody DepartmentBean bean, HttpServletRequest request) {
            bean.setStatus(Constants.USER_STATUS_ACTIVE);
            bean.setDeleted(Constants.USER_NOT_DELETED);
            bean.setIpAddress(RequestUtils.getClientIpAddress(request));
            bean.setMacId(RequestUtils.getMacAddress(bean.getIpAddress()));
            Integer count = departmentService.saveDepartmentMaster(bean);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.SUCCESS_MESSAGE : Constants.FAIL_MESSAGE);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Pagination method
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<DepartmentBean>> getAllDepartmentMasterPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
            List<DepartmentBean> departmentList = departmentService.getAllDepartmentsPagination(page, per_page);
            int totalRows = !departmentList.isEmpty() ? departmentList.get(0).getTotalRecords() : 0;
            PaginationResponse<DepartmentBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(departmentList);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Delete Department
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteDepartmentMaster(@PathVariable("id") Integer departmentId) {
            Long count = departmentService.deleteDepartmentMasterId(departmentId);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Get Department by ID
    @GetMapping("/get-department-byid/{id}")
    public ResponseEntity<DepartmentBean> getDepartmentMasterById(@PathVariable("id") Integer departmentId) {
            DepartmentBean bean = departmentService.getDepartmentMasterById(departmentId);
            return ResponseEntity.ok(bean);
    }
}
