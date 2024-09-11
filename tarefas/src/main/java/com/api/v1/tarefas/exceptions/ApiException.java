package com.api.v1.tarefas.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private String title;
    private HttpStatus httpStatus;

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String title, String message) {
        super(message);
        this.title = title;
    }

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(String title, String message, HttpStatus httpStatus) {
        super(message);
        this.title = title;
        this.httpStatus = httpStatus;
    }

}
