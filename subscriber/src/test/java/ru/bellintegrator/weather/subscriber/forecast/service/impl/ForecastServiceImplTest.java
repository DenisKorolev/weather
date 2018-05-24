package ru.bellintegrator.weather.subscriber.forecast.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.bellintegrator.weather.common.exception.CityWasNotFoundException;
import ru.bellintegrator.weather.common.exception.ConnectionException;
import ru.bellintegrator.weather.common.exception.RequiredFieldIsNullException;
import ru.bellintegrator.weather.subscriber.forecast.dao.ForecastDAO;
import ru.bellintegrator.weather.subscriber.forecast.dao.LastForecastDAO;
import ru.bellintegrator.weather.subscriber.forecast.dao.LocationDAO;
import ru.bellintegrator.weather.subscriber.forecast.model.LastForecast;
import ru.bellintegrator.weather.subscriber.forecast.model.LocationEntity;
import ru.bellintegrator.weather.subscriber.forecast.service.ForecastService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ForecastServiceImplTest {

    @MockBean
    private LocationDAO locationDAOMock;

    @MockBean
    private LastForecastDAO lastForecastDAOMock;

    @MockBean
    private ForecastDAO forecastDAOMock;

    @Autowired
    private ForecastService service;


    @Test(expected = RequiredFieldIsNullException.class)
    public void should_ThrowException_When_NullCityName() {

        service.returnByName(null);
    }

    @Test(expected = RequiredFieldIsNullException.class)
    public void should_ThrowException_When_EmptyCityName() {

        service.returnByName("");
    }

    @Test(expected = CityWasNotFoundException.class)
    public void should_ThrowException_When_CityDoesNotExist() {

        service.returnByName("Абв");
    }

    @Test(expected = CityWasNotFoundException.class)
    public void should_ThrowException_When_CityDoesNotHaveForecast() {

        service.returnByName("Антарктика");
    }

    @Test(expected = ConnectionException.class)
    public void should_ThrowException_When_WrongYahooApiUrl() {

        ReflectionTestUtils.setField(service, "baseURL", "wrong.url");
        service.returnByName("Test");
    }

    @Test(expected = CityWasNotFoundException.class)
    public void should_ThrowException_When_CityIsNotInDb() {

        Mockito.when(locationDAOMock.loadByLocation(Mockito.any(LocationEntity.class))).thenReturn(null);
        service.returnByName("Уфа");
    }

    @Test(expected = CityWasNotFoundException.class)
    public void should_ThrowException_When_CityLastForecastIsNotInDb() {

        Mockito.when(lastForecastDAOMock.loadByLocationId(Mockito.any(LastForecast.class))).thenReturn(null);
        service.returnByName("Уфа");
    }

    @Test(expected = RequiredFieldIsNullException.class)
    public void should_ThrowException_When_CityViewIsNull() {

        service.saveForecast(null);
    }


}