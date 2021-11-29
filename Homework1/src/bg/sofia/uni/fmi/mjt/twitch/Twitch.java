package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImpl;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.content.video.VideoImpl;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Twitch implements StreamingPlatform {
    public final static List<Category> CATEGORIES = List.of(
            Category.GAMES,
            Category.IRL,
            Category.MUSIC,
            Category.ESPORTS);

    public Twitch(UserService userService) {
        this.userService = userService;
        this.contentsOfUser = new HashMap<>();
        this.watchedContentOfUser = new HashMap<>();
    }

    @Override
    public Stream startStream(String username, String title, Category category)
            throws UserNotFoundException, UserStreamingException {
        validateUsername(username);
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Illegal title.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can not be null.");
        }
        if (this.userService.getUsers().get(username).getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException("User is already streaming!");
        }

        // Get User and create Stream
        User streamer = this.userService.getUsers().get(username);
        Stream newStream = new StreamImpl(new Metadata(title, category, streamer));
        // Add the user to the HashMap 'contentsOfUser', if he does not exist there.
        var allUsers = this.contentsOfUser.keySet();
        if (!allUsers.contains(streamer)) {
            this.contentsOfUser.put(streamer, new ArrayList<>());
        }
        // Get all the content of the user and add the stream to it.
        var contentsOfCurrentStreamer = this.contentsOfUser.get(streamer);
        contentsOfCurrentStreamer.add(newStream);
        contentsOfUser.put(streamer, contentsOfCurrentStreamer);
        // Set the status of the user to STREAMING
        streamer.setStatus(UserStatus.STREAMING);

        return newStream;
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        validateUsername(username);
        if (stream == null) {
            throw new IllegalArgumentException("Stream can not be null.");
        }
        if (this.userService.getUsers().get(username).getStatus() != UserStatus.STREAMING) {
            throw new UserStreamingException("User is not streaming!");
        }

        // Find the user by username
        User searchedUser = this.userService.getUsers().get(username);
        // Find the stream that will be stopped
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
        // Set the status of the user to OFFLINE
        searchedUser.setStatus(UserStatus.OFFLINE);
        return newVideo;
    }

    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        validateUsername(username);
        if (content == null) {
            throw new IllegalArgumentException("Content can not be null.");
        }
        if (this.userService.getUsers().get(username).getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException("User is currently streaming!");
        }
        // Find the user by username
        User watcher = this.userService.getUsers().get(username);
        // Increment numberOfWatches for the content
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
        validateUsername(username);
        User searchedUser = this.userService.getUsers().get(username);
        // Get all the contents that this user have watched
        var watchedFromUser = this.watchedContentOfUser.get(searchedUser);
        // Find the most watched Content
        int maxViews = 0;
        Content mostWatchedContent = null;
        for (HashMap.Entry<Content, Integer> set : watchedFromUser.entrySet()) {
            if (set.getValue() > maxViews) {
                mostWatchedContent = set.getKey();
                maxViews = set.getValue();
            }
        }

        return mostWatchedContent;
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        validateUsername(username);
        User searchedUser = this.userService.getUsers().get(username);
        var watchedFromUser = this.watchedContentOfUser.get(searchedUser);
        // Create HashMap to store every category and number of times it has been watched
        HashMap<Category, Integer> timesWatchedCategory = new HashMap<>();
        for (Category currentCategory : CATEGORIES) {
            if (numberByCategory(watchedFromUser, currentCategory) > 0) {
                timesWatchedCategory.put(currentCategory, numberByCategory(watchedFromUser, currentCategory));
            }
        }
        var sortedByNumberWatches = sortByValue(timesWatchedCategory);
        // Extract the categories
        List<Category> result = new ArrayList<>();
        for (Map.Entry<Category, Integer> current : sortedByNumberWatches.entrySet()) {
            result.add(current.getKey());
        }

        return List.copyOf(result);
    }

    private final UserService userService;
    private HashMap<User, List<Content>> contentsOfUser;
    private HashMap<User, HashMap<Content, Integer>> watchedContentOfUser;

    public HashMap<User, List<Content>> getContentsOfUser() {
        return contentsOfUser;
    }

    private void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username can not be null or empty");
        }
        if (this.userService.getUsers().get(username) == null) {
            throw new UserNotFoundException("User could not be found in service.");
        }
    }

    private void addContentToWatched(Content content, User user) {
        if (!this.watchedContentOfUser.containsKey(user)) {
            watchedContentOfUser.put(user, new HashMap<>());
        }
        var watchedByUser = watchedContentOfUser.get(user);
        for (HashMap.Entry<Content, Integer> current : watchedByUser.entrySet()) {
            if (current.getKey().equals(content)) {
                watchedByUser.remove(content);
                watchedByUser.put(content, current.getValue() + 1);
                return;
            }
        }

        watchedByUser.put(content, 1);
    }

    private int numberByCategory(HashMap<Content, Integer> watched, Category category) {
        int result = 0;
        var contents = watched.keySet();

        for (HashMap.Entry<Content, Integer> current : watched.entrySet()) {
            if (current.getKey().getMetadata().category().equals(category)) {
                result += current.getValue();
            }
        }

        return result;
    }

    public static HashMap<Category, Integer> sortByValue(HashMap<Category, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<Category, Integer>> list = new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<>() {
            public int compare(Map.Entry<Category, Integer> o1,
                               Map.Entry<Category, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Category, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Category, Integer> current : list) {
            result.put(current.getKey(), current.getValue());
        }
        return result;
    }

}
