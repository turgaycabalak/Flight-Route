package com.flight.routes.dto.transportation;

import java.util.Set;

import com.flight.routes.domain.enums.TransportationTypeEnum;
import com.flight.routes.dto.location.LocationResponse;

public record TransportationResponse(
    Long id,
    LocationResponse originLocation,
    LocationResponse destinationLocation,
    TransportationTypeEnum transportationType,
    Set<Integer> operatingDays
) {
}
