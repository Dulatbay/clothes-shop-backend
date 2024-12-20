package com.example.shop.clothesshop.services.impl;


import com.example.shop.clothesshop.dtos.request.AuthRequest;
import com.example.shop.clothesshop.dtos.request.RegisterUserRequest;
import com.example.shop.clothesshop.dtos.response.AuthResponse;
import com.example.shop.clothesshop.entities.Token;
import com.example.shop.clothesshop.entities.User;
import com.example.shop.clothesshop.entities.enums.TokenType;
import com.example.shop.clothesshop.exceptions.DbNotFoundException;
import com.example.shop.clothesshop.repository.TokenRepository;
import com.example.shop.clothesshop.repository.UserRepository;
import com.example.shop.clothesshop.security.JwtService;
import com.example.shop.clothesshop.security.Role;
import com.example.shop.clothesshop.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public void registerUser(RegisterUserRequest registerUserRequest) {
        userRepository.findByEmail(registerUserRequest.getEmail().trim())
                .ifPresent(usr -> {
                    throw new IllegalArgumentException("User with email " + registerUserRequest.getEmail() + " already exists");
                });

        if (!registerUserRequest.getPassword().trim().equals(registerUserRequest.getConfirmPassword().trim())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = new User();
        user.setEmail(registerUserRequest.getEmail());
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setRole(Role.USER);

        log.info("Registered user: {}", user);
        userRepository.save(user);
    }

    @Override
    public AuthResponse authenticateUser(AuthRequest authRequest) {
        Optional<User> userOptional = userRepository.findByEmail(authRequest.getEmailOrUsername())
                .or(() -> userRepository.findByUsername(authRequest.getEmailOrUsername()));

        if(userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid email/username or password");
        }

        var user = userOptional.get();

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token is empty");
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {

            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new DbNotFoundException(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Token is invalid"));


            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);

            } else {
                throw new IllegalArgumentException("Token is invalid");
            }
        } else
            throw new IllegalArgumentException("Token is empty");
    }
}




