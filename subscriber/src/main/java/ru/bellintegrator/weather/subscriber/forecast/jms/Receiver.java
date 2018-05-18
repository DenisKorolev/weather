package ru.bellintegrator.weather.subscriber.forecast.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weather.common.view.forecast.CityView;
import ru.bellintegrator.weather.subscriber.forecast.service.ForecastService;

@Component
public class Receiver {

    @Autowired
    private ForecastService service;

    @JmsListener(destination = "weather")
    public void receiveWeather(CityView view) {
        System.out.println("Weather forecast was received: " + view.getQuery().getCreated());
        service.saveForecast(view);
    }
}
