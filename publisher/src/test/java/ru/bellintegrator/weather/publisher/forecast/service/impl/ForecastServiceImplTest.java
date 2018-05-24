package ru.bellintegrator.weather.publisher.forecast.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.bellintegrator.weather.common.exception.CityWasNotFoundException;
import ru.bellintegrator.weather.common.exception.ConnectionException;
import ru.bellintegrator.weather.common.exception.RequiredFieldIsNullException;
import ru.bellintegrator.weather.common.view.forecast.CityView;
import ru.bellintegrator.weather.publisher.forecast.service.ForecastService;
import javax.jms.JMSRuntimeException;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ForecastServiceImplTest {

    @MockBean
    private JmsTemplate jmsTemplateMock;

    @Autowired
    private ForecastService service;

    @Test(expected = RequiredFieldIsNullException.class)
    public void should_ThrowException_When_NullCityName() {

        service.loadByName(null);
    }

    @Test(expected = RequiredFieldIsNullException.class)
    public void should_ThrowException_When_EmptyCityName() {

        service.loadByName("");
    }

    @Test
    public void should_sendWeatherForecastToJms_When_correctCityName() {

        service.loadByName("Ufa");
        service.loadByName("Уфа");
    }

    @Test(expected = CityWasNotFoundException.class)
    public void should_ThrowException_When_CityDoesNotExist() {

        service.loadByName("Абв");
    }

    @Test(expected = CityWasNotFoundException.class)
    public void should_ThrowException_When_CityDoesNotHaveForecast() {

        service.loadByName("Антарктика");
    }

    @Test(expected = ConnectionException.class)
    public void should_ThrowException_When_WrongYahooApiUrl() {

        ReflectionTestUtils.setField(service, "baseURL", "wrong.url");
        service.loadByName("Test");
    }

    @Test(expected = ConnectionException.class)
    public void should_ThrowException_When_JmsServerIsOffline() {

        Mockito.doThrow(new JMSRuntimeException("Exception")).when(jmsTemplateMock).convertAndSend(Mockito.anyString(), Mockito.any(CityView.class));
        service.loadByName("Уфа");
    }
}