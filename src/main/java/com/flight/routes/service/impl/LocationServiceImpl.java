package com.flight.routes.service.impl;

import java.util.Optional;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.dto.location.LocationOperationRequest;
import com.flight.routes.exception.BusinessException;
import com.flight.routes.exception.NotFoundException;
import com.flight.routes.repository.LocationRepository;
import com.flight.routes.service.LocationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationServiceImpl implements LocationService {
  private final LocationRepository locationRepository;

  public LocationServiceImpl(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
  }

  @Override
  @Transactional
  public Location create(Location location) {
    String locationCode = location.getLocationCode();
    boolean exists = locationRepository.existsByLocationCode(locationCode);
    if (exists) {
      throw new BusinessException("error.location.code_exists", locationCode);
    }
    return locationRepository.save(location);
  }

  @Override
  @Transactional
  public Location update(Long id, LocationOperationRequest updateRequest) {
    Location location = getById(id);

    location.setName(updateRequest.name());
    location.setCountry(updateRequest.country());
    location.setCity(updateRequest.city());
    location.setLocationCode(updateRequest.locationCode());

    return location;
  }

  @Override
  @Transactional
  public void delete(Long id) {
    // todo: soft-delete
    locationRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public Optional<Location> findById(Long id) {
    return locationRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public Location getById(Long id) {
    return findById(id)
        .orElseThrow(() -> new NotFoundException("error.location.not_found", id));
  }

  @Override
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public Page<Location> findAll(Pageable pageable) {
    return locationRepository.findAll(pageable);
  }
}
