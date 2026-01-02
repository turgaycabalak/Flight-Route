package com.flight.routes.mapper;

import java.util.Collection;
import java.util.List;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.dto.location.LocationResponse;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
  LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);


  LocationResponse toResponse(Location entity);

  List<LocationResponse> toResponseList(Collection<Location> entities);
}
