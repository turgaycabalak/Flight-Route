package com.flight.routes.dto.location;

public record LocationResponse(
    Long id,
    String name,
    String country,
    String city,
    String locationCode
) {
}
