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
}
