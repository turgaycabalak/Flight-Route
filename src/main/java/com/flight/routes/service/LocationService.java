package com.flight.routes.service;

import java.util.List;
import java.util.Optional;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.dto.location.LocationOperationRequest;

public interface LocationService {
  Location create(Location location);

  Location update(Long id, LocationOperationRequest updateRequest);

  void delete(Long id);

  Optional<Location> findById(Long id);

  Location getById(Long id);

  List<Location> findAll();
}
