package com.flight.routes.controller.api;

import java.time.LocalDate;
import java.util.List;

import com.flight.routes.dto.route.RouteResponse;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/routes")
@Tag(name = "Route API", description = "Route endpoints")
public interface RouteApi {

  @GetMapping("/v1")
  List<RouteResponse> getRoutes(@RequestParam("originId") Long originId,
                                @RequestParam("destinationId") Long destinationId,
                                @RequestParam(value = "date", required = false) LocalDate date);

  @GetMapping("/v2")
  List<RouteResponse> getRoutesV2(@RequestParam("originId") Long originId,
                                  @RequestParam("destinationId") Long destinationId,
                                  @RequestParam(value = "date", required = false) LocalDate date);

  @GetMapping("/v3")
  List<RouteResponse> getRoutesV3(@RequestParam("originId") Long originId,
                                  @RequestParam("destinationId") Long destinationId,
                                  @RequestParam(value = "date", required = false) LocalDate date);
}
