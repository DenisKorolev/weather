package ru.bellintegrator.weather.subscriber.forecast.dao;

import ru.bellintegrator.weather.subscriber.forecast.model.LastForecast;

public interface LastForecastDAO {

    /**
     * Gets LastForecast object from DB by Location id
     * @param lastForecast LastForecast object with Location id
     * @return LastForecast object from DB
     */
    LastForecast loadByLocationId(LastForecast lastForecast);

    /**
     * Deletes LastForecast object from DB
     * @param lastForecast LastForecast object to delete
     */
    void delete(LastForecast lastForecast);

    /**
     *Adds LastForecast object to DB
     * @param lastForecast LastForecast object to save
     */
    void save(LastForecast lastForecast);
}
