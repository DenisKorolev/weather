package ru.bellintegrator.weather.subscriber.forecast.dao;

import ru.bellintegrator.weather.subscriber.forecast.model.LocationEntity;

public interface LocationDAO {

    /**
     * Gets Location object from DB by city, country, region
     * @param location Location object with parameters to query
     * @return Location object from DB
     */
    LocationEntity loadByLocation(LocationEntity location);

    /**
     * Saves Location information in DB
     * @param location Location object to save
     */
    void save(LocationEntity location);
}
