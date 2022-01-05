package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.List;

public class Pit {
    private List<PitTeam> pitTeams;
    private int maxPitTeams;

    public Pit(int nPitTeams) {
        this.maxPitTeams = nPitTeams;

    }

    public void submitCar(Car car) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    public Car getCar() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    public int getPitStopsCount() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    public List<PitTeam> getPitTeams() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    public void finishRace() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

}