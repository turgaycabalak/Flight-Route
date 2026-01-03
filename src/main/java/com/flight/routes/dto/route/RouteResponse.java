package com.flight.routes.dto.route;

import java.util.List;

public record RouteResponse(
    List<RouteLegResponse> legs
) {
}
