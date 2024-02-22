package com.tune_fun.v1.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

/**
 * @see com.tune_fun.v1.common.response.ResponseMapper#error(MessageCode)
 */
@Getter
@AllArgsConstructor
public enum MessageCode {

    SUCCESS(HttpStatus.OK, "0000"),
    CREATED(HttpStatus.CREATED, "0001"),
    ACCEPTED(HttpStatus.ACCEPTED, "0002"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "9996"),
    NOT_OWNER(HttpStatus.UNAUTHORIZED, "9997"),
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, "9998"),
    ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999"),
    EXCEPTION_ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "9100"),


    // 사용자 관련
    ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST, "2000"),
    USER_POLICY_ACCOUNT_REGISTERED(HttpStatus.BAD_REQUEST, "2001"),
    USER_POLICY_NICKNAME_REGISTERED(HttpStatus.BAD_REQUEST, "2002"),
    USER_UNREGISTERED(HttpStatus.UNAUTHORIZED, "2003"),
    USER_POLICY_AUTH(HttpStatus.UNAUTHORIZED, "2004"),
    USER_POLICY_USERNAME_REGISTERED(HttpStatus.BAD_REQUEST, "2005"),

    // 로그인 관련
    EXCEPTION_AUTHENTICATION_LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "3001"),
    EXCEPTION_AUTHENTICATION_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "3002"),
    EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "3003"),
    EXCEPTION_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "3004"),
    EXCEPTION_REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "3005"),
    EXCEPTION_EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "3006"),
    EXCEPTION_EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "3007"),

    // OTP 관련
    EXCEPTION_OTP_NOT_FOUND(HttpStatus.BAD_REQUEST, "3101"),
    EXCEPTION_OTP_EXPIRED(HttpStatus.BAD_REQUEST, "3102"),
    EXCEPTION_OTP_NOT_MATCH(HttpStatus.BAD_REQUEST, "3103"),
    OTP_VERIFIED_SUCCESS(HttpStatus.OK, "3104"),

    // 분산 락 관련
    LOCK_ACQUISITION_FAILED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "4001"),
    LOCK_INTERRUPTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "4002");

    private final HttpStatus httpStatus;
    private final String code;

    public static Optional<MessageCode> get(String name) {
        return Arrays.stream(MessageCode.values())
                .filter(env -> env.name().equals(name))
                .findFirst();
    }

}
