package com.mj.string_frequency_manager.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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


    private AppErrorResponse getGenericErrorCode(){
        return AppErrorResponse.builder().errorCode(AppErrorCode.UNKNOWN)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .error("Unable to process with null error response.").build();
    }

}
