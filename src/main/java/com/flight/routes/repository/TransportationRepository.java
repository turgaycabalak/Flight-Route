package com.flight.routes.repository;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.domain.enums.TransportationTypeEnum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransportationRepository extends JpaRepository<Transportation, Long> {

  @Query("""
      SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END
          FROM Transportation t
              WHERE t.origin.id = :originLocationId
                AND t.destination.id = :destinationLocationId
                AND t.transportationType = :transportationTypeEnum
      """)
  boolean existsBy(Long originLocationId, Long destinationLocationId, TransportationTypeEnum transportationTypeEnum);

  boolean existsByOriginAndDestinationAndTransportationTypeAndIdNot(Location origin, Location destination,
                                                                    TransportationTypeEnum transportationType,
                                                                    Long id);
}
