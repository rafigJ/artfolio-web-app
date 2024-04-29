package ru.vsu.cs.artfolio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
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
            @RequestPart("post") @Valid PostRequestDto post,
            @RequestPart("file") List<MultipartFile> listFiles) {

        LOGGER.info("Пользователь {} сохраняет пост {}", user.getUsername(), post);
        var createdPost = service.createPost(user.getUserEntity().getUuid(), post, listFiles);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullPostResponseDto> getPostById(@PathVariable("id") Long id) {
        LOGGER.info("Получение поста " + id);
        return ResponseEntity.ok(service.getPostById(id));
    }

    @GetMapping("/medias/{id}")
    public ResponseEntity<InputStreamResource> downloadMedia(@PathVariable Long id) {
        MediaDto media = service.getMediaById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(media.contentType()))
                .body(new InputStreamResource(media.fileStream()));
    }
}
