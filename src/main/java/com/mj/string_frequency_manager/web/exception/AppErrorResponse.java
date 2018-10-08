package com.mj.string_frequency_manager.web.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class AppErrorResponse {

    @Builder
    public AppErrorResponse(String error, AppErrorCode errorCode, HttpStatus httpStatus) {
        if(error != null) {
            this.errors = Collections.singletonList(error);
        }
        if(errorCode != null) {
            this.type = errorCode.name();
        }
        if(httpStatus != null){
            this.status = httpStatus.value();
        }
    }

    @Builder.Default private Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    @Builder.Default private Integer status = HttpStatus.BAD_REQUEST.value();

    private String type;
    private List<String> errors ;
}
