package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException {

    public NotFoundException(String s) {
        super(s);
        log.warn(s);
    }
}
