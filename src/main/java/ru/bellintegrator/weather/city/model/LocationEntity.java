package ru.bellintegrator.weather.city.model;



import javax.persistence.*;

@Entity
@Table(name = "location")
public class LocationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "location_id")
  private Long locationId;

  @Version
  private Long version;

  private String city;

  private String country;

  private String region;


  public long getLocationId() {
    return locationId;
  }

  public void setLocationId(long locationId) {
    this.locationId = locationId;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }


  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

}
