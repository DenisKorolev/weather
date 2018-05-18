package ru.bellintegrator.weather.subscriber.forecast.dao;

import ru.bellintegrator.weather.subscriber.forecast.model.ForecastEntity;

public interface ForecastDAO {

    /**
     * Adds ForecastEntity object to DB
     * @param forecast ForecastEntity object to save
     */
    void save(ForecastEntity forecast);
}
