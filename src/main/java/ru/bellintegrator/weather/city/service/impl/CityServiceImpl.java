package ru.bellintegrator.weather.city.service.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.bellintegrator.weather.city.service.CityService;
import ru.bellintegrator.weather.city.view.CityView;
import ru.bellintegrator.weather.common.exception.CityWasNotFoundException;
import ru.bellintegrator.weather.common.exception.RequiredFieldIsNullException;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadByName(String cityName) {

        if (Strings.isNullOrEmpty(cityName)) {
            throw new RequiredFieldIsNullException(cityName);
        }

        String url = "https://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where woeid in " +
                "(select woeid from geo.places(1) where text=\"" + cityName + "\")&format=json&env=store://datatables.org/alltableswithkeys";
        System.out.println(url);

        RestTemplate restTemplate = new RestTemplate();
        CityView result = restTemplate.getForObject(url, CityView.class);

        if (result.getQuery().getCount() < 1)
            throw new CityWasNotFoundException(cityName);

        jmsTemplate.convertAndSend("weather", result);
        System.out.println("Weather forecast was sent");
    }
}
