package ru.bellintegrator.weather.common.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weather.city.service.CityService;
import ru.bellintegrator.weather.city.view.CityView;

@Component
public class Receiver {
    @Autowired
    private CityService service;

    @JmsListener(destination = "weather")
    public void receiveWeather(CityView view) {
        System.out.println("Weather forecast was received");
        service.saveForecast(view);
    }
}
