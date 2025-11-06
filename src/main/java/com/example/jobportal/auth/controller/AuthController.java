package com.example.jobportal.auth.controller;

import com.example.jobportal.auth.dto.request.LoginRequest;
import com.example.jobportal.auth.dto.response.LoginResponse;
import com.example.jobportal.auth.dto.request.RegisterRequest;
import com.example.jobportal.auth.dto.response.RegisterResponse;
import com.example.jobportal.auth.service.AuthService;
import com.example.jobportal.auth.service.JobPortalUserPrincipal;
import com.example.jobportal.user.repository.UserRepository;
import com.example.jobportal.util.Jwtutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private Jwtutil jwtutil;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        RegisterResponse registerResponse = RegisterResponse.builder()
                .email(registerRequest.getEmail())
                .message("Account has been created successfully").build();
        return new ResponseEntity<>(registerResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        LoginResponse loginResponse = LoginResponse.builder()
                .message("User logged in successfully")
                .token(token).build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Map> getCurrentUser(@AuthenticationPrincipal JobPortalUserPrincipal principal){
        if (principal == null) throw new AccessDeniedException("Invalid token");

        Map<String, String> claims = authService.getDetailsOfUser(principal);
        return new ResponseEntity<>(claims, HttpStatus.OK);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@AuthenticationPrincipal JobPortalUserPrincipal principal) {
        if (principal == null) throw new AccessDeniedException("Invalid token");

        String refreshedToken = jwtutil.generateToken(principal.getUsername());
        LoginResponse loginResponse = LoginResponse.builder()
                .token(refreshedToken).build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
