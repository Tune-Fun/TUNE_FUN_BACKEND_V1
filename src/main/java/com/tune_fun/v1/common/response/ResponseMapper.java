package com.tune_fun.v1.common.response;

import com.tune_fun.v1.common.util.i18n.MessageSourceUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResponseMapper {

    private final MessageSourceUtil messageSourceUtil;

    public ResponseEntity<Response<?>> ok() {
        return ResponseEntity.ok().body(
                new Response<>(messageSourceUtil.getMessage(MessageCode.SUCCESS.getCode()), MessageCode.SUCCESS.getCode(), null)
        );
    }

    private <T extends BasePayload> ResponseEntity<Response<T>> ok(final MessageCode messageCode, final T source) {
        return ResponseEntity.ok().body(
                new Response<>(messageSourceUtil.getMessage(messageCode.getCode()), messageCode.getCode(), source)
        );
    }

    private <T extends BasePayload> ResponseEntity<Response<T>> created(final MessageCode messageCode, final T source) {
        return ResponseEntity.created(null).body(
                new Response<>(messageSourceUtil.getMessage(messageCode.name()), messageCode.getCode(), source)
        );
    }

    public <T extends BasePayload> ResponseEntity<Response<T>> ok(final MessageCode messageCode) {
        return ok(messageCode, null);
    }

    public <T extends BasePayload> ResponseEntity<Response<T>> ok(final T source) {
        return ok(MessageCode.SUCCESS, source);
    }

    public ResponseEntity<ExceptionResponse> error(final MessageCode messageCode) {
        return ResponseEntity
                .status(messageCode.getHttpStatus())
                .body(new ExceptionResponse(messageSourceUtil.getMessage(messageCode.getCode()), messageCode.getCode()));
    }

    public ResponseEntity<ExceptionResponse> error(final MessageCode messageCode, final String message) {
        return ResponseEntity
                .status(messageCode.getHttpStatus())
                .body(new ExceptionResponse(message, messageCode.getCode()));
    }

}
