package com.example.shop.clothesshop.services;


import com.example.shop.clothesshop.dtos.request.AuthRequest;
import com.example.shop.clothesshop.dtos.request.RegisterUserRequest;
import com.example.shop.clothesshop.dtos.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    void registerUser(RegisterUserRequest user);

    AuthResponse authenticateUser(AuthRequest auth);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
