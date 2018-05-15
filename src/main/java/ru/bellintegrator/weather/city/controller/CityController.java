package ru.bellintegrator.weather.city.controller;

import org.springframework.web.bind.annotation.PathVariable;
import ru.bellintegrator.weather.city.view.CityView;
import ru.bellintegrator.weather.city.view.Query;
import ru.bellintegrator.weather.common.view.ResultView;

public interface CityController {

    /**
     * Loads weather by city name
     * @param cityName City name
     * @return Query result
     */
    ResultView loadByName (@PathVariable(value = "cityName") String cityName);

    /**
     * Returns weather forecast by city name
     * @param cityName City name
     * @return City view
     */
    CityView returnByName (@PathVariable(value = "cityName") String cityName);
}
