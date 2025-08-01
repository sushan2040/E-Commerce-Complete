package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
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

    // ✅ Fix: Wrap String response in a JSON object
    @PostMapping(value="/save",consumes = "application/json",produces = "application/json")
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

    @GetMapping(value="/get-submodule-list")
    public ResponseEntity<List<SubModuleMasterBean>> getSubModuleList() {
        List<SubModuleMasterBean> beanList = subModuleService.getSubModuleList();
        return ResponseEntity.ok(beanList);
    }

    // ✅ Fix: Corrected pagination response structure
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<SubModuleMasterBean>> getAllSubmodulesPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int per_page) {
        List<SubModuleMasterBean> lookupDetails = subModuleService.getAllSubmodulesPagination(page, per_page);
        
        int totalRows = !lookupDetails.isEmpty() ? lookupDetails.get(0).getTotalRecords() : 0;
        int totalPages = (int) Math.ceil((double) totalRows / per_page);

        PaginationResponse<SubModuleMasterBean> response = new PaginationResponse<>();
        response.setPage(page);
        response.setTotalPages(totalPages);
        response.setData(lookupDetails);

        return ResponseEntity.ok(response);
    }

    // ✅ Fix: Wrap delete response in a JSON object
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteSubModule(@PathVariable("id") Integer subModuleId) {
        Long count = subModuleService.deleteSubModuleId(subModuleId);

        Map<String, String> response = new HashMap<>();
        response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-submodule-byid/{id}")
    public ResponseEntity<SubModuleMasterBean> getSubmoduleById(@PathVariable("id") Integer subModuleId) {
        SubModuleMasterBean bean = subModuleService.getSubmoduleById(subModuleId);
        return ResponseEntity.ok(bean);
    }
}
