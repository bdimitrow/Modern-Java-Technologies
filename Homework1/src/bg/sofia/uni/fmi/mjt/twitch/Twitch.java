package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImpl;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.content.video.VideoImpl;
import bg.sofia.uni.fmi.mjt.twitch.user.*;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;

import java.util.*;

public class Twitch implements StreamingPlatform {
    public Twitch(UserService userService) {
        this.userService = userService;
        this.contentsOfUser = new HashMap<>();
        this.watchedContentOfUser = new HashMap<>();
    }

    @Override
    public Stream startStream(String username, String title, Category category) throws UserNotFoundException, UserStreamingException {
        if (username == null || title == null || category == null || username.isEmpty() || title.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (this.userService.getUsers().get(username) == null) {
            throw new UserNotFoundException("User could not be found in the service.");
        }
        if (this.userService.getUsers().get(username).getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException("User is already streaming!");
        }
        // Create User an Stream
        UserImpl streamer = new UserImpl(username, UserStatus.STREAMING);
        Stream newStream = new StreamImpl(new Metadata(title, category, streamer));
        // Add the user to the platform, if he does not exist there.
        var allUsers = this.contentsOfUser.keySet();
        if (!allUsers.contains(streamer)) {
            this.contentsOfUser.put(streamer, new ArrayList<>());
        }
        // Get all the content of the user and add the stream to it.
        var contentsOfStreamer = this.contentsOfUser.get(streamer);
        contentsOfStreamer.add(newStream);
        contentsOfUser.put(streamer, contentsOfStreamer);

        return newStream;
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        if (username == null || stream == null || username.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments are passed.");
        }
        if (this.userService.getUsers().get(username) == null) {
            throw new UserNotFoundException("User could not be found in the service.");
        }
        if (this.userService.getUsers().get(username).getStatus() != UserStatus.STREAMING) {
            throw new UserStreamingException("User is not streaming!");
        }
        // Find the user by username
        User searchedUser = getUser(username);
        // Get the stream that will be stopped
        var contentOfSearchedUser = this.contentsOfUser.get(searchedUser);
        Content toBeStopped = null;
        for (Content current : contentOfSearchedUser) {
            if (current.equals(stream)) {
                toBeStopped = current;
            }
        }
        // Validate stream
        if (toBeStopped == null) {
            throw new IllegalArgumentException("The specified stream does not belong to that user.");
        }
        // Create video and add it as content for the user
        Video newVideo = new VideoImpl(toBeStopped);
        contentOfSearchedUser.remove(toBeStopped);
        contentOfSearchedUser.add(newVideo);

        return newVideo;
    }

    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        if (username == null || content == null || username.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (this.userService.getUsers().get(username) == null) {
            throw new UserNotFoundException("User could not be found in service.");
        }
        if (this.userService.getUsers().get(username).getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException("User is currently streaming!");
        }

        User watcher = getUser(username);
        content.startWatching(watcher);

        addContentToWatched(content, watcher);
    }

    @Override
    public User getMostWatchedStreamer() {
        int viewersOfCurrentUser = 0;
        int maxViewers = 0;
        User mostWatchedStreamer = null;
        var allUsers = contentsOfUser.keySet();
        for (User current : allUsers) {
            var contentsOfCurrentUser = contentsOfUser.get(current);
            for (Content currentContent : contentsOfCurrentUser) {
                viewersOfCurrentUser += currentContent.getNumberOfViews();
            }
            if (viewersOfCurrentUser > maxViewers) {
                mostWatchedStreamer = current;
                maxViewers = viewersOfCurrentUser;
            }
        }

        return maxViewers == 0 ? null : mostWatchedStreamer;
    }

    @Override
    public Content getMostWatchedContent() {
        int viewersOfCurrentContent = 0;
        int maxViewersForContent = 0;
        Content mostWatchedContent = null;
        var allUsers = contentsOfUser.keySet();
        for (User current : allUsers) {
            var contentsOfCurrentUser = contentsOfUser.get(current);
            for (Content currentContent : contentsOfCurrentUser) {
                if (currentContent.getNumberOfViews() > maxViewersForContent) {
                    mostWatchedContent = currentContent;
                    maxViewersForContent = currentContent.getNumberOfViews();
                }
            }
        }

        return maxViewersForContent == 0 ? null : mostWatchedContent;
    }

    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        return null;
    }

    private UserService userService;
    private HashMap<User, List<Content>> contentsOfUser;
    private HashMap<User, HashMap<Content, Integer>> watchedContentOfUser;

    private User getUser(String username) {
        var allUsers = this.contentsOfUser.keySet();
        for (User current : allUsers) {
            if (current.getName().equals(username)) {
                return current;
            }
        }
        return null;
    }

    private void addContentToWatched(Content content, User user) {
        if (!this.watchedContentOfUser.containsKey(user)) {
            watchedContentOfUser.put(user, new HashMap<>());
        }
        var watchedByUser = watchedContentOfUser.get(user);
        if (!watchedByUser.containsKey(content)) {
            watchedByUser.put(content, 1);
            return;
        }
        int timesListened = watchedByUser.get(content);
        watchedByUser.put(content, timesListened + 1);
        return;
    }

}
