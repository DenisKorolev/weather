package ru.bellintegrator.weather.publisher.forecast.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bellintegrator.weather.common.view.ResultView;
import ru.bellintegrator.weather.publisher.forecast.controller.ForecastController;
import ru.bellintegrator.weather.publisher.forecast.service.ForecastService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/forecast", produces = APPLICATION_JSON_VALUE)
public class ForecastControllerImpl implements ForecastController {

    @Autowired
    private ForecastService service;

    /**
     * {@inheritDoc}
     */
    @Override
    @ApiOperation(value = "Loads City weather forecast by name from Yahoo to DB", httpMethod = "GET")
    @RequestMapping(value = "/load/{cityName}", method = {GET})
    public ResultView loadByName(@PathVariable(value = "cityName") String cityName) {

        service.loadByName(cityName);

        return new ResultView("success");
    }
}
