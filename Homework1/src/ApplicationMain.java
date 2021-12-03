import bg.sofia.uni.fmi.mjt.twitch.Twitch;
import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImpl;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.content.video.VideoImpl;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserImpl;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserServiceImpl;

import java.util.HashMap;
import java.util.List;
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
        Stream stream1 = twitch.startStream("firstUser", "stream11", Category.GAMES);

//        System.out.println(firstUser.getStatus());
//        System.out.println(secondUser.getStatus());
        var contents = twitch.getContentsOfUser();
        var u = contents.keySet();
//        for (var curr : u) {
//            var list = contents.get(curr);
//            for (var listElem : list) {
//                System.out.println("User: " + curr.getName() + " Content: " + listElem.getMetadata().title());
//            }
//        }
//        sleep(455);
        Video video = twitch.endStream("firstUser", stream1);
        Stream stream12 = twitch.startStream("secondUser", "stream12", Category.IRL);
        Video video2 = twitch.endStream("secondUser", stream12);
        Stream stream13 = twitch.startStream("firstUser", "stream13", Category.MUSIC);
        Video video3 = twitch.endStream("firstUser", stream13);
        Stream stream14 = twitch.startStream("firstUser", "stream14", Category.MUSIC);
        Video video4 = twitch.endStream("firstUser", stream14);


        twitch.watch("secondUser", video);
        twitch.watch("secondUser", video3);
        twitch.watch("secondUser", video2);
        twitch.watch("secondUser", video3);
        twitch.watch("secondUser", video3);
        twitch.watch("secondUser", video2);
        twitch.watch("thirdUser", video);
        twitch.watch("thirdUser", video);
        twitch.watch("thirdUser", video);
        twitch.watch("thirdUser", video);
        twitch.watch("thirdUser", video2);
        twitch.watch("thirdUser", video2);

//        contents = twitch.getContentsOfUser();
//        u = contents.keySet();
//        for (var curr : u) {
//            var list = contents.get(curr);
//            for (var listElem : list) {
//                System.out.println("User: " + curr.getName() + " Content: " + listElem.getNumberOfViews());
//            }
//        }
//        System.out.println(twitch.getMostWatchedStreamer().getName());
        System.out.println(twitch.getMostWatchedContent().getMetadata().title());
//        System.out.println("dadad: " + twitch.getMostWatchedContentFrom("secondUser").getMetadata().title());

//        List<Category> categoryList = twitch.getMostWatchedCategoriesBy("secondUser");
//        for (var a : categoryList) {
//            System.out.println(a.name());
//        }
    }

}
