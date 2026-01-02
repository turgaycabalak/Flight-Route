package com.flight.routes.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Update and create location request dto.
 */
public record LocationOperationRequest(
    @NotBlank(message = "{location.name.notBlank}")
    @Size(min = 2, max = 100, message = "{location.name.size}")
    String name,

    @NotBlank(message = "{location.country.notBlank}")
    @Size(min = 2, max = 100, message = "{location.country.size}")
    String country,

    @NotBlank(message = "{location.city.notBlank}")
    @Size(min = 2, max = 100, message = "{location.city.size}")
    String city,

    @NotBlank(message = "{location.code.notBlank}")
    @Size(min = 3, max = 10, message = "{location.code.size}")
    String locationCode
) {
}
