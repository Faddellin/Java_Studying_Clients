package org.example.clientsbackend.Application.Exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.example.clientsbackend.Application.Models.Common.ResponseModel;
import org.example.clientsbackend.Application.Utilities.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Dictionary;
import java.util.Hashtable;

@ControllerAdvice
public class ExceptionsHandler {

    private Pair<Integer, HttpStatus> getStatusCodeAndHttpStatusByExceptionClass(Class<? extends Exception> exceptionClass) {
        if(exceptionClass.equals(EntityNotFoundException.class)) {
            return new Pair<>(404, HttpStatus.NOT_FOUND);
        }
        else if(exceptionClass.equals(ConstraintViolationException.class) ||
                exceptionClass.equals(MethodArgumentNotValidException.class) ||
                exceptionClass.equals(NoResourceFoundException.class) ||
                exceptionClass.equals(ValidationException.class) ||
                exceptionClass.equals(HttpMessageNotReadableException.class)) {
            return new Pair<>(400, HttpStatus.BAD_REQUEST);
        }

        return new Pair<>(500, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExceptionWrapper.class)
    public ResponseEntity<ResponseModel> handleAllWrappedExceptions(
            ExceptionWrapper e
    ) {
        Pair<Integer, HttpStatus> statusCodeAndHttpStatus = getStatusCodeAndHttpStatusByExceptionClass(e.getExceptionClass());
        ResponseModel response = new ResponseModel(statusCodeAndHttpStatus.first, e.getErrors());
        return new ResponseEntity<ResponseModel>(response, statusCodeAndHttpStatus.second);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseModel> handleConstraintValidationException(
            ConstraintViolationException e
    ) {
        Dictionary<String, String> errors = new Hashtable<String, String>();
        e.getConstraintViolations()
                .forEach(
                        violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage())
                );
        Pair<Integer, HttpStatus> statusCodeAndHttpStatus = getStatusCodeAndHttpStatusByExceptionClass(e.getClass());
        ResponseModel response = new ResponseModel(statusCodeAndHttpStatus.first, errors);
        return new ResponseEntity<ResponseModel>(response, statusCodeAndHttpStatus.second);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseModel> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        Dictionary<String, String> errors = new Hashtable<String, String>();
        e.getBindingResult().getFieldErrors()
                .forEach(
                        violation -> errors.put(violation.getField(), violation.getDefaultMessage())
                );
        Pair<Integer, HttpStatus> statusCodeAndHttpStatus = getStatusCodeAndHttpStatusByExceptionClass(e.getClass());
        ResponseModel response = new ResponseModel(statusCodeAndHttpStatus.first, errors);
        return new ResponseEntity<ResponseModel>(response, statusCodeAndHttpStatus.second);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseModel> handleValidationException(
            ValidationException e
    ) {
        Pair<Integer, HttpStatus> statusCodeAndHttpStatus = getStatusCodeAndHttpStatusByExceptionClass(e.getClass());
        Dictionary<String, String> errors = new Hashtable<String, String>();
        errors.put("Validation", e.getMessage());
        ResponseModel response = new ResponseModel(statusCodeAndHttpStatus.first, errors);
        return new ResponseEntity<ResponseModel>(response, statusCodeAndHttpStatus.second);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseModel> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e
    ) {
        Pair<Integer, HttpStatus> statusCodeAndHttpStatus = getStatusCodeAndHttpStatusByExceptionClass(e.getClass());
        Dictionary<String, String> errors = new Hashtable<String, String>();
        errors.put("JsonBody", e.getMessage());
        ResponseModel response = new ResponseModel(statusCodeAndHttpStatus.first, errors);
        return new ResponseEntity<ResponseModel>(response, statusCodeAndHttpStatus.second);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseModel> handleNoResourceFoundException(
            NoResourceFoundException e
    ) {
        Pair<Integer, HttpStatus> statusCodeAndHttpStatus = getStatusCodeAndHttpStatusByExceptionClass(e.getClass());
        Dictionary<String, String> errors = new Hashtable<String, String>();
        errors.put("url", e.getMessage());
        ResponseModel response = new ResponseModel(statusCodeAndHttpStatus.first, errors);
        return new ResponseEntity<ResponseModel>(response, statusCodeAndHttpStatus.second);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel> handleNotKnownException(
            Exception e
    ) {
        Pair<Integer, HttpStatus> statusCodeAndHttpStatus = getStatusCodeAndHttpStatusByExceptionClass(e.getClass());
        Dictionary<String, String> errors = new Hashtable<String, String>();
        errors.put("unknownError", "Unknown error, please tell us about it");
        ResponseModel response = new ResponseModel(statusCodeAndHttpStatus.first, errors);
        return new ResponseEntity<ResponseModel>(response, statusCodeAndHttpStatus.second);
    }
}
