package com.flight.routes.controller;

import java.util.List;

import com.flight.routes.controller.api.LocationApi;
import com.flight.routes.domain.entity.Location;
import com.flight.routes.dto.location.LocationOperationRequest;
import com.flight.routes.dto.location.LocationResponse;
import com.flight.routes.mapper.LocationMapper;
import com.flight.routes.service.LocationService;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController implements LocationApi {
  private final LocationService locationService;

  public LocationController(LocationService locationService) {
    this.locationService = locationService;
  }

  @Override
  public LocationResponse create(LocationOperationRequest request) {
    Location location = new Location(
        request.name(),
        request.country(),
        request.city(),
        request.locationCode()
    );
    Location created = locationService.create(location);
    return LocationMapper.INSTANCE.toResponse(created);
  }

  @Override
  public LocationResponse update(Long id, LocationOperationRequest request) {
    Location updated = locationService.update(id, request);
    return LocationMapper.INSTANCE.toResponse(updated);
  }

  @Override
  public void delete(Long id) {
    locationService.delete(id);
  }

  @Override
  public LocationResponse getById(Long id) {
    Location location = locationService.getById(id);
    return LocationMapper.INSTANCE.toResponse(location);
  }

  @Override
  public List<LocationResponse> getAll() {
    List<Location> all = locationService.findAll();
    return LocationMapper.INSTANCE.toResponseList(all);
  }
}
