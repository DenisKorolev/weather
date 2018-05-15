package ru.bellintegrator.weather.city.service;

import ru.bellintegrator.weather.city.view.CityView;

public interface CityService {

    /**
     * Loads City weather forecast by City name from Yahoo API and sends data to JMS Listener
     * @param cityName City name
     */
    void loadByName(String cityName);

    /**
     * Returns City weather forecast by City name from DB
     * @param cityName City name
     * @return City view
     */
    CityView returnByName(String cityName);

    /**
     * Saves weather forecast to DB
     * @param view City weather forecast view
     */
    void saveForecast(CityView view);
}
