package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.time.LocalTime;

public class StreamImpl implements Stream {
    public StreamImpl(int numberOfViews, LocalTime startTime, Metadata metadata, User streamer) {
        this.numberOfViews = numberOfViews;
        this.startTime = startTime;
        this.metadata = metadata;
        this.streamer = streamer;
    }

    public StreamImpl() {
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Duration getDuration() {
        return Duration.between(this.startTime, LocalTime.now());
    }

    @Override
    public void startWatching(User user) {
        ++numberOfViews;
    }

    @Override
    public void stopWatching(User user) {
        --numberOfViews;
    }

    @Override
    public int getNumberOfViews() {
        return numberOfViews;
    }

    private int numberOfViews;
    private LocalTime startTime;
    private Metadata metadata;
    private User streamer;

}
