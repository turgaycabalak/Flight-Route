package com.flight.routes.service.impl;

import java.util.List;
import java.util.Optional;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.dto.transportation.TransportationCreateRequest;
import com.flight.routes.dto.transportation.TransportationUpdateRequest;
import com.flight.routes.repository.TransportationRepository;
import com.flight.routes.service.LocationService;
import com.flight.routes.service.TransportationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransportationServiceImpl implements TransportationService {
  private final TransportationRepository transportationRepository;
  private final LocationService locationService;

  public TransportationServiceImpl(TransportationRepository transportationRepository, LocationService locationService) {
    this.transportationRepository = transportationRepository;
    this.locationService = locationService;
  }

  @Override
  @Transactional
  public Transportation create(TransportationCreateRequest request) {
    // todo: structuredScope vm
    Location origin = locationService.getById(request.originLocationId());
    Location destination = locationService.getById(request.destinationLocationId());

    Transportation transportation = new Transportation(origin, destination, request.transportationType());

    if (request.operatingDays() != null) {
      transportation.getOperatingDays().addAll(request.operatingDays());
    }

    return transportationRepository.save(transportation);
  }

  @Override
  @Transactional
  public Transportation update(Long id, TransportationUpdateRequest updateRequest) {
    Transportation transportation = getById(id);

    transportation.setTransportationType(updateRequest.transportationType());

    transportation.getOperatingDays().clear();
    if (updateRequest.operatingDays() != null) {
      transportation.getOperatingDays().addAll(updateRequest.operatingDays());
    }

    return transportation;
  }

  @Override
  @Transactional
  public void delete(Long id) {
    // todo: soft-delete
    transportationRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public Optional<Transportation> findById(Long id) {
    return transportationRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public Transportation getById(Long id) {
    return transportationRepository.findById(id)
        // todo: throw exception
        // todo: multi language
        .orElseThrow(() -> new IllegalArgumentException("Transportation not found with id: " + id));
  }

  @Override
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public List<Transportation> findAll() {
    // todo: pageable
    return transportationRepository.findAll();
  }
}
