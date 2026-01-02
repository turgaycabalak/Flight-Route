package com.flight.routes.dto.transportation;

import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.flight.routes.domain.enums.TransportationTypeEnum;

public record TransportationUpdateRequest(
    @NotNull(message = "{transportation.type.notNull}")
    TransportationTypeEnum transportationType,

    Set<
        @Min(value = 1, message = "{transportation.operatingDay.min}")
        @Max(value = 7, message = "{transportation.operatingDay.max}")
            Integer
        > operatingDays
) {
}
