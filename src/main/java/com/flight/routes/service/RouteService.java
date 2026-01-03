package com.flight.routes.service;

import java.time.LocalDate;
import java.util.List;

import com.flight.routes.dto.route.RouteResponse;

public interface RouteService {

  List<RouteResponse> calculateRoutes(Long originId, Long destinationId, LocalDate date);
}
