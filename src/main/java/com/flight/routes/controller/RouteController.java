package com.flight.routes.controller;

import java.time.LocalDate;
import java.util.List;

import com.flight.routes.controller.api.RouteApi;
import com.flight.routes.dto.route.RouteResponse;
import com.flight.routes.service.RouteService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController implements RouteApi {
  private final RouteService routeServiceV1;
  private final RouteService routeServiceV2;
  private final RouteService routeServiceV3;

  public RouteController(@Qualifier("v1") RouteService routeServiceV1,
                         @Qualifier("v2") RouteService routeServiceV2,
                         @Qualifier("v3") RouteService routeServiceV3) {
    this.routeServiceV1 = routeServiceV1;
    this.routeServiceV2 = routeServiceV2;
    this.routeServiceV3 = routeServiceV3;
  }


  @Override
  public List<RouteResponse> getRoutes(Long originId, Long destinationId, LocalDate date) {
    return routeServiceV1.calculateRoutes(originId, destinationId, date);
  }

  @Override
  public List<RouteResponse> getRoutesV2(Long originId, Long destinationId, LocalDate date) {
    return routeServiceV2.calculateRoutes(originId, destinationId, date);
  }

  @Override
  public List<RouteResponse> getRoutesV3(Long originId, Long destinationId, LocalDate date) {
    return routeServiceV3.calculateRoutes(originId, destinationId, date);
  }
}
