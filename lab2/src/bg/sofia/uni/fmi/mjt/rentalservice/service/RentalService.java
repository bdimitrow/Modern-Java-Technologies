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
//        if(isBooked(vehicle)){
//            return -1;
//        }
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
        Vehicle nearestVehicle = null;
        double minDistance = maxDistance;
        for (Vehicle vehicle : vehiclesInMethod) {
            if (vehicle.getType().equals(type)) {
                double currentDistance = calculateDistance(vehicle.getLocation(), location);
                if (!isBooked(vehicle) && currentDistance < minDistance) {
                    minDistance = currentDistance;
                    nearestVehicle = vehicle;
                }
            }
        }
        return nearestVehicle;
    }

    private double calculateDistance(Location a, Location b) {
        return Math.sqrt(
                (b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY()) * (b.getY() - a.getY()));
    }

    private boolean isBooked(Vehicle vehicle) {
        return vehicle.getEndOfReservationPeriod().isAfter(LocalDateTime.now());
    }

    private Vehicle[] vehicles;
}
