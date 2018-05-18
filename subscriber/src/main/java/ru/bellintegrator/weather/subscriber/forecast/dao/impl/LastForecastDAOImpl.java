package ru.bellintegrator.weather.subscriber.forecast.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bellintegrator.weather.subscriber.forecast.dao.LastForecastDAO;
import ru.bellintegrator.weather.subscriber.forecast.model.LastForecast;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class LastForecastDAOImpl implements LastForecastDAO {

    @Autowired
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public LastForecast loadByLocationId(LastForecast forecastEntity) {

        Query query = em.createNativeQuery("SELECT lf.* FROM last_forecast lf WHERE location_id="
                + forecastEntity.getLocation().getLocationId() + " ORDER BY lf.created DESC", LastForecast.class);

        List<LastForecast> results = query.getResultList();
        LastForecast lastForecastResult = null;
        if (!results.isEmpty())
            lastForecastResult = results.get(0);

        return lastForecastResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(LastForecast lastForecast) {
        em.remove(lastForecast);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(LastForecast lastForecast) {
        em.persist(lastForecast);
    }
}
