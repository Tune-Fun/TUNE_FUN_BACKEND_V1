package com.tune_fun.v1.common.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tune_fun.v1.common.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    protected ResponseEntity<Response<ValidationErrorData>> illegalArgumentExceptionHandle(MethodArgumentNotValidException e) {
        Map<String, String> validationErrorPair = getValidationErrorPair(e);
        return responseMapper.ok(MessageCode.EXCEPTION_ILLEGAL_ARGUMENT, new ValidationErrorData(validationErrorPair));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> allExceptionHandle(Exception e) {
        return responseMapper.error(MessageCode.ERROR);
    }

    private Map<String, String> getValidationErrorPair(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        return fieldErrorList.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    protected record ValidationErrorData(@JsonUnwrapped Map<String, String> errors) implements BasePayload {
    }

}
