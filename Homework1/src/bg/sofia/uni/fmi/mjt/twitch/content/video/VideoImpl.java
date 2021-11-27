package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.util.Objects;

public class VideoImpl implements Video {
    public VideoImpl(Content stream) {
        this.metadata = stream.getMetadata();
        this.numberOfViews = 0;
        this.duration = stream.getDuration();
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void startWatching(User user) {
        ++numberOfViews;
    }

    @Override
    public void stopWatching(User user) {
    }

    @Override
    public int getNumberOfViews() {
        return numberOfViews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoImpl video = (VideoImpl) o;
        return numberOfViews == video.numberOfViews && Objects.equals(metadata, video.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfViews, metadata);
    }

    private int numberOfViews;
    private Metadata metadata;
    private Duration duration;
}
