package com.example.ecommerce.configuration.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.SubModuleService;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/submodule")
public class SubModuleController {

    private final SubModuleService subModuleService;

    @Autowired
    public SubModuleController(SubModuleService subModuleService) {
        this.subModuleService = subModuleService;
    }
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // Asynchronous Save method
    @PostMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, String>> saveSubModule(
            @RequestBody SubModuleMasterBean bean, HttpServletRequest request) {
            bean.setDeleted(Constants.NOT_DELETED);
            bean.setStatus(Constants.STATUS_ACTIVE);
            bean.setIpAddress(RequestUtils.getClientIpAddress(request));
            bean.setMacId(RequestUtils.getMacAddress(bean.getIpAddress()));
            Integer count = subModuleService.saveSubModule(bean);

            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? "success" : "error");

            return ResponseEntity.ok(response);
    }

    // Asynchronous Get Submodule List method
    @GetMapping(value = "/get-submodule-list")
    public ResponseEntity<List<SubModuleMasterBean>> getSubModuleList() {
            List<SubModuleMasterBean> beanList = subModuleService.getSubModuleList();
            return ResponseEntity.ok(beanList);
    }

    // Asynchronous Pagination method
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<SubModuleMasterBean>> getAllSubmodulesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {

            List<SubModuleMasterBean> lookupDetailsFuture = subModuleService.getAllSubmodulesPagination(page, per_page);

            List<SubModuleMasterBean> lookupDetails = lookupDetailsFuture;  // Blocking until the result is available

			int totalRows = !lookupDetails.isEmpty() ? lookupDetails.get(0).getTotalRecords() : 0;
			//int totalPages = (int) Math.ceil((double) totalRows / per_page);

			PaginationResponse<SubModuleMasterBean> response = new PaginationResponse<>();
			response.setPage(page);
			response.setTotalPages(totalRows); // Corrected totalPages calculation
			response.setData(lookupDetails);
			return ResponseEntity.ok(response);
    }

    // Asynchronous Delete method
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteSubModule(@PathVariable("id") Integer subModuleId) {
            Long count = subModuleService.deleteSubModuleId(subModuleId);

            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);

            return ResponseEntity.ok(response);
    }

    // Asynchronous Get Submodule by ID method
    @GetMapping("/get-submodule-byid/{id}")
    public ResponseEntity<SubModuleMasterBean> getSubmoduleById(@PathVariable("id") Integer subModuleId) {
            SubModuleMasterBean bean = subModuleService.getSubmoduleById(subModuleId);
            return ResponseEntity.ok(bean);
    }
}
