package ru.academits.phonebookspringboot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.academits.phonebookspringboot.data.BaseResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse handleValidationException(IllegalArgumentException e) {
        log.warn("Request failed: {}", e.getMessage());
        return BaseResponse.error(e.getMessage());
    }
}