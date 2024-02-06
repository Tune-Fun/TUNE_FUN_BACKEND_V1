package com.tune_fun.v1.common.response;

public sealed interface AbstractResponse permits Response, ExceptionResponse {

    String getMessage();

    String getCode();

}
