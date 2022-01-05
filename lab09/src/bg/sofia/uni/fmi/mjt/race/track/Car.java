package bg.sofia.uni.fmi.mjt.race.track;

public class Car implements Runnable {
    private int id;
    private int nPitStops;
    private Track track;

    public Car(int id, int nPitStops, Track track) {
        this.id = id;
        this.nPitStops = nPitStops;
        this.track = track;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Method not yet implemented");
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