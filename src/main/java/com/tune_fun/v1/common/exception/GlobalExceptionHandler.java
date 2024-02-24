package com.tune_fun.v1.common.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tune_fun.v1.common.response.*;
import com.tune_fun.v1.common.util.i18n.MessageSourceUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.tune_fun.v1.common.response.MessageCode.ERROR;
import static com.tune_fun.v1.common.response.MessageCode.EXCEPTION_ILLEGAL_ARGUMENT;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseMapper responseMapper;

    @ExceptionHandler(CommonApplicationException.class)
    protected ResponseEntity<ExceptionResponse> commonApplicationException(CommonApplicationException e) {
        return e.getMessageCode() == null ?
                responseMapper.error(ERROR) : responseMapper.error(e.getMessageCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Response<ValidationErrorData>> constraintViolationException(ConstraintViolationException e) {
        Map<String, String> validationErrorPair = e.getConstraintViolations().stream()
                .collect(toMap(violation -> getPropertyName(
                                violation.getPropertyPath().toString()),
                        ConstraintViolation::getMessage)
                );
        return responseMapper.ok(EXCEPTION_ILLEGAL_ARGUMENT, new ValidationErrorData(validationErrorPair));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Response<ValidationErrorData>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> validationErrorPair = getValidationErrorPair(e);
        return responseMapper.ok(EXCEPTION_ILLEGAL_ARGUMENT, new ValidationErrorData(validationErrorPair));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> exception(Exception e) {
        return responseMapper.error(ERROR);
    }

    private Map<String, String> getValidationErrorPair(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        return fieldErrorList.stream().collect(toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    protected record ValidationErrorData(@JsonUnwrapped Map<String, String> errors) implements BasePayload {
    }

    protected static String getPropertyName(String propertyPath) {
        return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
    }

}
