package ru.bellintegrator.weather.city.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bellintegrator.weather.city.dao.ForecastDAO;
import ru.bellintegrator.weather.city.model.Forecast;

import javax.persistence.EntityManager;

@Repository
public class ForecastDAOImpl implements ForecastDAO {

    @Autowired
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Forecast forecast) {
        em.persist(forecast);
    }
}
