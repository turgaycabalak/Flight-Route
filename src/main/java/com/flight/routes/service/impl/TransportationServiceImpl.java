package com.flight.routes.service.impl;

import java.util.Optional;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.domain.enums.TransportationTypeEnum;
import com.flight.routes.dto.transportation.TransportationCreateRequest;
import com.flight.routes.dto.transportation.TransportationUpdateRequest;
import com.flight.routes.exception.BusinessException;
import com.flight.routes.exception.NotFoundException;
import com.flight.routes.repository.TransportationRepository;
import com.flight.routes.service.LocationService;
import com.flight.routes.service.TransportationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Long originId = request.originLocationId();
    Long destinationId = request.destinationLocationId();
    TransportationTypeEnum transportationTypeEnum = request.transportationType();

    // todo: structuredScope vm
    Location origin = locationService.getById(originId);
    Location destination = locationService.getById(destinationId);

    boolean exists = transportationRepository.existsBy(originId, destinationId, transportationTypeEnum);
    if (exists) {
      throw new BusinessException("error.transportation.exists",
          origin.getName(), destination.getName(), transportationTypeEnum);
    }

    Transportation transportation = new Transportation(origin, destination, transportationTypeEnum);

    if (request.operatingDays() != null) {
      transportation.getOperatingDays().addAll(request.operatingDays());
    }

    // todo: race condition! maybe try-catch block?
    return transportationRepository.save(transportation);
  }

  @Override
  @Transactional
  public Transportation update(Long id, TransportationUpdateRequest updateRequest) {
    Transportation transportation = getById(id);

    TransportationTypeEnum newType = updateRequest.transportationType();

    boolean exists = transportationRepository.existsByOriginAndDestinationAndTransportationTypeAndIdNot(
        transportation.getOrigin(), transportation.getDestination(), newType, id);

    if (exists) {
      throw new BusinessException("error.transportation.exists",
          transportation.getOrigin().getName(), transportation.getDestination().getName(), newType);
    }

    transportation.setTransportationType(newType);

    if (updateRequest.operatingDays() != null) {
      transportation.getOperatingDays().clear();
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
        .orElseThrow(() -> new NotFoundException("error.transportation.not_found", id));
  }

  @Override
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public Page<Transportation> findAll(Pageable pageable) {
    return transportationRepository.findAll(pageable);
  }
}
