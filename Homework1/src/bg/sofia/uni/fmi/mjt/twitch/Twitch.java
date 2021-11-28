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

import java.util.*;

public class Twitch implements StreamingPlatform {
    public final List<Category> CATEGORIES = List.of(Category.GAMES, Category.IRL, Category.MUSIC, Category.ESPORTS);

    public Twitch(UserService userService) {
        this.userService = userService;
        this.contentsOfUser = new HashMap<>();
        this.watchedContentOfUser = new HashMap<>();
    }

    @Override
    public Stream startStream(String username, String title, Category category)
            throws UserNotFoundException, UserStreamingException {
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
        User streamer = this.userService.getUsers().get(username);
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

        streamer.setStatus(UserStatus.STREAMING);
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
        User searchedUser = this.userService.getUsers().get(username);
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

        searchedUser.setStatus(UserStatus.OFFLINE);
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

        User watcher = this.userService.getUsers().get(username);
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
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username can not be null or empty");
        }
        if (this.userService.getUsers().get(username) == null) {
            throw new UserNotFoundException("User could not be found in service.");
        }
        User searchedUser = this.userService.getUsers().get(username);
        var watchedFromUser = this.watchedContentOfUser.get(searchedUser);
        var contents = watchedFromUser.keySet();
        int maxViews = 0;
        Content mostWatchedContent = null;
        for (var currentContent : contents) {
            if (watchedFromUser.get(currentContent) > maxViews) {
                mostWatchedContent = currentContent;
                maxViews = watchedFromUser.get(currentContent);
            }
        }

        return mostWatchedContent;
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username can not be null or empty");
        }
        if (this.userService.getUsers().get(username) == null) {
            throw new UserNotFoundException("User could not be found in service.");
        }
        User searchedUser = this.userService.getUsers().get(username);
        HashMap<Category, Integer> timesWatchedCategory = new HashMap<>();
        var watchedFromUser = this.watchedContentOfUser.get(searchedUser);
        for (Category currentCategory : CATEGORIES) {
            if (numberByCategory(watchedFromUser, currentCategory) > 0) {
                timesWatchedCategory.put(currentCategory, numberByCategory(watchedFromUser, currentCategory));
            }
        }
        var sortedByWatches = sortByValue(timesWatchedCategory);

        List<Category> result = new ArrayList<>();

        for (Map.Entry<Category, Integer> current : sortedByWatches.entrySet()) {
            result.add(current.getKey());
        }

        return List.copyOf(result);
    }

    private UserService userService;
    private HashMap<User, List<Content>> contentsOfUser;
    private HashMap<User, HashMap<Content, Integer>> watchedContentOfUser;

    public HashMap<User, List<Content>> getContentsOfUser() {
        return contentsOfUser;
    }

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
    }

    private int numberByCategory(HashMap<Content, Integer> watched, Category category) {
        int result = 0;
        var contents = watched.keySet();

        for (var current : contents) {
            if (current.getMetadata().category().equals(category)) {
                result += watched.get(current);
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
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Category, Integer> temp = new LinkedHashMap<Category, Integer>();
        for (Map.Entry<Category, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}
