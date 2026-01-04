package com.flight.routes.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.domain.enums.TransportationTypeEnum;
import com.flight.routes.dto.route.RouteLegResponse;
import com.flight.routes.dto.route.RouteResponse;
import com.flight.routes.repository.TransportationRepository;
import com.flight.routes.service.LocationService;
import com.flight.routes.service.RouteService;
import com.flight.routes.service.TransportationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("v2")
@Transactional(readOnly = true)
public class RouteServiceImplV2 implements RouteService {
  private final LocationService locationService;
  private final TransportationService transportationService;
  private final TransportationRepository transportationRepository;

  public RouteServiceImplV2(LocationService locationService, TransportationService transportationService,
                            TransportationRepository transportationRepository) {
    this.locationService = locationService;
    this.transportationService = transportationService;
    this.transportationRepository = transportationRepository;
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
    if (originId == null || destinationId == null || originId.equals(destinationId)) {
      return List.of();
    }

    Location origin = locationService.getById(originId);
    Location destination = locationService.getById(destinationId);

    int dayOfWeek = date.getDayOfWeek().getValue();
    List<RouteResponse> validRoutes = new ArrayList<>();

    List<Transportation> firstLegs = transportationRepository.findByOriginAndDay(origin, dayOfWeek);

    for (Transportation leg1 : firstLegs) {
      if (leg1.getTransportationType() == TransportationTypeEnum.FLIGHT) {
        // Direct FLIGHT
        if (leg1.getDestination().getId().equals(destination.getId())) {
          validRoutes.add(createRoute(leg1));
        }

        // FLIGHT + AFTER
        List<Transportation> afterTransfers = transportationRepository
            .findByOriginAndTypeNotAndDay(leg1.getDestination(), TransportationTypeEnum.FLIGHT, dayOfWeek);

        for (Transportation leg2 : afterTransfers) {
          if (leg2.getDestination().getId().equals(destination.getId())) {
            validRoutes.add(createRoute(leg1, leg2));
          }
        }

      } else {
        // BEFORE + FLIGHT
        List<Transportation> flights = transportationRepository
            .findByOriginAndTypeAndDay(leg1.getDestination(), TransportationTypeEnum.FLIGHT, dayOfWeek);

        for (Transportation leg2 : flights) {
          if (leg2.getDestination().getId().equals(destination.getId())) {
            validRoutes.add(createRoute(leg1, leg2));
          }

          // BEFORE + FLIGHT + AFTER
          List<Transportation> lastLegs = transportationRepository
              .findByOriginAndTypeNotAndDay(leg2.getDestination(), TransportationTypeEnum.FLIGHT, dayOfWeek);

          for (Transportation leg3 : lastLegs) {
            if (leg3.getDestination().getId().equals(destination.getId())) {
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
