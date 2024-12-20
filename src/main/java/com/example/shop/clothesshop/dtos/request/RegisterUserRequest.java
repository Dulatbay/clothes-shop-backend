package com.example.shop.clothesshop.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequest {

    @NotNull
    @JsonProperty("username")
    private String username;

    @Size(min = 5, message = "Password should have minimum 5 characters")
    @NotNull
    @JsonProperty("password")
    private String password;

    @Size(min = 5, message = "Password should have minimum 5 characters")
    @NotNull
    @JsonProperty("confirm_password")
    private String confirmPassword;

    @NotNull
    @JsonProperty("email")
    private String email;
}
