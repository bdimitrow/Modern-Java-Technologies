package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.Pit;

import java.util.ArrayList;
import java.util.List;

public class RaceTrack implements Track {
    private final List<Integer> finishedCarsIds = new ArrayList<>();
    private final Pit currentPit;
    private int totalPitCounts;

    public RaceTrack(int numberOfTeams) {
        this.currentPit = new Pit(numberOfTeams);
        totalPitCounts = 0;
    }

    @Override
    public void enterPit(Car car) {
        if (car.getNPitStops() < 0) {
            finishedCarsIds.add(car.getCarId());
        } else {
            ++totalPitCounts;
            currentPit.submitCar(car);
        }
    }

    public int getTotalPitCounts() {
        return this.totalPitCounts;
    }

    @Override
    public int getNumberOfFinishedCars() {
        return this.finishedCarsIds.size();
    }

    @Override
    public List<Integer> getFinishedCarsIds() {
        return this.finishedCarsIds;
    }

    @Override
    public Pit getPit() {
        return this.currentPit;
    }

//    public static void main(String[] args) throws InterruptedException {
//        RaceTrack raceTrack = new RaceTrack(3);
//        List<Car> carList = new ArrayList<>();
//        for (int i = 1; i <= 5; ++i) {
//            carList.add(new Car(i, 4, raceTrack));
//        }
//        for (Car car : carList) {
//            car.run();
//        }
//
//        Thread.sleep(5000);
//        raceTrack.getPit().finishRace();
//
//        for (Car car : carList) {
//            System.out.println(car.getNPitStops());
//        }
//
//        System.out.println(raceTrack.getPit().getPitStopsCount());
//        System.out.println(raceTrack.getNumberOfFinishedCars());
//
//    }
}
