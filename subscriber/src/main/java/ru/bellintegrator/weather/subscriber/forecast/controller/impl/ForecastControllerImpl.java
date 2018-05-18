package ru.bellintegrator.weather.subscriber.forecast.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bellintegrator.weather.common.view.forecast.CityView;
import ru.bellintegrator.weather.subscriber.forecast.controller.ForecastController;
import ru.bellintegrator.weather.subscriber.forecast.service.ForecastService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/city", produces = APPLICATION_JSON_VALUE)
public class ForecastControllerImpl implements ForecastController {

    @Autowired
    private ForecastService service;

    /**
     * {@inheritDoc}
     */
    @Override
    @ApiOperation(value = "Returns City weather forecast by name from DB", httpMethod = "GET")
    @RequestMapping(value = "/return/{cityName}", method = {GET})
    public CityView returnByName(@PathVariable(value = "cityName") String cityName) {

        return service.returnByName(cityName);
    }
}
