package bg.sofia.uni.fmi.mjt.spotify.playable;

public class Audio implements Playable {

    @Override
    public String play() {
        ++numberOfPlays;
        return "Currently playing Audio content: " + this.title;
    }

    @Override
    public int getTotalPlays() {
        return numberOfPlays;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getArtist() {
        return this.artist;
    }

    @Override
    public int getYear() {
        return this.year;
    }

    @Override
    public double getDuration() {
        return this.duration;
    }

    private static int numberOfPlays = 0;
    private String title;
    private String artist;
    private int year;
    private double duration;
}
