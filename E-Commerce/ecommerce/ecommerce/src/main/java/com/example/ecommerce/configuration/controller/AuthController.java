package com.example.ecommerce.configuration.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.configuration.beans.AuthResponse;
import com.example.ecommerce.configuration.beans.SubModuleMasterBean;
import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.configuration.service.UsersService;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.utils.RequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UsersService userRepo;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AuthResponse> registerHandler(@RequestBody Users user, HttpServletRequest request) {
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
            Users savedUser = userRepo.save(user);
            String token = jwtService.generateToken(savedUser);

            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setMessage("Registration successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new AuthResponse());
        }
    }

    @PostMapping(value = "/seller-register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AuthResponse> sellerRegisterHandler(@RequestBody Users user, HttpServletRequest request) {
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
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log the exception for better debugging (replace e.printStackTrace() with proper logging)
            e.printStackTrace();
            
            // Create error response
            AuthResponse response = new AuthResponse();
            response.setMessage("Error during seller registration");
            response.setStatus("error");

            // Return error response with 500 status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody Users body) {
        try {
            // Authenticate user credentials
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword(),List.of(new SimpleGrantedAuthority("USER")))
            );
           // authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Fetch user details from DB
            Users savedUser = userRepo.findByEmail(body.getEmail());
            if (savedUser == null) {
                throw new UsernameNotFoundException("User not found");
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
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.emptyList());
        }
        boolean isAuthenticated=SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        System.out.println("isAuthenticated:"+isAuthenticated);
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
        List<SubModuleMasterBean> subModuleList = null;
        try {
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
            subModuleList = userRepo.getUserAccess(parsedUser);
            List<SubModuleMasterBean> childSubModules;
            for (SubModuleMasterBean bean : subModuleList) {
                childSubModules = new ArrayList<>();
                for (SubModuleMasterBean subModule : subModuleList) {
                    if (bean.getSubModuleId() == subModule.getParentId()) {
                        childSubModules.add(subModule);
                    }
                }
                bean.setSubModule(childSubModules);
            }
            return ResponseEntity.ok(subModuleList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }
    }



}