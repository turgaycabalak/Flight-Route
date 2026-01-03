package com.flight.routes.service;

import java.util.Optional;

import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.dto.transportation.TransportationCreateRequest;
import com.flight.routes.dto.transportation.TransportationUpdateRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransportationService {

  Transportation create(TransportationCreateRequest request);

  Transportation update(Long id, TransportationUpdateRequest request);

  void delete(Long id);

  Optional<Transportation> findById(Long id);

  Transportation getById(Long id);

  Page<Transportation> findAll(Pageable pageable);
}
