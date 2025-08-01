package com.example.ecommerce.configuration.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.configuration.service.UsersService;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.usermgmt.beans.EmployeeMasterBean;
import com.example.ecommerce.seller.usermgmt.service.EmployeeService;
import com.example.ecommerce.utils.RequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-data/auth")
public class AuthController {

    @Autowired private UsersService userRepo;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RedisTemplate<String,Object> redisTemplate;
    @Autowired private EmployeeService employeeService;
    
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @PostMapping(value = "/register", produces = "application/json", consumes = "application/json")
    public Callable<ResponseEntity<AuthResponse>> registerHandler(@RequestBody Users user, HttpServletRequest request) {
        return () -> {
            try {
                String encodedPass = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPass);
                user.setDeleted(Constants.USER_NOT_DELETED);
                user.setStatus(Constants.USER_STATUS_ACTIVE);
                user.setIpAddress(RequestUtils.getClientIpAddress(request));
                user.setMacAddress(RequestUtils.getMacAddress(RequestUtils.getClientIpAddress(request)));
                user.setCreatedDate(new Date());
                user.setUpdatedDate(new Date());
                user.setIsUserSeller(Constants.USER_NOT_SELLER);
                user.setIsAdmin(Constants.IS_NOT_ADMIN);
                Users savedUser = userRepo.save(user);
                String token = jwtService.generateToken(savedUser);

                AuthResponse response = new AuthResponse();
                response.setToken(token);
                response.setStatus("success");
                response.setMessage("Registration successful");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse());
            }
        };
    }

    @PostMapping(value = "/seller-register")
    public Callable<ResponseEntity<AuthResponse>> sellerRegisterHandler(@RequestBody Users user, HttpServletRequest request) {
        return () -> {
            try {
                // Encrypt password
                String encodedPass = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPass);

                // Set other fields
                user.setDeleted(Constants.USER_NOT_DELETED);
                user.setStatus(Constants.USER_STATUS_ACTIVE);
                user.setIpAddress(RequestUtils.getClientIpAddress(request));
                user.setMacAddress(RequestUtils.getMacAddress(RequestUtils.getClientIpAddress(request)));
                user.setCreatedDate(new Date());
                user.setUpdatedDate(new Date());
                user.setIsUserSeller(Constants.USER_SELLER);
                user.setIsAdmin(Constants.IS_NOT_ADMIN);
                // Save the user
                Users savedUser = userRepo.save(user);

                // Generate JWT token
                String token = jwtService.generateToken(savedUser);

                // Create response object
                AuthResponse response = new AuthResponse();
                response.setToken(token);
                response.setMessage("Seller registered successfully");
                response.setStatus("success");

                // Return success response with 200 status
                return ResponseEntity.status(HttpStatus.OK).body(response);

            } catch (Exception e) {
                // Log the exception for better debugging
                e.printStackTrace();

                // Create error response
                AuthResponse response = new AuthResponse();
                response.setMessage("Error during seller registration");
                response.setStatus("error");

                // Return error response with 500 status
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        };
    }

    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public Callable<ResponseEntity<AuthResponse>> loginHandler(@RequestBody Users body) {
        return () -> {
            try {
                // Authenticate user credentials
                Authentication authentication = authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword(), List.of(new SimpleGrantedAuthority("USER")))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Fetch user details from DB
                Users savedUser = userRepo.findByEmail(body.getEmail());
                if (savedUser == null) {
                    throw new BadCredentialsException("User not found");
                }

                // Generate JWT token
                String token = jwtService.generateToken(savedUser);

                // Create response object
                AuthResponse response = new AuthResponse();
                response.setToken(token);
                response.setMessage("Login successful");

                return ResponseEntity.ok(response);

            } catch (BadCredentialsException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, "Invalid Credentials"));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse(null, "Login Failed"));
            }
        };
    }
    
    @PostMapping(value = "/employee/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AuthResponse> employeeLoginHandler(@RequestBody Users body) {
            try {
            	// Authenticate user credentials
                Authentication authentication = authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword(), List.of(new SimpleGrantedAuthority("USER")))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Fetch user details from DB
               
            	// Fetch user details from DB
                Users savedUser = userRepo.findByEmail(body.getEmail());
                if (savedUser == null) {
                    throw new BadCredentialsException("User not found");
                }

                // Generate JWT token
                String token = jwtService.generateToken(savedUser);

                // Create response object
                AuthResponse response = new AuthResponse();
                response.setToken(token);
                response.setMessage("Login successful");

                return ResponseEntity.ok(response);

            } catch (BadCredentialsException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, "Invalid Credentials"));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse(null, "Login Failed"));
            }
    }

    @PostMapping(value = "/get-user-access")
    public ResponseEntity<List<SubModuleMasterBean>> getUserAccess(@RequestHeader HttpHeaders headers) {
        	Long startTime=System.currentTimeMillis();
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.emptyList());
            }
            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            List<SubModuleMasterBean> subModuleList = null;
            try {
                String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
                Users parsedUser = new ObjectMapper().readValue(user, Users.class);
                	if(parsedUser.getIsAdmin()!=null && parsedUser.getIsAdmin().equals(Constants.IS_NOT_ADMIN) && parsedUser.getIsUserSeller()!=null && parsedUser.getIsUserSeller().equals(Constants.USER_NOT_SELLER)) {
                		if(parsedUser.getBusinessId()==null) {
                			subModuleList=userRepo.getUserAccess(parsedUser);
                		}else {
                		subModuleList=userRepo.getEmployeeAccess(parsedUser);
                		}
                	}else {
                	subModuleList = userRepo.getUserAccess(parsedUser);
                	}
                    List<SubModuleMasterBean> mainList = new ArrayList<>();
                    List<SubModuleMasterBean> childSubModules;
                    for (SubModuleMasterBean bean : subModuleList) {
                        SubModuleMasterBean bean2 = new SubModuleMasterBean();
                        childSubModules = new ArrayList<>();
                        for (SubModuleMasterBean subModule : subModuleList) {
                            if (bean.getSubModuleId() == subModule.getParentId()) {
                                childSubModules.add(subModule);
                            }
                        }
                        bean2.setSubModuleName(bean.getSubModuleName());
                        bean2.setSubModuleId(bean.getSubModuleId());
                        bean2.setIcon(bean.getIcon());
                        bean2.setSubModule(childSubModules);
                        if (!bean2.getSubModule().isEmpty()) {
                            mainList.add(bean2);
                        }
                    Long endTime=System.currentTimeMillis();
                    
                    }
                    if(!mainList.isEmpty()) {
                    	return ResponseEntity.ok(mainList);
                    }else {
                    	return ResponseEntity.ok(subModuleList);
                    }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
            }
        
    }
    
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(@RequestHeader HttpHeaders headers){
    	  String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
    	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("");
        }
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
        }catch(Exception e) {
        	e.printStackTrace();
        }
    	return ResponseEntity.ok("logout");
    }
}
