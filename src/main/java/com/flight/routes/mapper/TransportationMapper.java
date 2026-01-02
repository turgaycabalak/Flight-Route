package com.flight.routes.mapper;

import java.util.Collection;
import java.util.List;

import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.dto.transportation.TransportationResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(/*componentModel = "spring", */uses = LocationMapper.class)
public interface TransportationMapper {
  TransportationMapper INSTANCE = Mappers.getMapper(TransportationMapper.class);

  @Mapping(target = "originLocation", source = "origin")
  @Mapping(target = "destinationLocation", source = "destination")
  TransportationResponse toResponse(Transportation entity);

  List<TransportationResponse> toResponseList(Collection<Transportation> entities);
}
