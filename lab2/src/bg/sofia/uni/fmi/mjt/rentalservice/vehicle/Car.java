package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Car implements Vehicle {
    public Car() {
        this.id = UUID.randomUUID().toString();
    }

    public Car(String id, Location location) {
        this.id = id;
        this.location = location;
    }

    @Override
    public double getPricePerMinute() {
        return 0.50;
    }

    @Override
    public String getType() {
        return "CAR";
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public LocalDateTime getEndOfReservationPeriod() {
        return reservationPeriod == null ? LocalDateTime.now() : reservationPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime until) {
        this.reservationPeriod = until;
    }

    private final String id;
    private Location location;
    private LocalDateTime reservationPeriod;
}
