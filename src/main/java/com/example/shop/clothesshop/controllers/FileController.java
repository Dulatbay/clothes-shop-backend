package com.example.shop.clothesshop.controllers;

import com.example.shop.clothesshop.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @GetMapping(value = "/{filename}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody Resource getFileAsResourceByFilename(@PathVariable("filename") String filename) {
        log.info("Get Resource by name, name ={}", filename);
        return fileService.loadAsResource(filename);
    }
}

