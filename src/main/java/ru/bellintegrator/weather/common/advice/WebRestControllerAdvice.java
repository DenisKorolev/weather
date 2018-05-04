package ru.bellintegrator.weather.common.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bellintegrator.weather.common.view.ErrorView;

import javax.validation.ValidationException;

@RestControllerAdvice
public class WebRestControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    public ErrorView handleValidationException(ValidationException ex){
        ErrorView errorView = new ErrorView(ex.getMessage());
        return errorView;
    }
}
