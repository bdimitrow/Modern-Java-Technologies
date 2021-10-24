package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;
import java.util.UUID;

public class Bicycle implements Vehicle {
    public Bicycle() {
        this.id = UUID.randomUUID().toString();
    }

    public Bicycle(Location location) {
        this.id = UUID.randomUUID().toString();
        this.location = location;
    }

    @Override
    public double getPricePerMinute() {
        return this.PRICE_BICYCLE;
    }

    @Override
    public String getType() {
        return this.TYPE_BICYCLE;
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
        return this.reservationPeriod;
    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime until) {
        this.reservationPeriod = until;
    }

    private final double PRICE_BICYCLE = 0.20;
    private final String TYPE_BICYCLE = "BICYCLE";
    private String id;
    private Location location;
    private LocalDateTime reservationPeriod;
}
