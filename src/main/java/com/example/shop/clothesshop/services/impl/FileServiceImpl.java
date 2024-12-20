package com.example.shop.clothesshop.services.impl;

import com.example.shop.clothesshop.config.validators.ValidFile;
import com.example.shop.clothesshop.exceptions.StorageException;
import com.example.shop.clothesshop.services.FileService;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static com.example.shop.clothesshop.constants.ValueConstants.UPLOADED_FOLDER;


@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final Path rootLocation;

    public FileServiceImpl() {
        this.rootLocation = Paths.get(UPLOADED_FOLDER);
    }

    @Override
    public String save(@ValidFile MultipartFile file) {
        try {
            if (file.getOriginalFilename() == null || file.isEmpty()) {
                throw new StorageException(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Failed to store empty file.");
            }

            String filename = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());


            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(filename))
                    .normalize().toAbsolutePath();


            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Cannot store file outside current directory.");
            }

            @Cleanup
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);

            log.info("Save file name, {}", filename);
            return filename;
        } catch (IOException e) {
            log.error("IOException: ", e);
            throw new StorageException(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Failed to store file.");
        }
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "Could not read file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new StorageException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "Could not read file: " + fileName);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(this.rootLocation);
            log.info("Create directory: {}", rootLocation);
        } catch (IOException e) {
            log.error("Could not initialize storage.\nIOException: ", e);
        }
    }

    @Override
    public void deleteAll() {
        log.info("Deleted all File Service");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void deleteByFileName(String fileName) {
        try {
            Path file = load(fileName);
            if (Files.exists(file)) {
                Files.delete(file);
                log.info("Deleted file: {}", fileName);
            } else {
                log.warn("File not found: {}", fileName);
                throw new StorageException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "File not found: " + fileName);
            }
        } catch (IOException e) {
            log.error("IOException during file deletion: ", e);
            throw new StorageException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Failed to delete file: " + fileName);
        }
    }

}
