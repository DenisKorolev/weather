package ru.bellintegrator.weather.subscriber.forecast.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bellintegrator.weather.subscriber.forecast.dao.ForecastDAO;
import ru.bellintegrator.weather.subscriber.forecast.model.ForecastEntity;

import javax.persistence.EntityManager;

@Repository
public class ForecastDAOImpl implements ForecastDAO {

    @Autowired
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(ForecastEntity forecast) {
        em.persist(forecast);
    }
}
