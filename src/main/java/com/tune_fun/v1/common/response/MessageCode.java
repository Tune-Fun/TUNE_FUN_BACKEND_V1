package com.tune_fun.v1.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

/**
 * @see com.tune_fun.v1.common.response.ResponseMapper#ok(MessageCode)
 * @see com.tune_fun.v1.common.response.ResponseMapper#error(MessageCode)
 */
@Getter
@AllArgsConstructor
public enum MessageCode {

    SUCCESS(OK, "0000"),
    CREATED(HttpStatus.CREATED, "0001"),
    ACCEPTED(HttpStatus.ACCEPTED, "0002"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "9996"),
    NOT_OWNER(UNAUTHORIZED, "9997"),
    NOT_FOUND_DATA(BAD_REQUEST, "9998"),
    ERROR(INTERNAL_SERVER_ERROR, "9999"),
    EXCEPTION_ILLEGAL_ARGUMENT(BAD_REQUEST, "9100"),


    // 사용자 관련
    ACCOUNT_NOT_FOUND(BAD_REQUEST, "2000"),
    USER_POLICY_ACCOUNT_REGISTERED(BAD_REQUEST, "2001"),
    USER_POLICY_NICKNAME_REGISTERED(BAD_REQUEST, "2002"),
    USER_UNREGISTERED(UNAUTHORIZED, "2003"),
    USER_POLICY_AUTH(UNAUTHORIZED, "2004"),
    USER_POLICY_USERNAME_REGISTERED(BAD_REQUEST, "2005"),
    USER_POLICY_EMAIL_REGISTERED(BAD_REQUEST, "2006"),
    SUCCESS_USERNAME_UNIQUE(OK, "2007"),
    SUCCESS_EMAIL_UNIQUE(OK, "2008"),
    SUCCESS_EMAIL_VERIFIED(OK, "2009"),
    SUCCESS_SET_NEW_PASSWORD(OK, "2010"),
    SUCCESS_UPDATE_NICKNAME(OK, "2011"),
    USER_POLICY_CANNOT_UNLINK_FIRST_PROVIDER(BAD_REQUEST, "2012"),

    // 로그인 관련
    EXCEPTION_AUTHENTICATION_LOGIN_FAIL(UNAUTHORIZED, "3001"),
    EXCEPTION_AUTHENTICATION_INVALID_TOKEN(UNAUTHORIZED, "3002"),
    EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND(UNAUTHORIZED, "3003"),
    EXCEPTION_EXPIRED_TOKEN(UNAUTHORIZED, "3004"),
    EXCEPTION_REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "3005"),
    EXCEPTION_EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "3006"),
    EXCEPTION_EMAIL_NOT_VERIFIED(FORBIDDEN, "3007"),

    // OTP 관련
    EXCEPTION_OTP_NOT_FOUND(BAD_REQUEST, "3101"),
    EXCEPTION_OTP_EXPIRED(BAD_REQUEST, "3102"),
    EXCEPTION_OTP_NOT_MATCH(BAD_REQUEST, "3103"),
    SUCCESS_OTP_VERIFIED(OK, "3104"),
    EXCEPTION_OTP_TYPE_NOT_FOUND(BAD_REQUEST, "3105"),
    SUCCESS_FORGOT_PASSWORD_OTP_SENT(HttpStatus.CREATED, "3106"),
    SUCCESS_OTP_RESEND(HttpStatus.CREATED, "3107"),

    // 분산 락 관련
    LOCK_ACQUISITION_FAILED_ERROR(INTERNAL_SERVER_ERROR, "4001"),
    LOCK_INTERRUPTED_ERROR(INTERNAL_SERVER_ERROR, "4002");

    private final HttpStatus httpStatus;
    private final String code;

    public static Optional<MessageCode> get(String name) {
        return Arrays.stream(MessageCode.values())
                .filter(env -> env.name().equals(name))
                .findFirst();
    }

}
