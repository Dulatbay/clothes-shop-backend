package com.example.shop.clothesshop.constants;

import com.example.shop.clothesshop.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) return null;

        return (User) authentication.getPrincipal();
    }
}
