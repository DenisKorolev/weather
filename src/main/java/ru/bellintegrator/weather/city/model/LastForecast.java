package ru.bellintegrator.weather.city.model;


import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "last_forecast")
public class LastForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "last_forecast_id")
    private Long lastForecastId;

    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "forecast_id")
    private Forecast forecast;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    private Instant created;

    public Long getLastForecastId() {
        return lastForecastId;
    }

    public void setLastForecastId(Long lastForecastId) {
        this.lastForecastId = lastForecastId;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }


    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}