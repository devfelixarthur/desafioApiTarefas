package com.api.v1.tarefas.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

import java.util.List;

@Getter
@Setter
public class ErrorMessage {
    private Integer statusCode;
    private Instant timestamp;
    private String error;
    private List<Map<String, String>> description;

    public ErrorMessage(Integer statusCode, Instant timestamp, String error, List<Map<String, String>> description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.error = error;
        this.description = description;
    }


}
