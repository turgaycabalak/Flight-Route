package com.flight.routes.repository;

import com.flight.routes.domain.entity.Transportation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportationRepository extends JpaRepository<Transportation, Long> {
}
