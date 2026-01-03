package com.flight.routes.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.domain.enums.TransportationTypeEnum;
import com.flight.routes.dto.route.RouteLegResponse;
import com.flight.routes.dto.route.RouteResponse;
import com.flight.routes.service.LocationService;
import com.flight.routes.service.RouteService;
import com.flight.routes.service.TransportationService;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RouteServiceImpl implements RouteService {
  private final LocationService locationService;
  private final TransportationService transportationService;

  public RouteServiceImpl(LocationService locationService, TransportationService transportationService) {
    this.locationService = locationService;
    this.transportationService = transportationService;
  }


  /**
   * Calculate routes between two locations.
   * <p>
   * routes = []
   * <p>
   * 1) Direct FLIGHT
   * origin -> destination (FLIGHT)
   * <p>
   * 2) BEFORE + FLIGHT
   * origin -> X (NON_FLIGHT)
   * X -> destination (FLIGHT)
   * <p>
   * 3) FLIGHT + AFTER
   * origin -> X (FLIGHT)
   * X -> destination (NON_FLIGHT)
   * <p>
   * 4) BEFORE + FLIGHT + AFTER
   * origin -> X (NON_FLIGHT)
   * X -> Y (FLIGHT)
   * Y -> destination (NON_FLIGHT)
   */
  @Override
  public List<RouteResponse> calculateRoutes(Long originId, Long destinationId, LocalDate date) {
    Location origin = locationService.getById(originId);
    Location destination = locationService.getById(destinationId);

    if (origin.getId().equals(destination.getId())) {
      return List.of();
    }

    //List<Transportation> allTransportations = transportationService.findAll(Pageable.unpaged()).getContent();

    List<RouteResponse> routes = new ArrayList<>();

    routes.addAll(findDirectFlights(origin, destination, date));
    routes.addAll(findBeforeFlightRoutes(origin, destination, date));
    routes.addAll(findFlightAfterRoutes(origin, destination, date));
    routes.addAll(findBeforeFlightAfterRoutes(origin, destination, date));

    return routes;
  }

  /// 1) Direct FLIGHT
  /// origin -> destination (FLIGHT) /////////////////////////
  private List<RouteResponse> findDirectFlights(Location origin,
                                                Location destination,
                                                LocalDate date) {

    // there should be only one flight between origin and destination.
    return transportationService.findAll(Pageable.unpaged())
        .getContent()
        .stream()
        .filter(t -> t.getTransportationType() == TransportationTypeEnum.FLIGHT)
        .filter(t -> t.getOrigin().getId().equals(origin.getId()))
        .filter(t -> t.getDestination().getId().equals(destination.getId()))
        .filter(t -> t.operatesOn(date.getDayOfWeek().getValue()))
        .map(this::toDirectFlightRoute)
        .toList();
  }

  private RouteResponse toDirectFlightRoute(Transportation flight) {
    RouteLegResponse leg = new RouteLegResponse(
        flight.getId(),
        flight.getOrigin().getName(),
        flight.getDestination().getName(),
        flight.getTransportationType()
    );

    return new RouteResponse(List.of(leg));
  }

  /// 2) BEFORE + FLIGHT
  /// origin -> X (NON_FLIGHT)
  /// X -> destination (FLIGHT) /////////////////////////
  private List<RouteResponse> findBeforeFlightRoutes(Location origin,
                                                     Location destination,
                                                     LocalDate date) {

    List<Transportation> all = transportationService.findAll(Pageable.unpaged())
        .getContent();

    // 1 BEFORE candidates
    List<Transportation> beforeTransfers = all.stream()
        .filter(t -> t.getTransportationType() != TransportationTypeEnum.FLIGHT)
        .filter(t -> t.getOrigin().getId().equals(origin.getId()))
        .filter(t -> t.operatesOn(date.getDayOfWeek().getValue()))
        .toList();

    // 2 FLIGHT candidates
    List<Transportation> flights = all.stream()
        .filter(t -> t.getTransportationType() == TransportationTypeEnum.FLIGHT)
        .filter(t -> t.getDestination().getId().equals(destination.getId()))
        .filter(t -> t.operatesOn(date.getDayOfWeek().getValue()))
        .toList();

    List<RouteResponse> routes = new ArrayList<>();

    // 3 CONNECT them
    for (Transportation before : beforeTransfers) {
      for (Transportation flight : flights) {

        if (before.getDestination().getId().equals(flight.getOrigin().getId())) {
          routes.add(toBeforeFlightRoute(before, flight));
        }
      }
    }

    return routes;
  }

  private RouteResponse toBeforeFlightRoute(Transportation before, Transportation flight) {
    RouteLegResponse leg1 = new RouteLegResponse(
        before.getId(),
        before.getOrigin().getName(),
        before.getDestination().getName(),
        before.getTransportationType()
    );

    RouteLegResponse leg2 = new RouteLegResponse(
        flight.getId(),
        flight.getOrigin().getName(),
        flight.getDestination().getName(),
        flight.getTransportationType()
    );

    return new RouteResponse(List.of(leg1, leg2));
  }

  /// 3) FLIGHT + AFTER
  /// origin -> X (FLIGHT)
  /// X -> destination (NON_FLIGHT) /////////////////////////
  private List<RouteResponse> findFlightAfterRoutes(Location origin,
                                                    Location destination,
                                                    LocalDate date) {

    List<Transportation> all = transportationService
        .findAll(Pageable.unpaged())
        .getContent();

    // 1 FLIGHT candidates
    List<Transportation> flights = all.stream()
        .filter(t -> t.getTransportationType() == TransportationTypeEnum.FLIGHT)
        .filter(t -> t.getOrigin().getId().equals(origin.getId()))
        .filter(t -> t.operatesOn(date.getDayOfWeek().getValue()))
        .toList();

    // 2 AFTER candidates
    List<Transportation> afterTransfers = all.stream()
        .filter(t -> t.getTransportationType() != TransportationTypeEnum.FLIGHT)
        .filter(t -> t.getDestination().getId().equals(destination.getId()))
        .filter(t -> t.operatesOn(date.getDayOfWeek().getValue()))
        .toList();

    List<RouteResponse> routes = new ArrayList<>();

    // 3 CONNECT
    for (Transportation flight : flights) {
      for (Transportation after : afterTransfers) {

        if (flight.getDestination().getId().equals(after.getOrigin().getId())) {
          routes.add(toFlightAfterRoute(flight, after));
        }
      }
    }

    return routes;
  }

  private RouteResponse toFlightAfterRoute(Transportation flight, Transportation after) {
    RouteLegResponse leg1 = new RouteLegResponse(
        flight.getId(),
        flight.getOrigin().getName(),
        flight.getDestination().getName(),
        flight.getTransportationType()
    );

    RouteLegResponse leg2 = new RouteLegResponse(
        after.getId(),
        after.getOrigin().getName(),
        after.getDestination().getName(),
        after.getTransportationType()
    );

    return new RouteResponse(List.of(leg1, leg2));
  }


  /// 4) BEFORE + FLIGHT + AFTER
  /// origin -> X (NON_FLIGHT)
  /// X -> Y (FLIGHT)
  /// Y -> destination (NON_FLIGHT) /////////////////////////
  private List<RouteResponse> findBeforeFlightAfterRoutes(Location origin,
                                                          Location destination,
                                                          LocalDate date) {

    List<Transportation> all = transportationService
        .findAll(Pageable.unpaged())
        .getContent();

    // 1 BEFORE
    List<Transportation> befores = all.stream()
        .filter(t -> t.getTransportationType() != TransportationTypeEnum.FLIGHT)
        .filter(t -> t.getOrigin().getId().equals(origin.getId()))
        .filter(t -> t.operatesOn(date.getDayOfWeek().getValue()))
        .toList();

    // 2 FLIGHT
    List<Transportation> flights = all.stream()
        .filter(t -> t.getTransportationType() == TransportationTypeEnum.FLIGHT)
        .filter(t -> t.operatesOn(date.getDayOfWeek().getValue()))
        .toList();

    // 3 AFTER
    List<Transportation> afters = all.stream()
        .filter(t -> t.getTransportationType() != TransportationTypeEnum.FLIGHT)
        .filter(t -> t.getDestination().getId().equals(destination.getId()))
        .filter(t -> t.operatesOn(date.getDayOfWeek().getValue()))
        .toList();

    List<RouteResponse> routes = new ArrayList<>();

    // 4 CONNECT ALL
    for (Transportation before : befores) {
      for (Transportation flight : flights) {
        if (!before.getDestination().equals(flight.getOrigin())) {
          continue;
        }

        for (Transportation after : afters) {
          if (!flight.getDestination().equals(after.getOrigin())) {
            continue;
          }

          routes.add(toBeforeFlightAfterRoute(before, flight, after));
        }
      }
    }

    return routes;
  }

  private RouteResponse toBeforeFlightAfterRoute(Transportation before,
                                                 Transportation flight,
                                                 Transportation after) {
    RouteLegResponse leg1 = toLeg(before);
    RouteLegResponse leg2 = toLeg(flight);
    RouteLegResponse leg3 = toLeg(after);

    return new RouteResponse(List.of(leg1, leg2, leg3));
  }

  private RouteLegResponse toLeg(Transportation t) {
    return new RouteLegResponse(
        t.getId(),
        t.getOrigin().getName(),
        t.getDestination().getName(),
        t.getTransportationType()
    );
  }
}
