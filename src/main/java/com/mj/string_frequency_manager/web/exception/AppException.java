package com.mj.string_frequency_manager.web.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppException extends RuntimeException {

    protected AppErrorResponse errorResponse;

}
