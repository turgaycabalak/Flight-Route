package com.flight.routes.util;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class LocationSortMapper {
  // k: client, v: db
  private static final Map<String, String> SORT_MAP = Map.of(
      "id", "id",
      "locationName", "name",
      "country", "country",
      "city", "city",
      "code", "locationCode"
  );

  private LocationSortMapper() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static Pageable mapPageable(Pageable pageable) {
    if (pageable.getSort().isUnsorted()) {
      return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
    }

    List<Sort.Order> orders = pageable.getSort().stream()
        .filter(order -> SORT_MAP.containsKey(order.getProperty()))
        .map(order -> new Sort.Order(
            order.getDirection(),
            SORT_MAP.get(order.getProperty())
        ))
        .toList();

    Sort finalSort = orders.isEmpty()
        ? Sort.by(Sort.Direction.DESC, "id")
        : Sort.by(orders);

    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), finalSort);
  }
}
