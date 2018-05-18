package ru.bellintegrator.weather.publisher.forecast.service.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.bellintegrator.weather.common.exception.CityWasNotFoundException;
import ru.bellintegrator.weather.common.exception.ConnectionException;
import ru.bellintegrator.weather.common.exception.RequiredFieldIsNullException;
import ru.bellintegrator.weather.common.view.forecast.CityView;
import ru.bellintegrator.weather.common.view.forecast.Location;
import ru.bellintegrator.weather.publisher.forecast.service.ForecastService;

@Service
public class ForecastServiceImpl implements ForecastService {

    @Autowired
    private JmsTemplate jmsTemplate;

    private CityView queryToYahooApi(String cityName) {

        if (Strings.isNullOrEmpty(cityName)) {
            throw new RequiredFieldIsNullException(cityName);
        }

        String url = "https://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where woeid in " +
                "(select woeid from geo.places(1) where text=\"" + cityName + "\") and u=\"c\"" +
                "&format=json&env=store://datatables.org/alltableswithkeys";
        System.out.println(url);

        //Query to Yahoo API
        RestTemplate restTemplate = new RestTemplate();
        CityView result = null;
        try {
            result = restTemplate.getForObject(url, CityView.class);
        }
        catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            throw new ConnectionException("Yahoo API");
        }


        //Checks if city found
        if ((result == null) || (result.getQuery() == null) || (result.getQuery().getCount() == null)
                || (result.getQuery().getCount() < 1) || (result.getQuery().getResults() == null)
                || (result.getQuery().getResults().getChannel() == null)
                || (result.getQuery().getResults().getChannel().getLocation() == null)) {
            throw new CityWasNotFoundException(cityName, "Yahoo API");
        }
        else {
            Location location = result.getQuery().getResults().getChannel().getLocation();
            if ((Strings.isNullOrEmpty(location.getCity())) || (Strings.isNullOrEmpty(location.getCountry()))
                    || (Strings.isNullOrEmpty(location.getRegion())))
                throw new CityWasNotFoundException(cityName, "Yahoo API");
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadByName(String cityName) {

        if (Strings.isNullOrEmpty(cityName)) {
            throw new RequiredFieldIsNullException(cityName);
        }

        CityView result = this.queryToYahooApi(cityName);

        //Sends JSON Response to JMS Topic
        try {
            jmsTemplate.convertAndSend("weather", result);
        }
        catch (Exception ex) {
            throw new ConnectionException("JMS Server");
        }
        System.err.println("Weather forecast was sent");
    }
}
