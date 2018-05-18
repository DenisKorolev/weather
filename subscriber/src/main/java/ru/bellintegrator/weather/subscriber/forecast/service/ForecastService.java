package ru.bellintegrator.weather.subscriber.forecast.service;

import ru.bellintegrator.weather.common.view.forecast.CityView;

public interface ForecastService {

    /**
     * Returns City weather forecast by a City name from DB
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
