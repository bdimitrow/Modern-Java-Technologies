package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;

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

    private int numberOfViews;
    private Metadata metadata;
    private Duration duration;
}
