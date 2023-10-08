package ru.practicum.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    String error;
    String description;
    String stackTrace;

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", description='" + description + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }
}
