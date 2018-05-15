package ru.bellintegrator.weather.city.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "forecast")
public class Forecast {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "forecast_id")
  private Long forecastId;

  @Version
  private Long version;

  @Column(name = "distance_units")
  private String distanceUnits;

  @Column(name = "pressure_units")
  private String pressureUnits;

  @Column(name = "speed_units")
  private String speedUnits;

  @Column(name = "temperature_units")
  private String temperatureUnits;

  @Column(name = "chill_wind")
  private Long chillWind;

  @Column(name = "direction_wind")
  private Long directionWind;

  @Column(name = "speed_wind")
  private Double speedWind;

  @Column(name = "humidity_atmosphere")
  private Long humidityAtmosphere;

  @Column(name = "pressure_atmosphere")
  private Double pressureAtmosphere;

  @Column(name = "rising_atmosphere")
  private Long risingAtmosphere;

  @Column(name = "visibility_atmosphere")
  private Double visibilityAtmosphere;

  @Column(name = "sunrise_astronomy")
  private String sunriseAstronomy;

  @Column(name = "sunset_astronomy")
  private String sunsetAstronomy;

  @Column(name = "date_Condition")
  private String dateCondition;

  @Column(name = "temp_condition")
  private Long tempCondition;

  @Column(name = "text_condition")
  private String textCondition;

  @Column(name = "date_forecast")
  private String dateForecast;

  @Column(name = "day_forecast")
  private String dayForecast;

  @Column(name = "high_forecast")
  private Long highForecast;

  @Column(name = "low_forecast")
  private Long lowForecast;

  @Column(name = "text_forecast")
  private String textForecast;


  public Long getForecastId() {
    return forecastId;
  }

  public void setForecastId(Long forecastId) {
    this.forecastId = forecastId;
  }


  public String getDistanceUnits() {
    return distanceUnits;
  }

  public void setDistanceUnits(String distanceUnits) {
    this.distanceUnits = distanceUnits;
  }


  public String getPressureUnits() {
    return pressureUnits;
  }

  public void setPressureUnits(String pressureUnits) {
    this.pressureUnits = pressureUnits;
  }


  public String getSpeedUnits() {
    return speedUnits;
  }

  public void setSpeedUnits(String speedUnits) {
    this.speedUnits = speedUnits;
  }


  public String getTemperatureUnits() {
    return temperatureUnits;
  }

  public void setTemperatureUnits(String temperatureUnits) {
    this.temperatureUnits = temperatureUnits;
  }


  public Long getChillWind() {
    return chillWind;
  }

  public void setChillWind(Long chillWind) {
    this.chillWind = chillWind;
  }


  public Long getDirectionWind() {
    return directionWind;
  }

  public void setDirectionWind(Long directionWind) {
    this.directionWind = directionWind;
  }


  public Double getSpeedWind() {
    return speedWind;
  }

  public void setSpeedWind(Double speedWind) {
    this.speedWind = speedWind;
  }


  public Long getHumidityAtmosphere() {
    return humidityAtmosphere;
  }

  public void setHumidityAtmosphere(Long humidityAtmosphere) {
    this.humidityAtmosphere = humidityAtmosphere;
  }


  public Double getPressureAtmosphere() {
    return pressureAtmosphere;
  }

  public void setPressureAtmosphere(Double pressureAtmosphere) {
    this.pressureAtmosphere = pressureAtmosphere;
  }


  public Long getRisingAtmosphere() {
    return risingAtmosphere;
  }

  public void setRisingAtmosphere(Long risingAtmosphere) {
    this.risingAtmosphere = risingAtmosphere;
  }


  public Double getVisibilityAtmosphere() {
    return visibilityAtmosphere;
  }

  public void setVisibilityAtmosphere(Double visibilityAtmosphere) {
    this.visibilityAtmosphere = visibilityAtmosphere;
  }


  public String getSunriseAstronomy() {
    return sunriseAstronomy;
  }

  public void setSunriseAstronomy(String sunriseAstronomy) {
    this.sunriseAstronomy = sunriseAstronomy;
  }


  public String getSunsetAstronomy() {
    return sunsetAstronomy;
  }

  public void setSunsetAstronomy(String sunsetAstronomy) {
    this.sunsetAstronomy = sunsetAstronomy;
  }


  public String getDateCondition() {
    return dateCondition;
  }

  public void setDateCondition(String dateCondition) {
    this.dateCondition = dateCondition;
  }


  public Long getTempCondition() {
    return tempCondition;
  }

  public void setTempCondition(Long tempCondition) {
    this.tempCondition = tempCondition;
  }


  public String getTextCondition() {
    return textCondition;
  }

  public void setTextCondition(String textCondition) {
    this.textCondition = textCondition;
  }


  public String getDateForecast() {
    return dateForecast;
  }

  public void setDateForecast(String dateForecast) {
    this.dateForecast = dateForecast;
  }


  public String getDayForecast() {
    return dayForecast;
  }

  public void setDayForecast(String dayForecast) {
    this.dayForecast = dayForecast;
  }


  public Long getHighForecast() {
    return highForecast;
  }

  public void setHighForecast(Long highForecast) {
    this.highForecast = highForecast;
  }


  public Long getLowForecast() {
    return lowForecast;
  }

  public void setLowForecast(Long lowForecast) {
    this.lowForecast = lowForecast;
  }


  public String getTextForecast() {
    return textForecast;
  }

  public void setTextForecast(String textForecast) {
    this.textForecast = textForecast;
  }

}
