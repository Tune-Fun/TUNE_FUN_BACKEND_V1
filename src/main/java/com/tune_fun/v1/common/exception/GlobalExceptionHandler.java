package com.tune_fun.v1.common.exception;

import com.tune_fun.v1.common.response.ExceptionResponse;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.response.ResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseMapper responseMapper;

    @ExceptionHandler(CommonApplicationException.class)
    protected ResponseEntity<ExceptionResponse> commonApplicationException(CommonApplicationException e) {
        return e.getMessageCode() == null ?
                responseMapper.error(MessageCode.ERROR) : responseMapper.error(e.getMessageCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponse> illegalArgumentExceptionHandle(MethodArgumentNotValidException e) {
        return responseMapper.error(MessageCode.EXCEPTION_ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> allExceptionHandle(Exception e) {
        return responseMapper.error(MessageCode.ERROR);
    }

}
