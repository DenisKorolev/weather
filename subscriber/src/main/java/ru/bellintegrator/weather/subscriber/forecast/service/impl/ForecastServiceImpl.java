package ru.bellintegrator.weather.subscriber.forecast.service.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private LocationDAO locationDAO;
    @Autowired
    private LastForecastDAO lastForecastDAO;
    @Autowired
    private ForecastDAO forecastDAO;

    @Value("${com.yahoo.api.url}")
    private String baseURL;

    private CityView queryToYahooApi(String cityName) {

        if (Strings.isNullOrEmpty(cityName)) {
            throw new RequiredFieldIsNullException(cityName);
        }

        String url = baseURL + "?q=select * from weather.forecast where woeid in " +
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

        if (Strings.isNullOrEmpty(cityName)) {
            throw new RequiredFieldIsNullException(cityName);
        }

        CityView result = this.queryToYahooApi(cityName);

        LocationEntity locationRes = this.loadLocationFromDB(result);

        if (locationRes == null)
            throw new CityWasNotFoundException(cityName, "DB");

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
            query.setCreated(String.valueOf(lastForecastRes.getCreated()));
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

                wind.setChill(String.valueOf(forecast.getChillWind()));
                wind.setDirection(String.valueOf(forecast.getDirectionWind()));
                wind.setSpeed(String.valueOf(forecast.getSpeedWind()));

                channel.setWind(wind);

                //Atmosphere
                Atmosphere atmosphere = new Atmosphere();

                atmosphere.setHumidity(String.valueOf(forecast.getHumidityAtmosphere()));
                atmosphere.setPressure(String.valueOf(forecast.getPressureAtmosphere()));
                atmosphere.setRising(String.valueOf(forecast.getRisingAtmosphere()));
                atmosphere.setVisibility(String.valueOf(forecast.getVisibilityAtmosphere()));

                channel.setAtmosphere(atmosphere);

                //Astronomy
                Astronomy astronomy = new Astronomy();

                astronomy.setSunrise(forecast.getSunriseAstronomy());
                astronomy.setSunset(forecast.getSunsetAstronomy());

                channel.setAstronomy(astronomy);

                //Condition
                Condition condition = new Condition();

                condition.setDate(forecast.getDateCondition());
                condition.setTemp(String.valueOf(forecast.getTempCondition()));
                condition.setText(forecast.getTextCondition());

                Item item = new Item();
                item.setCondition(condition);
                channel.setItem(item);

                //ForecastEntity
                Forecast forecastView = new ru.bellintegrator.weather.common.view.forecast.Forecast();
                List<Forecast> forecasts = Arrays.asList(forecastView);

                forecastView.setDate(forecast.getDateCondition());
                forecastView.setDay(forecast.getDayForecast());
                forecastView.setHigh(String.valueOf(forecast.getHighForecast()));
                forecastView.setLow(String.valueOf(forecast.getLowForecast()));
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
        else throw new CityWasNotFoundException(cityName, "DB");


        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void saveForecast(CityView view) {

        //Null check
        if ((view == null) || (view.getQuery() == null) || (view.getQuery().getResults() == null)
                || (view.getQuery().getResults().getChannel() == null)
                || (view.getQuery().getResults().getChannel().getLocation() == null)) {
            throw new RequiredFieldIsNullException("CityView");
        }

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
        Units units = channel.getUnits();
        if (units != null) {
            forecast.setDistanceUnits(units.getDistance());
            forecast.setPressureUnits(units.getPressure());
            forecast.setSpeedUnits(units.getSpeed());
            forecast.setTemperatureUnits(units.getTemperature());
        }

        //Wind
        Wind wind = channel.getWind();
        if (wind != null) {
            if (!Strings.isNullOrEmpty(wind.getChill())) {
                ValidationUtils.checkFieldOnNotLong(wind.getChill(), "chill_wind");
                forecast.setChillWind(Long.parseLong(wind.getChill()));
            }

            if (!Strings.isNullOrEmpty(wind.getDirection())) {
                ValidationUtils.checkFieldOnNotLong(wind.getDirection(), "direction_wind");
                forecast.setDirectionWind(Long.parseLong(wind.getDirection()));
            }

            if (!Strings.isNullOrEmpty(wind.getSpeed())) {
                ValidationUtils.checkFieldOnNotDouble(wind.getSpeed(), "speed_wind");
                forecast.setSpeedWind(Double.parseDouble(wind.getSpeed()));
            }
        }

        //Atmosphere
        Atmosphere atmosphere = channel.getAtmosphere();
        if (atmosphere != null) {
            if (!Strings.isNullOrEmpty(atmosphere.getHumidity())) {
                ValidationUtils.checkFieldOnNotLong(atmosphere.getHumidity(), "humidity_atmosphere");
                forecast.setHumidityAtmosphere(Long.parseLong(atmosphere.getHumidity()));
            }

            if (!Strings.isNullOrEmpty(atmosphere.getPressure())) {
                ValidationUtils.checkFieldOnNotDouble(atmosphere.getPressure(), "pressure_atmosphere");
                forecast.setPressureAtmosphere(Double.parseDouble(atmosphere.getPressure()));
            }

            if (!Strings.isNullOrEmpty(atmosphere.getRising())) {
                ValidationUtils.checkFieldOnNotLong(atmosphere.getRising(), "rising_atmosphere");
                forecast.setRisingAtmosphere(Long.parseLong(atmosphere.getRising()));
            }

            if (!Strings.isNullOrEmpty(atmosphere.getVisibility())) {
                ValidationUtils.checkFieldOnNotDouble(atmosphere.getVisibility(), "visibility_atmosphere");
                forecast.setVisibilityAtmosphere(Double.parseDouble(atmosphere.getVisibility()));
            }
        }

        //Astronomy
        Astronomy astronomy = channel.getAstronomy();
        if (astronomy != null) {
            forecast.setSunriseAstronomy(astronomy.getSunrise());
            forecast.setSunsetAstronomy(astronomy.getSunset());
        }

        Item item = channel.getItem();
        if (item != null) {
            //Condition
            Condition condition = item.getCondition();

            forecast.setDateCondition(condition.getDate());

            if (!Strings.isNullOrEmpty(condition.getTemp())) {
                ValidationUtils.checkFieldOnNotLong(condition.getTemp(), "temperature_condition");
                forecast.setTempCondition(Long.parseLong(condition.getTemp()));
            }

            forecast.setTextCondition(condition.getText());

            //ForecastEntity
            List<Forecast> forecastList = item.getForecasts();
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
