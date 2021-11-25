package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImpl;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;

import java.util.HashMap;
import java.util.List;

public class Twitch implements StreamingPlatform {
    private UserService userService;
    private HashMap<User,List<Content>> watched;
    private HashMap<User,List<Stream>> streamsOfUser;
    private HashMap<User,List<Video>> videosOfUser;

    public Twitch(UserService userService) {
        this.userService = userService;
        this.watched = new HashMap<>();
        this.streamsOfUser = new HashMap<>();
        this.videosOfUser = new HashMap<>();
    }

    @Override
    public Stream startStream(String username, String title, Category category) throws UserNotFoundException, UserStreamingException {
        if (username == null || title == null || category == null || username.isEmpty() || title.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (this.userService.getUsers().get(username) == null) {
            throw new UserNotFoundException();
        }
        if( this.userService.getUsers().get(username).getStatus() == UserStatus.STREAMING){
            throw new UserStreamingException("User is already streaming!");
        }

        Stream result = new StreamImpl();
        var streamsOfCurrentUser = this.streamsOfUser.get(username);
        return result;
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        if (username == null || stream == null || username.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (this.userService.getUsers().get(username) == null) {
            throw new UserNotFoundException();
        }
        if( this.userService.getUsers().get(username).getStatus() != UserStatus.STREAMING){
            throw new UserStreamingException("User is not streaming!");
        }

        stream.stopStream();

        return null;
    }

    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        if (username == null || content == null || username.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if(this.userService.getUsers().get(username)==null){
            throw new UserNotFoundException();
        }
        if( this.userService.getUsers().get(username).getStatus() == UserStatus.STREAMING){
            throw new UserStreamingException("User is currently streaming!");
        }

    }

    @Override
    public User getMostWatchedStreamer() {
        return null;
    }

    @Override
    public Content getMostWatchedContent() {
        return null;
    }

    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        return null;
    }
}
