package com.flight.routes.controller.api;

import jakarta.validation.Valid;

import com.flight.routes.dto.location.LocationOperationRequest;
import com.flight.routes.dto.location.LocationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@Tag(name = "Location API", description = "Location management endpoints")
public interface LocationApi {

  @Operation(
      summary = "Create location",
      description = "Creates a new location"
  )
  @ApiResponse(
      responseCode = "201",
      description = "Location created successfully"
  )
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  LocationResponse create(@Valid @RequestBody LocationOperationRequest request);

  @PutMapping("/{id}")
  LocationResponse update(@PathVariable Long id, @Valid @RequestBody LocationOperationRequest request);

  @Operation(summary = "Get location by id")
  @ApiResponse(
      responseCode = "200",
      description = "Location found successfully"
  )
  @GetMapping("/{id}")
  LocationResponse getById(@PathVariable Long id);

  @Operation(summary = "Get all locations")
  @ApiResponse(
      responseCode = "200",
      description = "Locations found successfully"
  )
  @GetMapping
  Page<LocationResponse> getAll(
      @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable);

  @Operation(summary = "Delete location")
  @ApiResponse(
      responseCode = "204",
      description = "Location deleted successfully"
  )
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@PathVariable Long id);
}

