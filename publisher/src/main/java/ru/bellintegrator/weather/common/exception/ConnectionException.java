package ru.bellintegrator.weather.common.exception;

import javax.validation.ValidationException;

public class ConnectionException extends ValidationException {
    public ConnectionException(String serviceName) {
        super(serviceName + " problems were occurred!");
    }
}
