package com.mj.string_frequency_manager.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<AppErrorResponse> handleAppRestException(AppException ex){

        if(ex.getErrorResponse() != null ){
            log.error("{}",ex.getErrorResponse().toString());
        }else{
            log.error("Unable to get error object from AppException. Showing generic error.");
        }

        AppErrorResponse errorResponse = ex.getErrorResponse() == null ? getGenericErrorCode(): ex.getErrorResponse();
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppErrorResponse> handleAppRestException(ConstraintViolationException ex){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                AppErrorResponse.builder()
                        .errorCode(AppErrorCode.STRING_FREQUENCY_RETRIEVAL_VALIDATION_ERROR)
                        .errors(ex.getConstraintViolations().stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.toList()))
                        .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<AppErrorResponse> handleResourceNotFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                AppErrorResponse.builder()
                        .errorCode(AppErrorCode.NO_HANDLER_ERROR)
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<AppErrorResponse> handleAll(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                AppErrorResponse.builder()
                        .errorCode(AppErrorCode.GENERIC_ERROR)
                        .error(e.getMessage())
                        .build());
    }


    private AppErrorResponse getGenericErrorCode(){
        return AppErrorResponse.builder().errorCode(AppErrorCode.UNKNOWN)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .error("Unable to process with null error response.").build();
    }

}
