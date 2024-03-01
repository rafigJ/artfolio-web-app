package ru.vsu.cs.artfolio.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.service.PostService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final PostService service;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("images") List<MultipartFile> listFiles) {
        listFiles.forEach(f -> LOGGER.info("upload file {}", f.getOriginalFilename()));
        try {
            service.uploadImages(listFiles);
            return ResponseEntity.ok("Загрузились");
        } catch (IOException e) {
            throw new RestException("Server error " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
        LOGGER.info("download file {}", fileName);
        try {
            byte[] imageData = service.downloadImage(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        } catch (IOException e) {
            throw new RestException("Server error", HttpStatus.BAD_GATEWAY);
        }
    }
}
