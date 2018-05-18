package ru.bellintegrator.weather.subscriber.forecast.controller;

import org.springframework.web.bind.annotation.PathVariable;
import ru.bellintegrator.weather.common.view.forecast.CityView;

public interface ForecastController {

    /**
     * Returns weather forecast by city name
     * @param cityName City name
     * @return City view
     */
    CityView returnByName (@PathVariable(value = "cityName") String cityName);
}
