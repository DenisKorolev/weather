package ru.bellintegrator.weather.common.exception;


import javax.validation.ValidationException;

public class FieldIsNotDataTypeException extends ValidationException {
    public FieldIsNotDataTypeException(String fieldName, String dataType){
        super("Field " + fieldName + " must be " + dataType + "!");
    }
}
