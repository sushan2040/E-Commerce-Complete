package com.example.ecommerce.configuration.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.service.CountryService;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/country", produces = "application/json", consumes = "application/json")
public class CountryMasterController {
    
    @Autowired
    private CountryService countryService;

    // ✅ Fix: Return JSON instead of plain string
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveCountry(
            @RequestHeader HttpHeaders headers,
            @RequestBody CountryMasterBean bean,
            HttpServletRequest request) {
        try {
            bean.setStatus(Constants.USER_STATUS_ACTIVE);
            bean.setDeleted(Constants.USER_NOT_DELETED);
            bean.setIpAddress(RequestUtils.getClientIpAddress(request));
            bean.setMacId(RequestUtils.getMacAddress(bean.getIpAddress()));

            Long count = countryService.saveCountryMaster(bean);
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

    // ✅ Fix: Wrap List in ResponseEntity
    @GetMapping("/fetch-all-countries")
    public ResponseEntity<List<CountryMasterBean>> fetchAllCountries() {
        List<CountryMasterBean> countries = countryService.fetchAllCountries();
        return ResponseEntity.ok(countries);
    }

    // ✅ Fix: Ensure PaginationResponse DTO is returned
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<CountryMasterBean>> getAllCountriesPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int per_page) {
        List<CountryMasterBean> countryList = countryService.getAllCountriesPagination(page, per_page);
        int totalRows = !countryList.isEmpty() ? countryList.get(0).getTotalRecords() : 0;

        PaginationResponse<CountryMasterBean> response = new PaginationResponse<>();
        response.setPage(page);
        response.setTotalPages((int) Math.ceil((double) totalRows / per_page));
        response.setData(countryList);

        return ResponseEntity.ok(response);
    }

    // ✅ Fix: Return JSON Object instead of plain String
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteCountry(@PathVariable("id") Integer countryId) {
        Long count = countryService.deleteCountryId(countryId);
        Map<String, String> response = new HashMap<>();
        response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
        return ResponseEntity.ok(response);
    }

    // ✅ Fix: Ensure proper JSON response
    @GetMapping("/get-country-byid/{id}")
    public ResponseEntity<CountryMasterBean> getCountryById(@PathVariable("id") Integer countryId) {
        CountryMasterBean bean = countryService.getCountryById(countryId);
        return ResponseEntity.ok(bean);
    }
}
