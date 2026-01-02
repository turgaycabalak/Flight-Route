package com.flight.routes.controller;

import java.util.List;

import com.flight.routes.controller.api.TransportationApi;
import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.dto.transportation.TransportationCreateRequest;
import com.flight.routes.dto.transportation.TransportationResponse;
import com.flight.routes.dto.transportation.TransportationUpdateRequest;
import com.flight.routes.mapper.TransportationMapper;
import com.flight.routes.service.TransportationService;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransportationController implements TransportationApi {
  private final TransportationService transportationService;
  private final TransportationMapper transportationMapper;

  public TransportationController(TransportationService transportationService,
                                  TransportationMapper transportationMapper) {
    this.transportationService = transportationService;
    this.transportationMapper = transportationMapper;
  }

  @Override
  public TransportationResponse create(TransportationCreateRequest request) {
    Transportation transportation = transportationService.create(request);
    return transportationMapper.toResponse(transportation);
  }

  @Override
  public TransportationResponse update(Long id, TransportationUpdateRequest request) {
    Transportation updated = transportationService.update(id, request);
    return transportationMapper.toResponse(updated);
  }

  @Override
  public void delete(Long id) {
    transportationService.delete(id);
  }

  @Override
  public TransportationResponse getById(Long id) {
    Transportation transportation = transportationService.getById(id);
    return transportationMapper.toResponse(transportation);
  }

  @Override
  public List<TransportationResponse> getAll() {
    List<Transportation> all = transportationService.findAll();
    return transportationMapper.toResponseList(all);
  }
}

