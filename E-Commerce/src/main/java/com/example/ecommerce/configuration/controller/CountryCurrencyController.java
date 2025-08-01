package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.configuration.beans.CountryCurrencyBean;
import com.example.ecommerce.configuration.beans.Level2MasterBean;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.service.CountryCurrencyService;
import com.example.ecommerce.constants.Constants;

@Controller
@RequestMapping(value = "/api-data/country-currency")
public class CountryCurrencyController {

	@Autowired
	CountryCurrencyService countryCurrencyService;
	
	@Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
	
	@PostMapping(value = "/save")
	public ResponseEntity<AuthResponse> saveCountryCurrencyMaster(@RequestBody CountryCurrencyBean bean){
		
		bean.setDeleted(Constants.NOT_DELETED);
		bean.setStatus(Constants.STATUS_ACTIVE);
		Long countryCurrencyId=countryCurrencyService.saveCountryCurrencyMaster(bean);
		AuthResponse authResponse=new AuthResponse();
		authResponse.setMessage(Constants.SUCCESS_MESSAGE);
		authResponse.setStatus("success");
		return ResponseEntity.ok(authResponse);
	}
	@PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<CountryCurrencyBean>> getAllCountryCurrencyPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page) {
            List<CountryCurrencyBean> currencyList = countryCurrencyService.getAllCountryCurrencyPagination(page, per_page);
            int totalRows = !currencyList.isEmpty() ? currencyList.get(0).getTotalRecords() : 0;

            PaginationResponse<CountryCurrencyBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(currencyList);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Delete Level 2 method
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deletelevel2(@PathVariable("id") Integer countryCurrencyMasterId) {
            Long count = countryCurrencyService.deleteCountryCurrencyId(countryCurrencyMasterId);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Get Level 2 by ID method
    @GetMapping("/get-country-currency-byid/{id}")
    public ResponseEntity<CountryCurrencyBean> getlevel2ById(@PathVariable("id") Integer countryCurrencyMasterId) {
        	CountryCurrencyBean bean = countryCurrencyService.getCountryCurrencyById(countryCurrencyMasterId);
            return ResponseEntity.ok(bean);
    }
    @PostMapping(value = "/check-combination")
    public ResponseEntity<Long> checkCombination(@RequestParam("countryId") Integer countryId){
    	Long count=countryCurrencyService.checkCombination(countryId);
    	return ResponseEntity.ok(count);
    }
	
}
