package ru.bellintegrator.weather.common.exception;


import javax.validation.ValidationException;

public class CityWasNotFoundException extends ValidationException {

    public CityWasNotFoundException(String cityName) {
        super("City named " + cityName + " was not found!");
    }

    public CityWasNotFoundException(String cityName, String where) {
        super("City named " + cityName + " was not found in " + where + "!");
    }
}
