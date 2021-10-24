package bg.sofia.uni.fmi.mjt.rentalservice;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.service.RentalService;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Bicycle;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Car;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

public class Application {
    public static void main(String[] args) {
        Vehicle skuter = new Car(new Location(0.1, 1));
        Vehicle kola = new Car(new Location(1.2, 2.0));
        Vehicle kolelo = new Bicycle(new Location(3, 4.2));
        Vehicle[] vehicles = {skuter, kola, kolelo};
        RentalService rentalService = new RentalService(vehicles);

//        double cena = rentalService.rentUntil(skuter, LocalDateTime.of(2021, Month.OCTOBER, 23, 11, 55, 0, 123456789));
//        System.out.println(cena);

        Vehicle forRent = rentalService.findNearestAvailableVehicleInRadius("CAR", new Location(0, 0), 10);
        System.out.println(forRent.getType() + " " + forRent.getLocation().getX());


    }

}
