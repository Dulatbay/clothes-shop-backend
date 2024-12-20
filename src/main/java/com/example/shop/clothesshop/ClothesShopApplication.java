package com.example.shop.clothesshop;

import com.example.shop.clothesshop.services.FileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClothesShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClothesShopApplication.class, args);
    }


    @Bean
    public CommandLineRunner CommandLineRunnerBean(FileService fileService) {
        return (args) -> {
            fileService.init();
        };
    }
}
