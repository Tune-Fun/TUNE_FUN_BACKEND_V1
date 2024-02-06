package com.habin.demo.common.response;

public record ExceptionResponse(String message, String code) implements AbstractResponse {
    @Override
    public String getMessage() {
        return message();
    }

    @Override
    public String getCode() {
        return code();
    }
}
