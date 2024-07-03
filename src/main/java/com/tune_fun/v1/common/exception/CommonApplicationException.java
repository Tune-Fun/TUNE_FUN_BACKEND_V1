package com.tune_fun.v1.common.exception;

import com.tune_fun.v1.common.response.MessageCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

/**
 * @see <a href="https://velog.io/@hope0206/Java의-예외-생성-비용-비용-절감-방법">Java의 예외 생성 비용 절감 방법</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CommonApplicationException extends RuntimeException implements Supplier<CommonApplicationException> {

    private MessageCode messageCode;

    public static final CommonApplicationException TOO_MANY_REQUESTS = new CommonApplicationException(MessageCode.TOO_MANY_REQUESTS);
    public static final CommonApplicationException NOT_OWNER = new CommonApplicationException(MessageCode.NOT_OWNER);
    public static final CommonApplicationException NOT_FOUND_DATA = new CommonApplicationException(MessageCode.NOT_FOUND_DATA);
    public static final CommonApplicationException ERROR = new CommonApplicationException(MessageCode.ERROR);
    public static final CommonApplicationException EXCEPTION_ILLEGAL_ARGUMENT = new CommonApplicationException(MessageCode.EXCEPTION_ILLEGAL_ARGUMENT);

    public static final CommonApplicationException ACCOUNT_NOT_FOUND = new CommonApplicationException(MessageCode.ACCOUNT_NOT_FOUND);
    public static final CommonApplicationException USER_POLICY_ACCOUNT_REGISTERED = new CommonApplicationException(MessageCode.USER_POLICY_ACCOUNT_REGISTERED);
    public static final CommonApplicationException USER_POLICY_NICKNAME_REGISTERED = new CommonApplicationException(MessageCode.USER_POLICY_NICKNAME_REGISTERED);
    public static final CommonApplicationException USER_UNREGISTERED = new CommonApplicationException(MessageCode.USER_UNREGISTERED);
    public static final CommonApplicationException USER_POLICY_AUTH = new CommonApplicationException(MessageCode.USER_POLICY_AUTH);
    public static final CommonApplicationException USER_POLICY_USERNAME_REGISTERED = new CommonApplicationException(MessageCode.USER_POLICY_USERNAME_REGISTERED);
    public static final CommonApplicationException USER_POLICY_EMAIL_REGISTERED = new CommonApplicationException(MessageCode.USER_POLICY_EMAIL_REGISTERED);
    public static final CommonApplicationException USER_POLICY_CANNOT_UNLINK_UNIQUE_PROVIDER = new CommonApplicationException(MessageCode.USER_POLICY_CANNOT_UNLINK_UNIQUE_PROVIDER);
    public static final CommonApplicationException USER_POLICY_ALREADY_LINKED_PROVIDER = new CommonApplicationException(MessageCode.USER_POLICY_ALREADY_LINKED_PROVIDER);
    public static final CommonApplicationException USER_POLICY_CANNOT_REGISTER_EMAIL_TWICE = new CommonApplicationException(MessageCode.USER_POLICY_CANNOT_REGISTER_EMAIL_TWICE);

    public static final CommonApplicationException EXCEPTION_AUTHENTICATION_LOGIN_FAIL = new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_LOGIN_FAIL);
    public static final CommonApplicationException EXCEPTION_AUTHENTICATION_INVALID_TOKEN = new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_INVALID_TOKEN);
    public static final CommonApplicationException EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND = new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND);
    public static final CommonApplicationException EXCEPTION_EXPIRED_TOKEN = new CommonApplicationException(MessageCode.EXCEPTION_EXPIRED_TOKEN);
    public static final CommonApplicationException EXCEPTION_REFRESH_TOKEN_NOT_FOUND = new CommonApplicationException(MessageCode.EXCEPTION_REFRESH_TOKEN_NOT_FOUND);
    public static final CommonApplicationException EXCEPTION_EXPIRED_REFRESH_TOKEN = new CommonApplicationException(MessageCode.EXCEPTION_EXPIRED_REFRESH_TOKEN);
    public static final CommonApplicationException EXCEPTION_EMAIL_NOT_VERIFIED = new CommonApplicationException(MessageCode.EXCEPTION_EMAIL_NOT_VERIFIED);

    public static final CommonApplicationException EXCEPTION_OTP_NOT_FOUND = new CommonApplicationException(MessageCode.EXCEPTION_OTP_NOT_FOUND);
    public static final CommonApplicationException EXCEPTION_OTP_EXPIRED = new CommonApplicationException(MessageCode.EXCEPTION_OTP_EXPIRED);
    public static final CommonApplicationException EXCEPTION_OTP_NOT_MATCH = new CommonApplicationException(MessageCode.EXCEPTION_OTP_NOT_MATCH);
    public static final CommonApplicationException EXCEPTION_OTP_TYPE_NOT_FOUND = new CommonApplicationException(MessageCode.SUCCESS_OTP_VERIFIED);

    public static final CommonApplicationException VOTE_POLICY_ONE_VOTE_PAPER_PER_USER = new CommonApplicationException(MessageCode.VOTE_POLICY_ONE_VOTE_PAPER_PER_USER);
    public static final CommonApplicationException VOTE_POLICY_ONLY_AUTHOR_CAN_UPDATE_DELIVERY_DATE = new CommonApplicationException(MessageCode.VOTE_POLICY_ONLY_AUTHOR_CAN_UPDATE_DELIVERY_DATE);
    public static final CommonApplicationException VOTE_PAPER_NOT_FOUND = new CommonApplicationException(MessageCode.VOTE_PAPER_NOT_FOUND);
    public static final CommonApplicationException VOTE_POLICY_ONE_VOTE_PER_USER = new CommonApplicationException(MessageCode.VOTE_POLICY_ONE_VOTE_PER_USER);
    public static final CommonApplicationException VOTE_POLICY_ONLY_AUTHOR_CAN_UPDATE_VIDEO_URL = new CommonApplicationException(MessageCode.VOTE_POLICY_ONLY_AUTHOR_CAN_UPDATE_VIDEO_URL);
    public static final CommonApplicationException VOTE_POLICY_ONLY_AUTHOR_CAN_DELETE = new CommonApplicationException(MessageCode.VOTE_POLICY_ONLY_AUTHOR_CAN_DELETE);
    public static final CommonApplicationException VOTE_POLICY_OFFERS_COUNT_SHOULD_BE_MORE_THAN_TWO = new CommonApplicationException(MessageCode.VOTE_POLICY_OFFERS_COUNT_SHOULD_BE_MORE_THAN_TWO);
    public static final CommonApplicationException VOTE_POLICY_ONLY_REGISTER_CHOICE_ON_ALLOW_ADD_CHOICES_OPTION = new CommonApplicationException(MessageCode.VOTE_POLICY_ONLY_REGISTER_CHOICE_ON_ALLOW_ADD_CHOICES_OPTION);
    public static final CommonApplicationException VOTE_POLICY_ONE_VOTE_CHOICE_PER_USER_ON_VOTE_PAPER = new CommonApplicationException(MessageCode.VOTE_POLICY_ONE_VOTE_CHOICE_PER_USER_ON_VOTE_PAPER);
    public static final CommonApplicationException VOTE_POLICY_ALREADY_LIKED_VOTE_PAPER = new CommonApplicationException(MessageCode.VOTE_POLICY_ALREADY_LIKED_VOTE_PAPER);

    public static final CommonApplicationException ALREADY_FOLLOWED = new CommonApplicationException(MessageCode.ALREADY_FOLLOWED);
    public static final CommonApplicationException NOT_FOLLOWED = new CommonApplicationException(MessageCode.NOT_FOLLOWED);

    public static final CommonApplicationException LOCK_ACQUISITION_FAILED_ERROR = new CommonApplicationException(MessageCode.LOCK_ACQUISITION_FAILED_ERROR);
    public static final CommonApplicationException LOCK_INTERRUPTED_ERROR = new CommonApplicationException(MessageCode.LOCK_INTERRUPTED_ERROR);


    @Override
    public synchronized Throwable fillInStackTrace() {
        return MessageCode.ERROR.equals(messageCode) ? super.fillInStackTrace() : this;
    }

    @Override
    public CommonApplicationException get() {
        return this;
    }
}
