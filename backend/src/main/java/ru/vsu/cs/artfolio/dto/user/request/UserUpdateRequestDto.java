package ru.vsu.cs.artfolio.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(
        @NotBlank(message = "should be not blank")
        @Size(min = 3, max = 40, message = "should contain minimum 3 characters, maximum 40 characters")
        String fullName,

        @Size(max = 401)
        String description,

        @Size(max = 40)
        String country,

        @Size(max = 40)
        String city,

        @NotBlank(message = "should be not blank")
        @Size(min = 5, max = 150, message = "should contain minimum 5 characters, maximum 150 characters")
        String username
) {
}
