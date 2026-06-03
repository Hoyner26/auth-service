package com.explorecr.auth.domain.port.in;

import com.explorecr.auth.adapter.in.web.dto.request.UpdateUserRequest;
import com.explorecr.auth.adapter.in.web.dto.response.PagedResponse;
import com.explorecr.auth.adapter.in.web.dto.response.UserResponse;
import com.explorecr.auth.domain.model.Role;
import com.explorecr.auth.domain.model.User;

import java.util.UUID;

public interface UserUseCase {
    PagedResponse<UserResponse> listUsers(int page, int pageSize, String search, Role role);

    UserResponse getUser(UUID id);

    UserResponse updateUser(UUID id, UpdateUserRequest request);

    void deleteUser(UUID id, User currentUser);
}
