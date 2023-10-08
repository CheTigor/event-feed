package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnavailablePublishedEventException extends RuntimeException {
    public UnavailablePublishedEventException(String s) {
        super(s);
        log.warn(s);
    }
}
