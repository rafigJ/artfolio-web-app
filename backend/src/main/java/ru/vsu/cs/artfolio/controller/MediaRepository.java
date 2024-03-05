package ru.vsu.cs.artfolio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.service.MediaService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/medias")
@RequiredArgsConstructor
public class MediaRepository {

    private final MediaService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadMedia(@PathVariable Long id) {
        byte[] imageData = service.downloadMedia(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }
}
