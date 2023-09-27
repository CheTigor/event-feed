package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTimeFormatException extends RuntimeException {

    public DateTimeFormatException(String s) {
        super(s);
        log.warn(s);
    }
}
