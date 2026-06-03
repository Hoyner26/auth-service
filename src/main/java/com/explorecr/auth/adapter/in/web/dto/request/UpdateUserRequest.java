package com.explorecr.auth.adapter.in.web.dto.request;

import com.explorecr.auth.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @Size(max = 100) String name,
    @Email String email,
    Role role,
    @Size(max = 500) String avatar
) {
}
