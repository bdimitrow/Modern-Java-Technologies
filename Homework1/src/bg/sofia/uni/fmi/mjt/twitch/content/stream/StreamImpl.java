package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class StreamImpl implements Stream {
    public StreamImpl(Metadata metadata) {
        this.numberOfViews = 0;
        this.startTime = LocalTime.now();
        this.metadata = metadata;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamImpl stream = (StreamImpl) o;
        return numberOfViews == stream.numberOfViews && Objects.equals(metadata, stream.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfViews, metadata);
    }

    private int numberOfViews;
    private LocalTime startTime;
    private Metadata metadata;
}
