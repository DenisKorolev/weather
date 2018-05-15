package ru.bellintegrator.weather.city.dao;

import ru.bellintegrator.weather.city.model.Forecast;

public interface ForecastDAO {

    /**
     * Adds Forecast object to DB
     * @param forecast Forecast object to save
     */
    void save(Forecast forecast);
}
