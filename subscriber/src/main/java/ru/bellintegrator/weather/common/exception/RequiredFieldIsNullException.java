package ru.bellintegrator.weather.common.exception;

import javax.validation.ValidationException;

public class RequiredFieldIsNullException extends ValidationException {
    public RequiredFieldIsNullException(String fieldName){
        super("Required field " + fieldName + " couldn't be empty!");
    }
}
