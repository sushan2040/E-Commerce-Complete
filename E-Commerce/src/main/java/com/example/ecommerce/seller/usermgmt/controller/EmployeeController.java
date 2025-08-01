package com.example.ecommerce.seller.usermgmt.controller;

import java.util.List;
import java.util.concurrent.Callable;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.usermgmt.beans.EmployeeMasterBean;
import com.example.ecommerce.seller.usermgmt.service.EmployeeService;
import com.example.ecommerce.utils.RequestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api-data/employee-master")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    JwtService jwtService;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;
    
    @Autowired private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // Asynchronous Save method
    @PostMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthResponse> saveEmployeeMaster(
            @RequestBody EmployeeMasterBean bean,
            @RequestHeader HttpHeaders headers,
            HttpServletRequest request) {
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                AuthResponse authResponse = new AuthResponse();
                authResponse.setMessage(Constants.FAIL_MESSAGE);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
            }

            String ipAddress = RequestUtils.getClientIpAddress(request);
            bean.setIpAddress(ipAddress);
            bean.setMacId(RequestUtils.getMacAddress(ipAddress));
            bean.setPassword(passwordEncoder.encode(bean.getPassword()));
            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);

            try {
                Users parsedUser = new ObjectMapper().readValue(user, Users.class);
                bean.setBusinessId(parsedUser.getBusinessId());
                bean.setDeleted(Constants.NOT_DELETED);
                bean.setStatus(Constants.STATUS_ACTIVE);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // Save employee asynchronously
            Integer empId = employeeService.saveEmloyeeMaster(bean);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage(Constants.SUCCESS_MESSAGE);
            return ResponseEntity.ok(authResponse);
    }

    // Fetch Employees with Pagination (Asynchronous)
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<EmployeeMasterBean>> getAllEmployeesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page,
            @RequestHeader HttpHeaders headers) {
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = null;
			try {
				parsedUser = new ObjectMapper().readValue(user, Users.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            List<EmployeeMasterBean> employeeList = employeeService.getAllEmployeesPagination(page, per_page, parsedUser.getBusinessId());
            int totalRows = !employeeList.isEmpty() ? employeeList.get(0).getTotalRecords() : 0;
            PaginationResponse<EmployeeMasterBean> response = new PaginationResponse<>();
            response.setPage(page);
            response.setTotalPages(totalRows);
            response.setData(employeeList);
            return ResponseEntity.ok(response);
    }
}
