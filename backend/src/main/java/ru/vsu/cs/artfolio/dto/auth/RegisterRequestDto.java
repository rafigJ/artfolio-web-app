package ru.vsu.cs.artfolio.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "should be not blank")
        @Size(min = 3, max = 40, message = "should contain minimum 3 characters, maximum 40 characters")
        String fullName,

        @NotBlank(message = "should be not blank")
        @Size(min = 5, max = 150, message = "should contain minimum 5 characters, maximum 150 characters")
        @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
                message = "Invalid email format")
        String email,

        @NotBlank(message = "should be not blank")
        @Size(min = 8, message = "Password should contain Minimum eight characters")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "Password must contain minimum eight characters: at least one letter and one number")
        String password,

        @NotBlank(message = "should be not blank")
        @Size(min = 5, message = "Secret word should contain Minimum eight characters")
        String secretWord
) {}

