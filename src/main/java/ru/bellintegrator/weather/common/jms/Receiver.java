package ru.bellintegrator.weather.common.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weather.city.view.CityView;

@Component
public class Receiver {

    @JmsListener(destination = "weather")
    public void receiveWeather(CityView view) {
        System.out.println("Weather forecast was received");
    }
}
