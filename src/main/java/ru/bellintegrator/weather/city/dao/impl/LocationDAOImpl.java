package ru.bellintegrator.weather.city.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bellintegrator.weather.city.dao.LocationDAO;
import ru.bellintegrator.weather.city.model.LocationEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class LocationDAOImpl implements LocationDAO {

    @Autowired
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationEntity loadByLocation(LocationEntity location) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LocationEntity> criteria = builder.createQuery(LocationEntity.class);

        Root<LocationEntity> locationEntityRoot = criteria.from(LocationEntity.class);
        criteria.where(builder.and(
                builder.equal(locationEntityRoot.get("city"), location.getCity()),
                builder.equal(locationEntityRoot.get("country"), location.getCountry()),
                builder.equal(locationEntityRoot.get("region"), location.getRegion())
            )
        );

        TypedQuery<LocationEntity> query = em.createQuery(criteria);
        List<LocationEntity> results = query.getResultList();
        LocationEntity locationEntity = null;
        if (!results.isEmpty())
            locationEntity = results.get(0);

        return locationEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(LocationEntity location) {
        em.persist(location);
    }
}
