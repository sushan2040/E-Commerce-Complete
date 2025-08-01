package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
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

import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.service.CountryService;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/country")
public class CountryMasterController {

    @Autowired
    private CountryService countryService;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    
    private String REDIS_PAGINATION_COUNTRY="REDIS_PAGE_COUNTRY_";

    // Asynchronous Save method
    @PostMapping("/save")
    public CompletableFuture<ResponseEntity<Map<String, String>>> saveCountry(
            @RequestHeader HttpHeaders headers,
            @RequestBody CountryMasterBean bean,
            HttpServletRequest request) {
        return CompletableFuture.supplyAsync(() -> {
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
        },taskExecutor);
    }

    @GetMapping("/fetch-all-countries")
    public CompletableFuture<ResponseEntity<List<CountryMasterBean>>> fetchAllCountries() {
        return CompletableFuture.supplyAsync(() -> {
            String cacheKey = "countries:all";  // Redis cache key for the list of countries
            if(redisTemplate.opsForValue().get(RedisKey.COUNTRY_ALL.getKey())==null) {
            // If countries are not in cache, fetch them from the database
            List<CountryMasterBean> countries = countryService.fetchAllCountries();

            // Cache the fetched data in Redis for future use
            redisTemplate.opsForValue().set(RedisKey.COUNTRY_ALL.getKey(), countries);
            // Return the countries as a response
            return ResponseEntity.ok(countries);
            }else {
            	List<CountryMasterBean> countries=(List<CountryMasterBean>)redisTemplate.opsForValue().get(RedisKey.COUNTRY_ALL.getKey());
            	return ResponseEntity.ok(countries);
            }
        });
    }

    // Asynchronous Pagination method
    @PostMapping("/pagination")
    public CompletableFuture<ResponseEntity<PaginationResponse<CountryMasterBean>>> getAllCountriesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
        return CompletableFuture.supplyAsync(() ->{
        	if(redisTemplate.opsForValue().get(RedisKey.COUNTRY_PAGINATION.getKey(page,per_page))==null) {
            List<CountryMasterBean> countryList = countryService.getAllCountriesPagination(page, per_page);
            int totalRows = !countryList.isEmpty() ? countryList.get(0).getTotalRecords() : 0;

            PaginationResponse<CountryMasterBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(countryList);
            redisTemplate.opsForValue().set(RedisKey.COUNTRY_PAGINATION.getKey(page,per_page),response);
            return ResponseEntity.ok(response);
        	}else {
        		PaginationResponse<CountryMasterBean> paginationResponse=(PaginationResponse<CountryMasterBean>)redisTemplate.opsForValue().get(RedisKey.COUNTRY_PAGINATION.getKey(page,per_page));
        		return ResponseEntity.ok(paginationResponse);
        	}
        },taskExecutor);
    }

    // Asynchronous Delete method
    @DeleteMapping("/delete/{id}")
    public CompletableFuture<ResponseEntity<Map<String, String>>> deleteCountry(@PathVariable("id") Integer countryId) {
        return CompletableFuture.supplyAsync(() ->{
            Long count = countryService.deleteCountryId(countryId);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
        },taskExecutor);
    }

    // Asynchronous Get Country by ID method
    @GetMapping("/get-country-byid/{id}")
    public CompletableFuture<ResponseEntity<CountryMasterBean>> getCountryById(@PathVariable("id") Integer countryId) {
        return CompletableFuture.supplyAsync(() -> {
            CountryMasterBean bean = countryService.getCountryById(countryId);
            return ResponseEntity.ok(bean);
        },taskExecutor);
    }
}
