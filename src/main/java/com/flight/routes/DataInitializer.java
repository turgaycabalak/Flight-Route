package com.flight.routes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flight.routes.domain.entity.Location;
import com.flight.routes.domain.entity.Transportation;
import com.flight.routes.domain.enums.TransportationTypeEnum;
import com.flight.routes.repository.LocationRepository;
import com.flight.routes.repository.TransportationRepository;
import com.github.javafaker.Faker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
//@Profile("local")
public class DataInitializer implements CommandLineRunner {
  private final LocationRepository locationRepository;
  private final TransportationRepository transportationRepository;
  private final Faker faker = new Faker();
  private final List<Location> airports = new ArrayList<>();
  private final List<Location> cities = new ArrayList<>();

  public DataInitializer(LocationRepository locationRepository, TransportationRepository transportationRepository) {
    this.locationRepository = locationRepository;
    this.transportationRepository = transportationRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    long locationCount = locationRepository.count();
    long transportationCount = transportationRepository.count();

    if (locationCount == 0 && transportationCount == 0) {
      seedLocations();
      seedTransportations();
    }
  }

  // -------------------- LOCATION --------------------
  private void seedLocations() {
    airports.add(saveLocation("Istanbul Airport", "Turkey", "Istanbul", "IST"));
    airports.add(saveLocation("Sabiha Gökçen Airport", "Turkey", "Istanbul", "SAW"));
    airports.add(saveLocation("London Heathrow Airport", "UK", "London", "LHR"));

    cities.add(saveLocation("Taksim Square", "Turkey", "Istanbul", "CCIST"));
    cities.add(saveLocation("Kadıköy", "Turkey", "Istanbul", "CCKD"));
    cities.add(saveLocation("Wembley Stadium", "UK", "London", "CCWEM"));
  }

  private Location saveLocation(String name, String country, String city, String code) {
    Location location = new Location(name, country, city, code);
    return locationRepository.save(location);
  }

  // -------------------- TRANSPORTATION --------------------
  private void seedTransportations() {
    seedBeforeFlights();
    seedFlights();
    seedAfterFlights();
  }

  /**
   * CITY -> AIRPORT (BUS / SUBWAY / UBER)
   */
  private void seedBeforeFlights() {
    for (Location city : cities) {
      for (Location airport : airports) {
        saveTransportation(
            city,
            airport,
            faker.options().option(
                TransportationTypeEnum.BUS,
                TransportationTypeEnum.SUBWAY,
                TransportationTypeEnum.UBER
            )
        );
      }
    }
  }

  /**
   * AIRPORT -> AIRPORT (FLIGHT)
   */
  private void seedFlights() {
    for (Location origin : airports) {
      for (Location destination : airports) {
        if (!origin.equals(destination)) {
          saveTransportation(origin, destination, TransportationTypeEnum.FLIGHT);
        }
      }
    }
  }

  /**
   * AIRPORT -> CITY (BUS / UBER)
   */
  private void seedAfterFlights() {
    for (Location airport : airports) {
      for (Location city : cities) {
        saveTransportation(
            airport,
            city,
            faker.options().option(
                TransportationTypeEnum.BUS,
                TransportationTypeEnum.UBER
            )
        );
      }
    }
  }

  private void saveTransportation(Location origin,
                                  Location destination,
                                  TransportationTypeEnum type) {
    Transportation transportation = new Transportation(origin, destination, type);

    transportation.getOperatingDays().addAll(randomOperatingDays());

    transportationRepository.save(transportation);
  }

  // -------------------- OPERATING DAYS --------------------
  private Set<Integer> randomOperatingDays() {
    Set<Integer> days = new HashSet<>();
    int count = faker.number().numberBetween(2, 5);

    while (days.size() < count) {
      days.add(faker.number().numberBetween(1, 8)); // 1–7
    }
    return days;
  }
}
