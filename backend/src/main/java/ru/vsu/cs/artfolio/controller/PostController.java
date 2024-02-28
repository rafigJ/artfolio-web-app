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

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final PostService service;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        LOGGER.info("upload file {}", file.getOriginalFilename());
        try {
            String uploadImage = service.uploadImage(file);
            return ResponseEntity.ok(uploadImage);
        } catch (IOException e) {
            throw new RestException("Server error", HttpStatus.BAD_GATEWAY);
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
