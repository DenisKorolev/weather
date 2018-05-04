package ru.bellintegrator.weather.city.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.bellintegrator.weather.city.controller.CityController;
import ru.bellintegrator.weather.city.service.CityService;
import ru.bellintegrator.weather.city.view.CityView;
import ru.bellintegrator.weather.common.view.ResultView;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/city", produces = APPLICATION_JSON_VALUE)
public class CityControllerImpl implements CityController {

    @Autowired
    private CityService service;

    /**
     * {@inheritDoc}
     */
    @Override
    @ApiOperation(value = "Loads City weather by name from Yahoo", httpMethod = "GET")
    @RequestMapping(value = "/{cityName}", method = {GET})
    public ResultView loadByName(@PathVariable(value = "cityName") String cityName) {

        service.loadByName(cityName);



        return new ResultView("success");
    }
}
