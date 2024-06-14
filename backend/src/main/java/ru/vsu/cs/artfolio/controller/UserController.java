package ru.vsu.cs.artfolio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.dto.user.request.UserUpdateRequestDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.service.UserService;

import javax.annotation.Nullable;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final UserService service;

    @GetMapping("/{username}")
    public ResponseEntity<FullUserResponseDto> getUserByUsername(@Nullable @AuthenticationPrincipal User user, @PathVariable("username") String username) {
        LOGGER.info("Получение данных о {}", username);
        UserEntity executor = user != null ? user.getUserEntity() : null;
        return ResponseEntity.ok(service.getUserByUsername(executor, username));
    }

    @PutMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FullUserResponseDto> updateUser(@AuthenticationPrincipal User user,
                                                          @RequestPart("userInfo") @Valid UserUpdateRequestDto request,
                                                          @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {
        LOGGER.info("Обновление данных о {}", user.getUsername());
        return ResponseEntity.ok(service.updateUserInformation(user.getUserEntity(), request, avatarFile));
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUserByUsername(@AuthenticationPrincipal User user,
                                                  @PathVariable("username") String username) {
        LOGGER.info("Удаление пользователя {}", username);
        service.deleteUser(user.getUserEntity(), username);
        return ResponseEntity.ok("{\"message\": \"user " + username + " is deleted\"}");
    }

    @GetMapping("/{username}/avatar")
    public ResponseEntity<InputStreamResource> getUserAvatar(@PathVariable("username") String username) {
        var avatar = service.downloadAvatar(username);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(avatar.contentType()))
                .body(new InputStreamResource(avatar.fileStream()));
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<PageDto<PostResponseDto>> getPostsByUsername(@RequestParam(value = "_page", defaultValue = "0") Integer page,
                                                                       @RequestParam(value = "_limit", defaultValue = "10") Integer limit,
                                                                       @PathVariable("username") String username) {
        var posts = service.getPostsPageByUsername(username, PageRequest.of(page, limit));
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{username}/subscribes")
    public ResponseEntity<PageDto<UserResponseDto>> getSubscribes(@RequestParam(value = "_page", defaultValue = "0") Integer page,
                                                                  @RequestParam(value = "_limit", defaultValue = "10") Integer limit,
                                                                  @PathVariable("username") String username) {
        return ResponseEntity.ok(service.getAllUserSubscribes(username, PageRequest.of(page, limit)));
    }

    @PostMapping("/{username}/subscribes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> subscribe(@PathVariable("username") String username, @AuthenticationPrincipal User user) {
        service.subscribe(user.getUserEntity(), username);
        return ResponseEntity.ok("{\"message\": \"subscribed to " + username + "\"}");
    }

    @DeleteMapping("/{username}/subscribes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteSubscribe(@PathVariable("username") String username, @AuthenticationPrincipal User user) {
        service.deleteSubscribe(user.getUserEntity(), username);
        return ResponseEntity.ok("{\"message\": \"delete subscribe from " + username + "\"}");
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<PageDto<?>> getFollowers(@RequestParam(value = "_page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "_limit", defaultValue = "10") Integer limit,
                                                   @PathVariable("username") String username) {
        return ResponseEntity.ok(service.getAllUserFollowers(username, PageRequest.of(page, limit)));
    }

}
