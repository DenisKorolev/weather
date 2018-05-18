package ru.bellintegrator.weather.common.util;

import ru.bellintegrator.weather.common.exception.FieldIsNotDataTypeException;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class ValidationUtils {


    public static void checkFieldOnNotFloat(String field, String fieldName){

        try {
            Float.parseFloat(field);
        }
        catch (NumberFormatException ex){
            throw new FieldIsNotDataTypeException(fieldName, "Float");
        }
    }

    public static void checkFieldOnNotDouble(String field, String fieldName){

        try {
            Double.parseDouble(field);
        }
        catch (NumberFormatException ex){
            throw new FieldIsNotDataTypeException(fieldName, "Double");
        }
    }

    public static void checkFieldOnNotInstant(String field, String fieldName){

        try {
            Instant.parse(field);
        }
        catch (DateTimeParseException ex){
            throw new FieldIsNotDataTypeException(fieldName, "Instant");
        }
    }

    public static void checkFieldOnNotLong(String field, String fieldName){

        try {
            Long.parseLong(field);
        }
        catch (NumberFormatException ex){
            throw new FieldIsNotDataTypeException(fieldName, "Long");
        }
    }
}
