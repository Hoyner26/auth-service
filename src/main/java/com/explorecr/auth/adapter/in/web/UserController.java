package com.explorecr.auth.adapter.in.web;

import com.explorecr.auth.adapter.in.web.dto.request.UpdateUserRequest;
import com.explorecr.auth.adapter.in.web.dto.response.ApiDataResponse;
import com.explorecr.auth.adapter.in.web.dto.response.MessageResponse;
import com.explorecr.auth.adapter.in.web.dto.response.PagedResponse;
import com.explorecr.auth.adapter.in.web.dto.response.UserResponse;
import com.explorecr.auth.domain.model.Role;
import com.explorecr.auth.domain.model.User;
import com.explorecr.auth.domain.port.in.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping
    public PagedResponse<UserResponse> listUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String role
    ) {
        return userUseCase.listUsers(page, pageSize, search, parseRole(role));
    }

    @GetMapping("/{id}")
    public ApiDataResponse<UserResponse> getUser(@PathVariable UUID id) {
        return new ApiDataResponse<>(userUseCase.getUser(id));
    }

    @PutMapping("/{id}")
    public UserUpdatedResponse updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        return new UserUpdatedResponse(userUseCase.updateUser(id, request), "User updated successfully");
    }

    @DeleteMapping("/{id}")
    public MessageResponse deleteUser(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        userUseCase.deleteUser(id, currentUser);
        return new MessageResponse("User deleted successfully");
    }

    private Role parseRole(String role) {
        if (role == null || role.isBlank()) {
            return null;
        }
        return Role.valueOf(role.trim().toUpperCase());
    }

    public record UserUpdatedResponse(UserResponse data, String message) {
    }
}
