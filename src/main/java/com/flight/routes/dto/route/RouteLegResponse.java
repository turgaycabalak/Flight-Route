package com.flight.routes.dto.route;

import com.flight.routes.domain.enums.TransportationTypeEnum;

public record RouteLegResponse(
    Long transportationId,
    String origin,
    String destination,
    TransportationTypeEnum type
) {
}
