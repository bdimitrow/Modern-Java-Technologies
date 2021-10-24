package bg.sofia.uni.fmi.mjt.rentalservice.service;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalService implements RentalServiceAPI {
    public RentalService(Vehicle[] vehicles) {
        this.vehicles = vehicles;

    }

    @Override
    public double rentUntil(Vehicle vehicle, LocalDateTime until) {
        vehicle.setEndOfReservationPeriod(until);
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), until);
        if (minutes > 0) {
            return minutes * vehicle.getPricePerMinute();
        }
        return -1;
    }

    @Override
    public Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance) {
        Vehicle[] vehiclesInMethod = this.vehicles;
        for (Vehicle vehicle : vehiclesInMethod) {
            if (vehicle.getType().equals(type)) {
                double distance = calculateDistance(vehicle.getLocation(), location);
                if (distance < maxDistance) {
                    return vehicle;
                }
            }
        }
        return null;
    }

    private double calculateDistance(Location a, Location b) {
        return Math.sqrt(
                (b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY()) * (b.getY() - a.getY()));
    }

    private Vehicle[] vehicles;
}
