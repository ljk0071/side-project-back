package com.side.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private String message;

    private T data;

    public static <T> ApiResponse<T> success() {
        return ApiResponse.success(null, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                          .message(message)
                          .data(data)
                          .build();
    }
}
