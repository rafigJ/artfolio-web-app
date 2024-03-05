package ru.vsu.cs.artfolio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.artfolio.dto.auth.AuthResponseDto;
import ru.vsu.cs.artfolio.auth.user.User;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class SomeController {

    @GetMapping
    public ResponseEntity<AuthResponseDto> hello(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(AuthResponseDto.builder()
                .name(user.getUsername())
                .email(user.getPassword())
                .role(user.getUserEntity().getRole().name())
                .build());
    }
}
