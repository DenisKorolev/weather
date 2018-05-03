package ru.bellintegrator.weather.city.controller;

import org.springframework.web.bind.annotation.PathVariable;
import ru.bellintegrator.weather.city.view.CityView;

public interface CityController {

    /**
     * Loads weather by city name
     * @param cityName City name
     * @return City weather view
     */
    CityView loadByName (@PathVariable(value = "cityName") String cityName);
}
