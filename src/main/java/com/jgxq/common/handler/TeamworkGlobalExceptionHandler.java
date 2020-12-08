package com.jgxq.common.handler;

import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.exception.SmartException;
import com.jgxq.core.resp.ErrorMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LuCong
 * @since 2020-09-22
 **/
@RestControllerAdvice
@Order(Integer.MAX_VALUE - 1)
public class TeamworkGlobalExceptionHandler {

    public TeamworkGlobalExceptionHandler() {
    }

    @ExceptionHandler({SmartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onSmartException(HttpServletRequest request, HttpServletResponse response, SmartException exception) {

        return new ErrorMessage(exception.getCode(), exception.getMessage(), exception.getFields());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onMissingServletRequestParameterException(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        return new ErrorMessage(CommonErrorCode.BAD_REQUEST.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({MissingPathVariableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onMissingPathVariableException(HttpServletRequest request, HttpServletResponse response, Exception exception) {


        return new ErrorMessage(CommonErrorCode.MISSING_PATH_VARIABLE.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onHttpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, Exception exception) {


        return new ErrorMessage(CommonErrorCode.MISSING_REQUIRED_HTTP_BODY.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onDataIntegrityViolationException(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        return new ErrorMessage(CommonErrorCode.DATA_INTEGRITY_VIOLATION_EXCEPTION.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({DataAccessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onDataAccessException(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        return new ErrorMessage(CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), "Data Access error");
    }

    @ExceptionHandler({NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onNumberFormatException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        return new ErrorMessage(CommonErrorCode.NUMBER_FORMAT_EXCEPTION.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage illegalArgumentException(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        return new ErrorMessage(CommonErrorCode.BAD_REQUEST.getErrorCode(), exception.getMessage());
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onMethodArgumentTypeMismatchException(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        return new ErrorMessage(CommonErrorCode.ARGUMENT_TYPE_MISMATCH.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        return new ErrorMessage(CommonErrorCode.HTTP_METHOD_NOT_SUPPORTED.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage onUnknownException(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        return new ErrorMessage(CommonErrorCode.UNKNOWN_ERROR.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) {
        String message = exception.getMessage();
        message = message.substring(message.lastIndexOf("[") + 1, message.lastIndexOf("]") - 1);
        return new ErrorMessage(CommonErrorCode.VALIDATION_ERROR.getErrorCode(), message);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleConstraintViolationException(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException exception) {
        List<String> errors = (List) exception.getConstraintViolations().stream().map(this::toString).collect(Collectors.toList());
        String message = StringUtils.strip(errors.toString(), "[]");
        return new ErrorMessage(CommonErrorCode.VALIDATION_ERROR.getErrorCode(), message);
    }

    private String toString(ConstraintViolation<?> violation) {
        return violation.getMessage();
//        return violation.getPropertyPath() + "校验失败：" + violation.getMessage();
    }
}
