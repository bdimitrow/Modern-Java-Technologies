package bg.sofia.uni.fmi.mjt.race.track;

public class Car implements Runnable {
    private static final int TIME_CAR_RUNNING = 30;
    private final int id;
    private int nPitStops;
    private final Track track;

    public Car(int id, int nPitStops, Track track) {
        this.id = id;
        this.nPitStops = nPitStops;
        this.track = track;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(TIME_CAR_RUNNING);
            --nPitStops;
            getTrack().enterPit(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getCarId() {
        return this.id;
    }

    public int getNPitStops() {
        return this.nPitStops;
    }

    public Track getTrack() {
        return this.track;
    }

}