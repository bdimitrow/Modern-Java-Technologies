package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.Pit;

import java.util.ArrayList;
import java.util.List;

public class RaceTrack implements Track {
    private int numberOfFinishedCars;
    private List<Integer> finishedCarsIds = new ArrayList<>();
    private Pit currentPit;

    public RaceTrack(int numberOfTeams) {
        this.currentPit = new Pit(numberOfTeams);
        this.numberOfFinishedCars = 0;
    }

    @Override
    public void enterPit(Car car) {
        if(car.getNPitStops() == 1){
            ++numberOfFinishedCars;
            finishedCarsIds.add(car.getCarId());
        }
//        currentPit.
    }

    @Override
    public int getNumberOfFinishedCars() {
        return this.numberOfFinishedCars;
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
