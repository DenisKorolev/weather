package ru.bellintegrator.weather.subscriber.forecast.service.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.bellintegrator.weather.common.exception.CityWasNotFoundException;
import ru.bellintegrator.weather.common.exception.ConnectionException;
import ru.bellintegrator.weather.common.exception.RequiredFieldIsNullException;
import ru.bellintegrator.weather.common.util.ValidationUtils;
import ru.bellintegrator.weather.common.view.forecast.*;
import ru.bellintegrator.weather.subscriber.forecast.dao.ForecastDAO;
import ru.bellintegrator.weather.subscriber.forecast.dao.LastForecastDAO;
import ru.bellintegrator.weather.subscriber.forecast.dao.LocationDAO;
import ru.bellintegrator.weather.subscriber.forecast.model.ForecastEntity;
import ru.bellintegrator.weather.subscriber.forecast.model.LastForecast;
import ru.bellintegrator.weather.subscriber.forecast.model.LocationEntity;
import ru.bellintegrator.weather.subscriber.forecast.service.ForecastService;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class ForecastServiceImpl implements ForecastService {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private LocationDAO locationDAO;
    @Autowired
    private LastForecastDAO lastForecastDAO;
    @Autowired
    private ForecastDAO forecastDAO;

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

    private LocationEntity loadLocationFromDB(CityView view) {

        //Checks if location exists in DB
        Location location = view.getQuery().getResults().getChannel().getLocation();
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setCity(location.getCity());
        locationEntity.setCountry(location.getCountry());
        locationEntity.setRegion(location.getRegion());

        LocationEntity locationRes = locationDAO.loadByLocation(locationEntity);

        return locationRes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CityView returnByName(String cityName) {

        CityView result = this.queryToYahooApi(cityName);

        LocationEntity locationRes = this.loadLocationFromDB(result);

        if (locationRes == null)
            new CityWasNotFoundException(cityName, "DB");

        //Loads LastForecast from DB by location_id
        System.err.println("Last ForecastEntity search has started");
        LastForecast lastForecastQuery = new LastForecast();
        lastForecastQuery.setLocation(locationRes);
        LastForecast lastForecastRes = lastForecastDAO.loadByLocationId(lastForecastQuery);
        System.err.println("Last ForecastEntity search has finished");

        CityView view = new CityView();

        //If LastForecast exists, maps data from tables last_forecast and forecast to the view
        if (lastForecastRes != null) {
            //LastForecast created
            Query query = new Query();
            query.setCreated(lastForecastRes.getCreated().toString());
            view.setQuery(query);

            Results results = new Results();
            query.setResults(results);
            Channel channel = new Channel();
            results.setChannel(channel);

            //ForecastEntity
            ForecastEntity forecast = lastForecastRes.getForecast();
            if (forecast != null) {
                //Units
                Units units = new Units();

                units.setDistance(forecast.getDistanceUnits());
                units.setPressure(forecast.getPressureUnits());
                units.setSpeed(forecast.getSpeedUnits());
                units.setTemperature(forecast.getTemperatureUnits());

                channel.setUnits(units);

                //Wind
                Wind wind = new Wind();

                wind.setChill(forecast.getChillWind().toString());
                wind.setDirection(forecast.getDirectionWind().toString());
                wind.setSpeed(forecast.getSpeedWind().toString());

                channel.setWind(wind);

                //Atmosphere
                Atmosphere atmosphere = new Atmosphere();

                atmosphere.setHumidity(forecast.getHumidityAtmosphere().toString());
                atmosphere.setPressure(forecast.getPressureAtmosphere().toString());
                atmosphere.setRising(forecast.getRisingAtmosphere().toString());
                atmosphere.setVisibility(forecast.getVisibilityAtmosphere().toString());

                channel.setAtmosphere(atmosphere);

                //Astronomy
                Astronomy astronomy = new Astronomy();

                astronomy.setSunrise(forecast.getSunriseAstronomy());
                astronomy.setSunset(forecast.getSunsetAstronomy());

                channel.setAstronomy(astronomy);

                //Condition
                Condition condition = new Condition();

                condition.setDate(forecast.getDateCondition());
                condition.setTemp(forecast.getTempCondition().toString());
                condition.setText(forecast.getTextCondition());

                Item item = new Item();
                item.setCondition(condition);
                channel.setItem(item);

                //ForecastEntity
                Forecast forecastView = new ru.bellintegrator.weather.common.view.forecast.Forecast();
                List<Forecast> forecasts = Arrays.asList(forecastView);

                forecastView.setDate(forecast.getDateCondition());
                forecastView.setDay(forecast.getDayForecast());
                forecastView.setHigh(forecast.getHighForecast().toString());
                forecastView.setLow(forecast.getLowForecast().toString());
                forecastView.setText(forecast.getTextForecast());

                channel.getItem().setForecasts(forecasts);
            }

            //Location
            LocationEntity locationEntity = lastForecastRes.getLocation();
            if (locationEntity != null) {
                Location location = new Location();

                location.setCity(locationEntity.getCity());
                location.setCountry(locationEntity.getCountry());
                location.setRegion(locationEntity.getRegion());

                channel.setLocation(location);
            }

        }


        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void saveForecast(CityView view) {

        //Checks if location exists in DB
        Location location = view.getQuery().getResults().getChannel().getLocation();
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setCity(location.getCity());
        locationEntity.setCountry(location.getCountry());
        locationEntity.setRegion(location.getRegion());

        LocationEntity locationRes = locationDAO.loadByLocation(locationEntity);

        //If Location doesn't exist in DB, adds it
        if (locationRes == null) {
            locationDAO.save(locationEntity);
            locationRes = locationEntity;
        }

        //Loads LastForecast from DB by location_id
        System.err.println("Last ForecastEntity search has started");
        LastForecast lastForecastQuery = new LastForecast();
        lastForecastQuery.setLocation(locationRes);
        LastForecast lastForecastRes = lastForecastDAO.loadByLocationId(lastForecastQuery);
        System.err.println("Last ForecastEntity search has finished");

        if (lastForecastRes != null){
            Instant dbInstant = lastForecastRes.getCreated();
            Instant queryInstant;
            if (Strings.isNullOrEmpty(view.getQuery().getCreated()))
                queryInstant = Instant.now();
            else
                queryInstant = Instant.parse(view.getQuery().getCreated());
            //If LastForecast in DB is older than current, LastForecast and ForecastEntity are deleted
            if (queryInstant.compareTo(dbInstant) > 0){
                lastForecastDAO.delete(lastForecastRes);
            }
            else return;
        }

        //Adds current LastForecast and ForecastEntity objects to DB
        ForecastEntity forecast = new ForecastEntity();
        Channel channel = view.getQuery().getResults().getChannel();

        //Units
        forecast.setDistanceUnits(channel.getUnits().getDistance());
        forecast.setPressureUnits(channel.getUnits().getPressure());
        forecast.setSpeedUnits(channel.getUnits().getSpeed());
        forecast.setTemperatureUnits(channel.getUnits().getTemperature());

        //Wind
        if (!Strings.isNullOrEmpty(channel.getWind().getChill())){
            ValidationUtils.checkFieldOnNotLong(channel.getWind().getChill(), "chill_wind");
            forecast.setChillWind(Long.parseLong(channel.getWind().getChill()));
        }

        if (!Strings.isNullOrEmpty(channel.getWind().getDirection())){
            ValidationUtils.checkFieldOnNotLong(channel.getWind().getDirection(), "direction_wind");
            forecast.setDirectionWind(Long.parseLong(channel.getWind().getDirection()));
        }

        if (!Strings.isNullOrEmpty(channel.getWind().getSpeed())){
            ValidationUtils.checkFieldOnNotDouble(channel.getWind().getSpeed(), "speed_wind");
            forecast.setSpeedWind(Double.parseDouble(channel.getWind().getSpeed()));
        }

        //Atmosphere
        if (!Strings.isNullOrEmpty(channel.getAtmosphere().getHumidity())){
            ValidationUtils.checkFieldOnNotLong(channel.getAtmosphere().getHumidity(), "humidity_atmosphere");
            forecast.setHumidityAtmosphere(Long.parseLong(channel.getAtmosphere().getHumidity()));
        }

        if (!Strings.isNullOrEmpty(channel.getAtmosphere().getPressure())){
            ValidationUtils.checkFieldOnNotDouble(channel.getAtmosphere().getPressure(), "pressure_atmosphere");
            forecast.setPressureAtmosphere(Double.parseDouble(channel.getAtmosphere().getPressure()));
        }

        if (!Strings.isNullOrEmpty(channel.getAtmosphere().getRising())){
            ValidationUtils.checkFieldOnNotLong(channel.getAtmosphere().getRising(), "rising_atmosphere");
            forecast.setRisingAtmosphere(Long.parseLong(channel.getAtmosphere().getRising()));
        }

        if (!Strings.isNullOrEmpty(channel.getAtmosphere().getVisibility())){
            ValidationUtils.checkFieldOnNotDouble(channel.getAtmosphere().getVisibility(), "visibility_atmosphere");
            forecast.setVisibilityAtmosphere(Double.parseDouble(channel.getAtmosphere().getVisibility()));
        }

        //Astronomy
        forecast.setSunriseAstronomy(channel.getAstronomy().getSunrise());
        forecast.setSunsetAstronomy(channel.getAstronomy().getSunset());

        //Condition
        forecast.setDateCondition(channel.getItem().getCondition().getDate());

        if (!Strings.isNullOrEmpty(channel.getItem().getCondition().getTemp())){
            ValidationUtils.checkFieldOnNotLong(channel.getItem().getCondition().getTemp(), "temperature_condition");
            forecast.setTempCondition(Long.parseLong(channel.getItem().getCondition().getTemp()));
        }

        forecast.setTextCondition(channel.getItem().getCondition().getText());

        //ForecastEntity
        List<Forecast> forecastList = channel.getItem().getForecasts();
        if ((forecastList != null) && (!forecastList.isEmpty())) {

            forecast.setDateForecast(forecastList.get(0).getDate());

            forecast.setDayForecast(forecastList.get(0).getDay());

            if (!Strings.isNullOrEmpty(forecastList.get(0).getHigh())) {
                ValidationUtils.checkFieldOnNotLong(forecastList.get(0).getHigh(), "high_forecast");
                forecast.setHighForecast(Long.parseLong(forecastList.get(0).getHigh()));
            }

            if (!Strings.isNullOrEmpty(forecastList.get(0).getLow())) {
                ValidationUtils.checkFieldOnNotLong(forecastList.get(0).getLow(), "low_forecast");
                forecast.setLowForecast(Long.parseLong(forecastList.get(0).getLow()));
            }

            forecast.setTextForecast(forecastList.get(0).getText());
        }

        forecastDAO.save(forecast);

        LastForecast lastForecast = new LastForecast();
        lastForecast.setForecast(forecast);
        lastForecast.setLocation(locationRes);

        ValidationUtils.checkFieldOnNotInstant(view.getQuery().getCreated(), "created");
        lastForecast.setCreated(Instant.parse(view.getQuery().getCreated()));
        lastForecastDAO.save(lastForecast);

        System.err.println("ForecastEntity was successfully saved");
    }


}
