package ru.vsu.cs.artfolio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.AuthenticationService;
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

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {
        service.changePassword(request);
        return ResponseEntity.ok().build();
    }
}
