package com.explorecr.auth.adapter.in.web.dto.response;

public record ErrorResponse(
    String error,
    int status
) {
}
