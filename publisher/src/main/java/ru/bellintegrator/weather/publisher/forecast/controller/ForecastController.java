package ru.bellintegrator.weather.publisher.forecast.controller;

import org.springframework.web.bind.annotation.PathVariable;
import ru.bellintegrator.weather.common.view.ResultView;

public interface ForecastController {

    /**
     * Loads weather by city name
     * @param cityName City name
     * @return Query result
     */
    ResultView loadByName (@PathVariable(value = "cityName") String cityName);
}
