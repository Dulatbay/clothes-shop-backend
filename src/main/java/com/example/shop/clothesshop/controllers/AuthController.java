package com.example.shop.clothesshop.controllers;


import com.example.shop.clothesshop.dtos.request.AuthRequest;
import com.example.shop.clothesshop.dtos.request.RegisterUserRequest;
import com.example.shop.clothesshop.dtos.response.AuthResponse;
import com.example.shop.clothesshop.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest){
        authService.registerUser(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest){
        AuthResponse authResponse = authService.authenticateUser(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }
}
