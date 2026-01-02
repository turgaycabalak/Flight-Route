package com.flight.routes.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "locations",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_location_code", columnNames = "location_code")
    }
)
public class Location {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String country;

  @Column(nullable = false)
  private String city;

  @Column(name = "location_code", nullable = false, length = 10)
  private String locationCode;

  protected Location() {
  }

  public Location(String name, String country, String city, String locationCode) {
    this.name = name;
    this.country = country;
    this.city = city;
    this.locationCode = locationCode;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCountry() {
    return country;
  }

  public String getCity() {
    return city;
  }

  public String getLocationCode() {
    return locationCode;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }
}
