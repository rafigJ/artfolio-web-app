package ru.vsu.cs.artfolio.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
        @NotBlank String email,
        @NotBlank String secretWord,

        @NotBlank(message = "should be not blank")
        @Size(min = 8, message = "Password should contain Minimum eight characters")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "Password must contain minimum eight characters: at least one letter and one number") String newPassword
) {
}
