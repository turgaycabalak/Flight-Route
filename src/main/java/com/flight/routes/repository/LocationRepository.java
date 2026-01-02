package com.flight.routes.repository;

import com.flight.routes.domain.entity.Location;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
