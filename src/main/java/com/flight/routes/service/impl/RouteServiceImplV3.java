package com.flight.routes.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.domain.enums.TransportationTypeEnum;
import com.flight.routes.dto.route.RouteLegResponse;
import com.flight.routes.dto.route.RouteResponse;
import com.flight.routes.repository.TransportationRepository;
import com.flight.routes.service.RouteService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("v3")
@Transactional(readOnly = true)
public class RouteServiceImplV3 implements RouteService {
  private final TransportationRepository transportationRepository;

  public RouteServiceImplV3(TransportationRepository transportationRepository) {
    this.transportationRepository = transportationRepository;
  }

  @Override
  public List<RouteResponse> calculateRoutes(Long originId, Long destinationId, LocalDate date) {
    if (originId == null || destinationId == null || originId.equals(destinationId)) {
      return List.of();
    }

    // A -> B (Flight)
    // A -> X (Bus), X -> B (Flight)
    // A -> X (Flight), X -> B (Bus)
    // A -> X (Bus), X -> Y (Flight), Y -> B (Bus)

    int operatingDay = date.getDayOfWeek().getValue();

    // All edges. Not including middle legs, ?-?
    // [A-?... and ?-B...]
    List<Transportation> edgeTransportations =
        transportationRepository.getTransportations(originId, destinationId, operatingDay);

    if (edgeTransportations.isEmpty()) {
      return List.of();
    }

    // starter: All transportations that start from 'A'
    // potentialFlight**: for possible middle transportations, all origin and destination ids (X-Y, X-Z, K-L, etc.)
    List<Transportation> starters = new ArrayList<>(); // A-?
    //List<Transportation> enders = new ArrayList<>(); // ?-B
    Set<Long> potentialFlightOrigins = new HashSet<>(); // destinations of t: B, X, Y, Z...
    Set<Long> potentialFlightDestinations = new HashSet<>(); // origins of t: B, X, Y, Z...

    for (Transportation t : edgeTransportations) {
      // Those whose origin is 'A' which means A-?
      // A-B, A-X, A-Y, A-Z...
      boolean itStartsWithOrigin = t.getOrigin().getId().equals(originId);
      if (itStartsWithOrigin) {
        starters.add(t); // A-?
        potentialFlightOrigins.add(t.getDestination().getId()); // destinations of t: B, X, Y, Z...
      }

      // Those whose destination is 'B' which means ?-B
      // A-B, X-B, Y-B, Z-B...
      boolean itEndsWithDestination = t.getDestination().getId().equals(destinationId);
      if (itEndsWithDestination) {
        //enders.add(t); // ?-B
        potentialFlightDestinations.add(t.getOrigin().getId()); // origins of t: B, X, Y, Z...
      }
    }

    // Middle flights: X-Y, X-Z, K-L, etc.
    List<Transportation> middleFlights = new ArrayList<>();
    if (!potentialFlightOrigins.isEmpty() && !potentialFlightDestinations.isEmpty()) {
      middleFlights = transportationRepository.findMiddleFlights(
          potentialFlightOrigins,
          potentialFlightDestinations,
          operatingDay
      );
    }

    // Merge edgeTransportations and middleFlights
    // Group by originId
    // k: originId, v: List<Transportation>
    Map<Long, List<Transportation>> transportMap = Stream.concat(edgeTransportations.stream(), middleFlights.stream())
        .collect(Collectors.groupingBy(t -> t.getOrigin().getId()));

    List<RouteResponse> validRoutes = new ArrayList<>();
    // Loop through all starters that start from the origin 'A'
    for (Transportation leg1 : starters) {

      // If the first leg is FLIGHT
      boolean flight = leg1.getTransportationType() == TransportationTypeEnum.FLIGHT;
      if (flight) {
        // 1) Direct FLIGHT
        boolean directFlight = leg1.getDestination().getId().equals(destinationId);
        if (directFlight) {
          validRoutes.add(createRoute(leg1));
        }

        // 2) FLIGHT + AFTER
        List<Transportation> nextLegs = transportMap.getOrDefault(leg1.getDestination().getId(), List.of());
        for (Transportation leg2 : nextLegs) {
          // - non-FLIGHT
          // - starts from X
          // - ends at B
          boolean itIsTransportGoesToDestination = leg2.getTransportationType() != TransportationTypeEnum.FLIGHT
              && leg2.getDestination().getId().equals(destinationId);
          if (itIsTransportGoesToDestination) {
            validRoutes.add(createRoute(leg1, leg2));
          }
        }
      }

      // If the first leg is NON-FLIGHT
      else {
        List<Transportation> nextFlightLegs =
            transportMap.getOrDefault(leg1.getDestination().getId(), List.of()).stream()
                .filter(t -> TransportationTypeEnum.FLIGHT.equals(t.getTransportationType()))
                .toList();
        for (Transportation leg2 : nextFlightLegs) {

          // 3) BEFORE + FLIGHT
          boolean itIsFlightGoesToDestination = leg2.getDestination().getId().equals(destinationId);
          if (itIsFlightGoesToDestination) {
            validRoutes.add(createRoute(leg1, leg2));
          }

          // 4) BEFORE + FLIGHT + AFTER
          List<Transportation> lastLegs = transportMap.getOrDefault(leg2.getDestination().getId(), List.of()).stream()
              .filter(t -> !TransportationTypeEnum.FLIGHT.equals(t.getTransportationType()))
              .toList();
          for (Transportation leg3 : lastLegs) {
            if (leg3.getDestination().getId().equals(destinationId)) {
              validRoutes.add(createRoute(leg1, leg2, leg3));
            }
          }
        }
      }
    }

    return validRoutes;
  }

  // 1, 2 or 3 legs
  private RouteResponse createRoute(Transportation... transportations) {
    List<RouteLegResponse> legs = new ArrayList<>(transportations.length);
    for (Transportation t : transportations) {
      legs.add(new RouteLegResponse(
          t.getId(),
          t.getOrigin().getName(),
          t.getDestination().getName(),
          t.getTransportationType()
      ));
    }
    return new RouteResponse(legs);
  }
}
