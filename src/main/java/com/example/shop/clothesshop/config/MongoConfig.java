package com.example.shop.clothesshop.config;

import com.example.shop.clothesshop.constants.Utils;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAware<>() {
            @Override
            @NonNull
            public Optional<String> getCurrentAuditor() {
                var user = Utils.getCurrentUser();

                if (user == null)
                    return Optional.empty();

                return Optional.of(user.getId());
            }
        };
    }
}
