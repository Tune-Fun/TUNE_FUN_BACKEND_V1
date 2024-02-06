package com.habin.demo.common.response;

public sealed interface AbstractResponse permits Response, ExceptionResponse {

    String getMessage();

    String getCode();

}
