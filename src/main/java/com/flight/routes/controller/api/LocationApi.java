package com.flight.routes.controller.api;

import java.util.List;

import jakarta.validation.Valid;

import com.flight.routes.dto.location.LocationOperationRequest;
import com.flight.routes.dto.location.LocationResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/api/locations")
public interface LocationApi {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  LocationResponse create(@Valid @RequestBody LocationOperationRequest request);

  @PutMapping("/{id}")
  LocationResponse update(@PathVariable Long id, @Valid @RequestBody LocationOperationRequest request);

  @GetMapping("/{id}")
  LocationResponse getById(@PathVariable Long id);

  @GetMapping
  List<LocationResponse> getAll();

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@PathVariable Long id);
}

