package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.ArrayList;
import java.util.List;

public class Pit {
    private final List<PitTeam> pitTeams = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private boolean finished = false;

    public Pit(int nPitTeams) {
        if (nPitTeams < 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < nPitTeams; ++i) {
            var pitTeam = new PitTeam(i, this);
            pitTeams.add(pitTeam);
            pitTeam.start();
        }
    }

    public void submitCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException();
        }
        if (finished) {
            return;
        }
        synchronized (this) {
            cars.add(car);
            this.notifyAll();
        }
    }

    public synchronized Car getCar() {
        while (cars.isEmpty() && !finished) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (cars.isEmpty()) {
            return null;
        }
        Car tobeReturned = cars.get(0);
        cars.remove(tobeReturned);
        return tobeReturned;
    }

    public int getPitStopsCount() {
        int stopsCount = 0;
        for (var pitTeam : pitTeams) {
            stopsCount += pitTeam.getPitStoppedCars();
        }

        return stopsCount;
    }

    public List<PitTeam> getPitTeams() {
        return this.pitTeams;
    }

    public void finishRace() {
        finished = true;

        synchronized (this) {
            this.notifyAll();
        }
    }
}