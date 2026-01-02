package com.flight.routes.domain.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.flight.routes.domain.enums.TransportationTypeEnum;

@Entity
@Table(
    name = "transportations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_transportation_unique_route",
            columnNames = {
                "origin_location_id",
                "destination_location_id",
                "transportation_type"
            }
        )
    }
)
public class Transportation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "origin_location_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_transportation_origin")
  )
  private Location origin;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "destination_location_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_transportation_destination")
  )
  private Location destination;

  @Enumerated(EnumType.STRING)
  @Column(name = "transportation_type", nullable = false, length = 20)
  private TransportationTypeEnum transportationType;

  @ElementCollection
  @CollectionTable(
      name = "transportation_operating_days",
      joinColumns = @JoinColumn(
          name = "transportation_id",
          foreignKey = @ForeignKey(name = "fk_transportation_operating_days")
      )
  )
  @Column(name = "day_of_week", nullable = false)
  private Set<Integer> operatingDays = new HashSet<>();


  protected Transportation() {
  }

  public Transportation(Location origin, Location destination, TransportationTypeEnum transportationType) {
    if (origin == null || destination == null) {
      throw new IllegalArgumentException("Origin and destination cannot be null");
    }
    if (origin.getId().equals(destination.getId())) {
      throw new IllegalArgumentException("Origin and destination cannot be same");
    }
    if (transportationType == null) {
      throw new IllegalArgumentException("Transportation type cannot be null");
    }

    this.origin = origin;
    this.destination = destination;
    this.transportationType = transportationType;
  }

  public boolean operatesOn(int dayOfWeek) {
    return operatingDays == null || operatingDays.isEmpty()
        || operatingDays.contains(dayOfWeek);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Location getOrigin() {
    return origin;
  }

  public void setOrigin(Location origin) {
    this.origin = origin;
  }

  public Location getDestination() {
    return destination;
  }

  public void setDestination(Location destination) {
    this.destination = destination;
  }

  public Set<Integer> getOperatingDays() {
    return operatingDays;
  }

  public void setOperatingDays(Set<Integer> operatingDays) {
    this.operatingDays = operatingDays;
  }

  public TransportationTypeEnum getTransportationType() {
    return transportationType;
  }

  public void setTransportationType(TransportationTypeEnum transportationType) {
    this.transportationType = transportationType;
  }
}
