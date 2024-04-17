package ru.vsu.cs.artfolio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.dto.RestExceptionDto;
import ru.vsu.cs.artfolio.dto.auth.AuthResponseDto;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class SomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SomeController.class);

    @GetMapping
    public ResponseEntity<AuthResponseDto> hello(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(AuthResponseDto.builder()
                .name(user.getUsername())
                .email(user.getPassword())
                .role(user.getUserEntity().getRole().name())
                .build());
    }

    @PostMapping()
    public ResponseEntity<AuthResponseDto> hellotest(@AuthenticationPrincipal User user,
                                                     @RequestPart("file") MultipartFile file,
                                                     @RequestPart("dto") @Valid RestExceptionDto dto) {
        LOGGER.info("Получен файл {}", file.getOriginalFilename());
        LOGGER.info("Получен dto {}", dto);
        return ResponseEntity.ok(AuthResponseDto.builder()
                .name(user.getUsername())
                .email(user.getPassword())
                .role(user.getUserEntity().getRole().name())
                .build());
    }
}
