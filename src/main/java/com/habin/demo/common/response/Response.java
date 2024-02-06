package com.habin.demo.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public record Response<T extends BasePayload>(String message, String code, @JsonInclude(NON_NULL) T data) implements AbstractResponse {

    @Override
    public String getMessage() {
        return message();
    }

    @Override
    public String getCode() {
        return code();
    }
}
