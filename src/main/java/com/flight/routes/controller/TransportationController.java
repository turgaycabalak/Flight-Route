package com.flight.routes.controller;

import com.flight.routes.controller.api.TransportationApi;
import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.dto.transportation.TransportationCreateRequest;
import com.flight.routes.dto.transportation.TransportationResponse;
import com.flight.routes.dto.transportation.TransportationUpdateRequest;
import com.flight.routes.mapper.TransportationMapper;
import com.flight.routes.service.TransportationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransportationController implements TransportationApi {
  private final TransportationService transportationService;

  public TransportationController(TransportationService transportationService) {
    this.transportationService = transportationService;
  }

  @Override
  public TransportationResponse create(TransportationCreateRequest request) {
    Transportation transportation = transportationService.create(request);
    return TransportationMapper.INSTANCE.toResponse(transportation);
  }

  @Override
  public TransportationResponse update(Long id, TransportationUpdateRequest request) {
    Transportation updated = transportationService.update(id, request);
    return TransportationMapper.INSTANCE.toResponse(updated);
  }

  @Override
  public void delete(Long id) {
    transportationService.delete(id);
  }

  @Override
  public TransportationResponse getById(Long id) {
    Transportation transportation = transportationService.getById(id);
    return TransportationMapper.INSTANCE.toResponse(transportation);
  }

  @Override
  public Page<TransportationResponse> getAll(Pageable pageable) {
    Page<Transportation> page = transportationService.findAll(pageable);
    return page.map(TransportationMapper.INSTANCE::toResponse);
  }
}

