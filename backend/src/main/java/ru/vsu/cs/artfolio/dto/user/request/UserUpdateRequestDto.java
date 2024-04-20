package ru.vsu.cs.artfolio.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(
        @NotBlank(message = "should be not blank")
        @Size(min = 3, max = 40, message = "should contain minimum 3 characters, maximum 40 characters")
        String fullName,

        String description,

        String country,

        String city,

        @NotBlank(message = "should be not blank")
        @Size(min = 5, max = 150, message = "should contain minimum 5 characters, maximum 150 characters")
        String username,

        @NotBlank(message = "should be not blank")
        @Size(min = 5, max = 150, message = "should contain minimum 5 characters, maximum 150 characters")
        @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
                message = "Invalid email format")
        String email
) {
}
