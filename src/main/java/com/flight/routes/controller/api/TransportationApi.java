package com.flight.routes.controller.api;

import java.util.List;

import com.flight.routes.dto.transportation.TransportationCreateRequest;
import com.flight.routes.dto.transportation.TransportationResponse;
import com.flight.routes.dto.transportation.TransportationUpdateRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/api/transportations")
public interface TransportationApi {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  TransportationResponse create(@RequestBody TransportationCreateRequest request);

  @PutMapping("/{id}")
  TransportationResponse update(@PathVariable Long id, @RequestBody TransportationUpdateRequest request);

  @GetMapping("/{id}")
  TransportationResponse getById(@PathVariable Long id);

  @GetMapping
  List<TransportationResponse> getAll();

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@PathVariable Long id);
}

