package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

public class PitTeam extends Thread {
    private static final int TIME_CAR_IN_PIT = 10;
    private final Pit pitStop;
    private int idPitTeam;
    private int totalCars;

    public PitTeam(int id, Pit pitStop) {
        this.idPitTeam = id;
        this.pitStop = pitStop;
        this.totalCars = 0;
    }

    @Override
    public void run() {
        try {
            boolean moreCars = true;
            while (moreCars) {
                Car car = pitStop.getCar();
                if (car != null) {
                    Thread.sleep(TIME_CAR_IN_PIT);
                    ++totalCars;
                    car.run();
                } else {
                    moreCars = false;
                }
            }
        } catch (InterruptedException e) {
            System.err.print("Unexpected exception was thrown: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getPitStoppedCars() {
        return this.totalCars;
    }

}