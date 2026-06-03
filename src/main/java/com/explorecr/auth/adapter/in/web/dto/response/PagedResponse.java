package com.explorecr.auth.adapter.in.web.dto.response;

import java.util.List;

public record PagedResponse<T>(
    List<T> data,
    long total,
    int page,
    int pageSize,
    int totalPages
) {
}
