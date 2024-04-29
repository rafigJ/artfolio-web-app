package ru.vsu.cs.artfolio.controller;

import io.minio.MinioClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.AuthenticationService;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.dto.auth.AuthRequestDto;
import ru.vsu.cs.artfolio.dto.auth.AuthResponseDto;
import ru.vsu.cs.artfolio.dto.auth.ChangePasswordRequestDto;
import ru.vsu.cs.artfolio.dto.auth.RegisterRequestDto;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestPart("userInfo") @Valid RegisterRequestDto request,
                                                    @RequestPart("avatarFile") MultipartFile avatarFile) {
        LOGGER.info("{}", avatarFile);
        var register = service.register(request, avatarFile);
        return ResponseEntity.ok(register);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthResponseDto> getUserCredentials(
            @RequestHeader String authorization,
            @AuthenticationPrincipal User user
    ) {
        String token = authorization.substring(7);
        var userEntity = user.getUserEntity();
        return ResponseEntity.ok(
                AuthResponseDto.builder()
                        .name(userEntity.getFullName())
                        .email(userEntity.getEmail())
                        .role(userEntity.getRole().name())
                        .token(token)
                        .build()
        );
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {
        service.changePassword(request);
        return ResponseEntity.ok().build();
    }
}
