package com.example.fluxdemo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseBody {

    private String errorCode;
    private String message;
}
