package com.flight.routes.controller;

import java.time.LocalDate;
import java.util.List;

import com.flight.routes.controller.api.RouteApi;
import com.flight.routes.dto.route.RouteResponse;
import com.flight.routes.service.RouteService;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController implements RouteApi {
  private final RouteService routeService;

  public RouteController(RouteService routeService) {
    this.routeService = routeService;
  }


  @Override
  public List<RouteResponse> getRoutes(Long originId, Long destinationId, LocalDate date) {
    return routeService.calculateRoutes(originId, destinationId, date);
  }

}
