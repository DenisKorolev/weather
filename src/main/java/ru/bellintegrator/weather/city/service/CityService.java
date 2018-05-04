package ru.bellintegrator.weather.city.service;

public interface CityService {

    /**
     * Loads City weather by City name from Yahoo API and sends data to JMS Listener
     * @param cityName City name
     */
    void loadByName(String cityName);
}
