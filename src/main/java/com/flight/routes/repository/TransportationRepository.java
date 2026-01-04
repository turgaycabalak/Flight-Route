package com.flight.routes.repository;

import java.util.List;
import java.util.Set;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.domain.enums.TransportationTypeEnum;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


  /// -- V1 -- //////////////////////////
  @Query("""
      SELECT t FROM Transportation t
      JOIN FETCH t.origin
      JOIN FETCH t.destination
      WHERE :day MEMBER OF t.operatingDays
      """)
  List<Transportation> findAllOperatingOnDay(@Param("day") Integer day);


  /// -- V2 -- //////////////////////////
  @EntityGraph(attributePaths = {"origin", "destination"})
  @Query("""
          SELECT t FROM Transportation t
          WHERE t.origin = :origin
          AND :day MEMBER OF t.operatingDays
      """)
  List<Transportation> findByOriginAndDay(@Param("origin") Location origin, @Param("day") Integer day);

  @EntityGraph(attributePaths = {"origin", "destination"})
  @Query("""
          SELECT t FROM Transportation t
          WHERE t.origin = :origin
          AND t.transportationType = :type
          AND :day MEMBER OF t.operatingDays
      """)
  List<Transportation> findByOriginAndTypeAndDay(@Param("origin") Location origin,
                                                 @Param("type") TransportationTypeEnum type,
                                                 @Param("day") Integer day);

  @EntityGraph(attributePaths = {"origin", "destination"})
  @Query("""
          SELECT t FROM Transportation t
          WHERE t.origin = :origin
          AND t.transportationType <> :type
          AND :day MEMBER OF t.operatingDays
      """)
  List<Transportation> findByOriginAndTypeNotAndDay(@Param("origin") Location origin,
                                                    @Param("type") TransportationTypeEnum type,
                                                    @Param("day") Integer day);


  /// -- V3 -- //////////////////////////
  @EntityGraph(attributePaths = {"origin", "destination"})
  @Query("""
      SELECT t FROM Transportation t
            WHERE :operatingDay MEMBER OF t.operatingDays
                  AND (t.origin.id = :originId
                        OR
                        t.destination.id = :destinationId)
      """)
  List<Transportation> getTransportations(@Param("originId") Long originId,
                                          @Param("destinationId") Long destinationId,
                                          @Param("operatingDay") Integer operatingDay);

  @EntityGraph(attributePaths = {"origin", "destination"})
  @Query("""
      SELECT t FROM Transportation t
      WHERE t.transportationType = 'FLIGHT'
      AND :operatingDay MEMBER OF t.operatingDays
      AND t.origin.id IN :originIds
      AND t.destination.id IN :destIds
      """)
  List<Transportation> findMiddleFlights(@Param("originIds") Set<Long> originIds,
                                         @Param("destIds") Set<Long> destIds,
                                         @Param("operatingDay") Integer operatingDay);
}
