package com.flight.routes.service;

import java.util.Optional;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.dto.location.LocationOperationRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationService {
  Location create(Location location);

  Location update(Long id, LocationOperationRequest updateRequest);

  void delete(Long id);

  Optional<Location> findById(Long id);

  Location getById(Long id);

  Page<Location> findAll(Pageable pageable);
}
