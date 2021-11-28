import bg.sofia.uni.fmi.mjt.twitch.Twitch;
import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImpl;
import bg.sofia.uni.fmi.mjt.twitch.content.video.VideoImpl;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserImpl;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class ApplicationMain {
    public static void main(String[] args) throws UserStreamingException, InterruptedException {
        User firstUser = new UserImpl("firstUser", UserStatus.OFFLINE);
        User secondUser = new UserImpl("secondUser", UserStatus.OFFLINE);
        User thirdUser = new UserImpl("thirdUser", UserStatus.OFFLINE);

        Map<String, User> users = new HashMap<>();
        users.put("firstUser", firstUser);
        users.put("secondUser", secondUser);
        users.put("thirdUser", thirdUser);
        UserService userService = new UserServiceImpl(users);

        Twitch twitch = new Twitch(userService);
        twitch.startStream("firstUser", "stream1", Category.GAMES);

        System.out.println(firstUser.getStatus());
        System.out.println(secondUser.getStatus());
        var contents = twitch.getContentsOfUser();
        var u = contents.keySet();
        for (var curr : u) {
            var list = contents.get(curr);
            for (var listElem : list) {
                System.out.println("User: " + curr.getName() + " Content: " + listElem.getMetadata().title());
            }
        }
        sleep(455);
        twitch.endStream("firstUser", new StreamImpl(new Metadata("stream1", Category.GAMES, firstUser)));

        twitch.watch("secondUser", new VideoImpl(new StreamImpl(new Metadata("stream1", Category.GAMES, firstUser))));

        contents = twitch.getContentsOfUser();
        u = contents.keySet();
        for (var curr : u) {
            var list = contents.get(curr);
            for (var listElem : list) {
                System.out.println("User: " + curr.getName() + " Content: " + listElem.getNumberOfViews());
            }
        }

    }

}
