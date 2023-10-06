package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublishStateException extends RuntimeException {

    public PublishStateException(String s) {
        super(s);
        log.warn(s);
    }
}
