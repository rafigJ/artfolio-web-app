package ru.vsu.cs.artfolio.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final PostService service;

    @PostMapping
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public ResponseEntity<FullPostResponseDto> createPost(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("file") List<MultipartFile> listFiles) {

        try {
            var createdPost = service.createPost(user.getUserEntity().getUuid(),
                    new PostRequestDto(name, description),
                    listFiles);
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            throw new RestException("Server error " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

//    @GetMapping("/{fileName}")
//    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
//        LOGGER.info("download file {}", fileName);
//        try {
////            byte[] imageData = service.downloadImage(fileName);
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_PNG)
//                    .body(imageData);
//        } catch (IOException e) {
//            throw new RestException("Server error " + e.getMessage(), HttpStatus.BAD_GATEWAY);
//        }
//    }
}
