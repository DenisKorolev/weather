package ru.bellintegrator.weather.city.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.bellintegrator.weather.city.controller.CityController;
import ru.bellintegrator.weather.city.view.CityView;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/city", produces = APPLICATION_JSON_VALUE)
public class CityControllerImpl implements CityController {

    /**
     * {@inheritDoc}
     */
    @Override
    @ApiOperation(value = "Loads City weather by name", httpMethod = "GET")
    @RequestMapping(value = "/{cityName}", method = {GET})
    public CityView loadByName(@PathVariable(value = "cityName") String cityName) {
        return null;
    }
}
