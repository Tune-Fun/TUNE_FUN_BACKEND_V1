package com.tune_fun.v1.common.exception;

import com.tune_fun.v1.common.response.ExceptionResponse;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.response.ValidationErrorData;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.tune_fun.v1.common.constant.Constants.DOT;
import static com.tune_fun.v1.common.response.MessageCode.ERROR;
import static com.tune_fun.v1.common.response.MessageCode.EXCEPTION_ILLEGAL_ARGUMENT;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseMapper responseMapper;

    protected static String getPropertyName(String propertyPath) {
        return propertyPath.substring(propertyPath.lastIndexOf(DOT) + 1);
    }

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
        return fieldErrorList.stream().collect(toMap(
                        fieldError -> LOWER_CAMEL.to(LOWER_UNDERSCORE, fieldError.getField()),
                        FieldError::getDefaultMessage
                )
        );
    }

}
