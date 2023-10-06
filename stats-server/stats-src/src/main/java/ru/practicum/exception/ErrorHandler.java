package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerDateTimeFormatException(DateTimeFormatException e) {
        log.info("400 {}", e.getMessage());
        return new ErrorResponse("Wrong time format", e.getMessage(), null);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerException(Exception e) {
        log.info("Error", e);
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return new ErrorResponse("Что то пошло не так", e.getMessage(), stackTrace);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.info("400 - Bad request", e);
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return new ErrorResponse("Что то пошло не так", e.getMessage(), stackTrace);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerDatesValidationException(DatesValidationException e) {
        log.info("400 - Bad request", e);
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return new ErrorResponse("Ошибка валидации дат", e.getMessage(), stackTrace);
    }

}
