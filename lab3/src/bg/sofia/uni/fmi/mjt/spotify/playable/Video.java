package bg.sofia.uni.fmi.mjt.spotify.playable;

public class Video implements Playable {
    public Video(String title, String artist, int year, double dur) {
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.duration = dur;
    }

    @Override
    public String play() {
        ++numberOfPlays;
        return "Currently playing Video content: " + this.title;
    }

    @Override
    public int getTotalPlays() {
        return this.numberOfPlays;
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

    private int numberOfPlays = 0;
    private String title;
    private String artist;
    private int year;
    private double duration;
}
