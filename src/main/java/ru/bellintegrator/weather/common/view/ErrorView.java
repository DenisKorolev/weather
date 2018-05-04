package ru.bellintegrator.weather.common.view;

public class ErrorView {

    private String error;

    /**
     * Constructor for Jackson
     */
    public ErrorView(){

    }

    public ErrorView(String error){
        setError(error);
    }

    @Override
    public String toString() {
        return "{error:" + getError() + "}";
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
