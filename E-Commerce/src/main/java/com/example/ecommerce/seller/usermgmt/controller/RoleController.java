package com.example.ecommerce.seller.usermgmt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.usermgmt.beans.RoleMasterBean;
import com.example.ecommerce.seller.usermgmt.service.RoleMasterService;
import com.example.ecommerce.utils.RequestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api-data/role-master")
public class RoleController {

    private final RoleMasterService roleMasterService;
    private final JwtService jwtService;

    @Autowired
    public RoleController(RoleMasterService roleMasterService, JwtService jwtService) {
        this.roleMasterService = roleMasterService;
        this.jwtService = jwtService;
    }
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    // Asynchronous Save Role method
    @PostMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthResponse> saveRoleMaster(
            @RequestBody RoleMasterBean bean,
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

            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);

            try {
                Users parsedUser = new ObjectMapper().readValue(user, Users.class);
                bean.setBusinessId(parsedUser.getBusinessId());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse());
            }

            // Save role master asynchronously
            Integer roleId = roleMasterService.saveRoleMaster(bean);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage(Constants.SUCCESS_MESSAGE);
            return ResponseEntity.ok(authResponse);
        
    }

    // Asynchronous Get All Roles method with Pagination
    @PostMapping("/pagination")
    public ResponseEntity<PaginationResponse<RoleMasterBean>> getAllRolesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int per_page,
            @RequestHeader HttpHeaders headers) {
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser;
            try {
            	parsedUser = new ObjectMapper().readValue(user, Users.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
                List<RoleMasterBean> roleList = roleMasterService.getAllRolesPagination(page, per_page, parsedUser.getBusinessId());

                int totalRows = !roleList.isEmpty() ? roleList.get(0).getTotalRecords() : 0;
                PaginationResponse<RoleMasterBean> response = new PaginationResponse<>();
                response.setPage(page);
                response.setTotalPages(totalRows);
                response.setData(roleList);
                return ResponseEntity.ok(response);
    }

    // Asynchronous Delete Role method
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteRoleMaster(@PathVariable("id") Integer roleId) {
            Long count = roleMasterService.deleteRoleId(roleId);
            Map<String, String> response = new HashMap<>();
            response.put("message", count > 0 ? Constants.DELETE_SUCCESS_MESSAGE : Constants.DELETE_FAIL_MESSAGE);
            return ResponseEntity.ok(response);
    }

    // Asynchronous Get Role by ID method
    @GetMapping("/get-role-master-byid/{id}")
    public ResponseEntity<RoleMasterBean> getRoleById(@PathVariable("id") Integer roleId) {
            RoleMasterBean bean = roleMasterService.getRoleById(roleId);
            return ResponseEntity.ok(bean);
    }

    // Asynchronous Get All Roles method (No pagination)
    @GetMapping("/get-roles")
    public ResponseEntity<List<RoleMasterBean>> getRoles(@RequestHeader HttpHeaders headers) {
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
           

            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);

            try {
                Users parsedUser = new ObjectMapper().readValue(user, Users.class);
                	 List<RoleMasterBean> roleList = roleMasterService.getRoles(parsedUser.getBusinessId());
                	return ResponseEntity.ok(roleList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
    }
}
