package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatesValidationException extends RuntimeException {

    public DatesValidationException(String message) {
        super(message);
        log.warn(message);
    }
}
