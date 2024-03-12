package com.tune_fun.v1.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public record Response<T extends BasePayload>(@NotNull String message, @NotNull String code,
                                              @JsonInclude(NON_NULL) @Nullable T data) implements AbstractResponse {

    @Override
    public String getMessage() {
        return message();
    }

    @Override
    public String getCode() {
        return code();
    }
}
