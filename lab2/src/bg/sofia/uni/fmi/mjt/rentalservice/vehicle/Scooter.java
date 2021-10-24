package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;
import java.util.UUID;

public class Scooter implements Vehicle {
    public Scooter() {
        this.id = UUID.randomUUID().toString();
    }

    public Scooter(Location location) {
        this.id = UUID.randomUUID().toString();
        this.location = location;
    }

    @Override
    public double getPricePerMinute() {
        return this.PRICE_SCOOTER;
    }

    @Override
    public String getType() {
        return this.TYPE_SCOOTER;
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
        return reservationPeriod;
    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime until) {
        this.reservationPeriod = until;
    }

    private final double PRICE_SCOOTER = 0.30;
    private final String TYPE_SCOOTER = "SCOOTER";
    private String id;
    private Location location;
    private LocalDateTime reservationPeriod;
}
