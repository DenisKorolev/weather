package ru.bellintegrator.weather.publisher.forecast.service;

public interface ForecastService {

    /**
     * Loads City weather forecast by a City name from Yahoo API and sends data to the JMS Topic
     * @param cityName City name
     */
    void loadByName(String cityName);
}
