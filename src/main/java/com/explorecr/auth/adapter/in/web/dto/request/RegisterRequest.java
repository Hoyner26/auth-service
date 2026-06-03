package com.explorecr.auth.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank @Size(max = 100) String name,
    @Email @NotBlank String email,
    @NotBlank @Size(min = 8, max = 100) String password,
    @NotBlank String confirmPassword
) {
}
