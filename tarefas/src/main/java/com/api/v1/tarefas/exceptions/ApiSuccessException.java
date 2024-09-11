package com.api.v1.tarefas.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiSuccessException extends RuntimeException {
    private HttpStatus httpStatus;

    public ApiSuccessException(){
    }

    public ApiSuccessException(String message) {
        super(message);
    }

    public ApiSuccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiSuccessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiSuccessException(String title, String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
