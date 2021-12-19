package bg.sofia.uni.fmi.mjt.game.recommender;

import bg.sofia.uni.fmi.mjt.game.recommender.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameRecommenderTest {
    private static List<Game> allGames;
    private static GameRecommender gameRecommender;

    @BeforeAll
    public static void setUp() throws IOException {
        Reader gamesStream = gameListToBeTested();

        try (BufferedReader reader = new BufferedReader(gamesStream)) {
            allGames = reader.lines()
                    .skip(1)
                    .map(Game::of)
                    .toList();
        }

        gameRecommender = new GameRecommender(gameListToBeTested());
    }

    @org.junit.jupiter.api.Test
    void getAllGames() {
        assertEquals(gameRecommender.getAllGames().size(), 10);
    }

    @org.junit.jupiter.api.Test
    void getGamesReleasedAfter() {
        Game expected = Game.of("Red Dead Redemption 2,Xbox One,26-Oct-2018,Developed by the creators of Grand Theft Auto V and Red Dead Redemption Red Dead Redemption 2 is an epic tale of life in Americaâ€™s unforgiving heartland. The gameâ€™s vast and atmospheric world also provides the foundation for a brand new online multiplayer experience. America 1899. The end of the Wild West era has begun. After a robbery goes badly wrong in the western town of Blackwater Arthur Morgan and the Van der Linde gang are forced to flee. With federal agents and the best bounty hunters in the nation massing on their heels the gang has to rob steal and fight their way across the rugged heartland of America in order to survive. As deepening internal fissures threaten to tear the gang apart Arthur must make a choice between his own ideals and loyalty to the gang that raised him. [Rockstar],97,8");
        assertEquals(gameRecommender.getGamesReleasedAfter(LocalDate.of(2017, 2, 2)).get(0), expected);
    }

    @org.junit.jupiter.api.Test
    void getTopNUserRatedGames() {
        Game one = Game.of("The Legend of Zelda: Ocarina of Time,Nintendo 64,23-Nov-1998,As a young boy Link is tricked by Ganondorf the King of the Gerudo Thieves. The evil human uses Link to gain access to the Sacred Realm where he places his tainted hands on Triforce and transforms the beautiful Hyrulean landscape into a barren wasteland. Link is determined to fix the problems he helped to create so with the help of Rauru he travels through time gathering the powers of the Seven Sages.,99,9.1");
        Game two = Game.of("Super Mario Galaxy,Wii,12-Nov-2007,[Metacritic's 2007 Wii Game of the Year] The ultimate Nintendo hero is taking the ultimate step ... out into space. Join Mario as he ushers in a new era of video games defying gravity across all the planets in the galaxy. When some creature escapes into space with Princess Peach Mario gives chase exploring bizarre planets all across the galaxy. Mario Peach and enemies new and old are here. Players run jump and battle enemies as they explore all the planets in the galaxy. Since this game makes full use of all the features of the Wii Remote players have to do all kinds of things to succeed: pressing buttons swinging the Wii Remote and the Nunchuk and even pointing at and dragging things with the pointer. Since he's in space Mario can perform mind-bending jumps unlike anything he's done before. He'll also have a wealth of new moves that are all based around tilting pointing and shaking the Wii Remote. Shake tilt and point! Mario takes advantage of all the unique aspects of the Wii Remote and Nunchuk controller unleashing new moves as players shake the controller and even point at and drag items with the pointer. [Nintendo],97,9.1");
        Game three = Game.of("Super Mario Galaxy 2,Wii,23-May-2010,Super Mario Galaxy 2 the sequel to the galaxy-hopping original game includes the gravity-defying physics-based exploration from the first game but is loaded with entirely new galaxies and features to challenge players. On some stages Mario can pair up with his dinosaur buddy Yoshi and use his tongue to grab items and spit them back at enemies. Players can also have fun with new items such as a drill that lets our hero tunnel through solid rock. [Nintendo],97,9.1");
        assertIterableEquals(gameRecommender.getTopNUserRatedGames(3), List.of(one, two, three));
        assertThrows(IllegalArgumentException.class, () -> gameRecommender.getTopNUserRatedGames(-2));
        assertEquals(gameRecommender.getTopNUserRatedGames(0), new ArrayList<>());
    }

    @org.junit.jupiter.api.Test
    void getYearsWithTopScoringGames() {
        assertIterableEquals(gameRecommender.getYearsWithTopScoringGames(98), List.of(1998, 2000, 2008, 1999));
        assertFalse(gameRecommender.getYearsWithTopScoringGames(98).contains(2014));
    }

    @org.junit.jupiter.api.Test
    void getAllNamesOfGamesReleasedIn() {
        assertEquals(gameRecommender.getAllNamesOfGamesReleasedIn(2008), "Grand Theft Auto IV, Grand Theft Auto IV");
        assertEquals(gameRecommender.getAllNamesOfGamesReleasedIn(2022), "");
    }

    @org.junit.jupiter.api.Test
    void getHighestUserRatedGameByPlatform() {
        assertEquals(gameRecommender.getHighestUserRatedGameByPlatform("PlayStation 3"), Game.of("Grand Theft Auto V,PlayStation 3,17-Sep-2013,Los Santos is a vast sun-soaked metropolis full of self-help gurus starlets and once-important formerly-known-as celebrities. The city was once the envy of the Western world but is now struggling to stay afloat in an era of economic uncertainty and reality TV. Amidst the chaos three unique criminals plot their own chances of survival and success: Franklin a former street gangster in search of real opportunities and serious cheddar; Michael a professional ex-con whose retirement is a lot less rosy than he hoped it would be; and Trevor a violent maniac driven by the chance of a cheap high and the next big score. Quickly running out of options the crew risks it all in a sequence of daring and dangerous heists that could set them up for life.,97,8.3"));
        assertThrows(NoSuchElementException.class, () -> gameRecommender.getHighestUserRatedGameByPlatform("PSP"));
    }

    @org.junit.jupiter.api.Test
    void getAllGamesByPlatform() {
        Map<String, Set<Game>> expected = Map.of(
                "Nintendo 64", Set.of(
                        Game.of("The Legend of Zelda: Ocarina of Time,Nintendo 64,23-Nov-1998,As a young boy Link is tricked by Ganondorf the King of the Gerudo Thieves. The evil human uses Link to gain access to the Sacred Realm where he places his tainted hands on Triforce and transforms the beautiful Hyrulean landscape into a barren wasteland. Link is determined to fix the problems he helped to create so with the help of Rauru he travels through time gathering the powers of the Seven Sages.,99,9.1")
                ),
                "Playtation", Set.of(
                        Game.of("Tony Hawk's Pro Skater 2,PlayStation,20-Sep-2000,As most major publishers' development efforts shift to any number of next-generation platforms Tony Hawk 2 will likely stand as one of the last truly fantastic games to be released on the PlayStation.,98,7.4")
                ),
                "Playstation 3", Set.of(
                        Game.of("Grand Theft Auto IV,PlayStation 3,29-Apr-2008,[Metacritic's 2008 PS3 Game of the Year; Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.7"),
                        Game.of("Grand Theft Auto V,PlayStation 3,17-Sep-2013,Los Santos is a vast sun-soaked metropolis full of self-help gurus starlets and once-important formerly-known-as celebrities. The city was once the envy of the Western world but is now struggling to stay afloat in an era of economic uncertainty and reality TV. Amidst the chaos three unique criminals plot their own chances of survival and success: Franklin a former street gangster in search of real opportunities and serious cheddar; Michael a professional ex-con whose retirement is a lot less rosy than he hoped it would be; and Trevor a violent maniac driven by the chance of a cheap high and the next big score. Quickly running out of options the crew risks it all in a sequence of daring and dangerous heists that could set them up for life.,97,8.3")
                ),
                "Dreamcast", Set.of(
                        Game.of("SoulCalibur,Dreamcast,08-Sep-1999,This is a tale of souls and swords transcending the world and all its history told for all eternity... The greatest weapons-based fighter returns this time on Sega Dreamcast. Soul Calibur unleashes incredible graphics fantastic fighters and combos so amazing they'll make your head spin!,98,8.4")
                ),
                "Xbox One", Set.of(
                        Game.of("Red Dead Redemption 2,Xbox One,26-Oct-2018,Developed by the creators of Grand Theft Auto V and Red Dead Redemption Red Dead Redemption 2 is an epic tale of life in Americaâ€™s unforgiving heartland. The gameâ€™s vast and atmospheric world also provides the foundation for a brand new online multiplayer experience. America 1899. The end of the Wild West era has begun. After a robbery goes badly wrong in the western town of Blackwater Arthur Morgan and the Van der Linde gang are forced to flee. With federal agents and the best bounty hunters in the nation massing on their heels the gang has to rob steal and fight their way across the rugged heartland of America in order to survive. As deepening internal fissures threaten to tear the gang apart Arthur must make a choice between his own ideals and loyalty to the gang that raised him. [Rockstar],97,8")
                ),
                "Xbox 360", Set.of(
                        Game.of("Grand Theft Auto IV,Xbox 360,29-Apr-2008,[Metacritic's 2008 Xbox 360 Game of the Year; Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.9")

                ),
                "Wii", Set.of(
                        Game.of("Super Mario Galaxy,Wii,12-Nov-2007,[Metacritic's 2007 Wii Game of the Year] The ultimate Nintendo hero is taking the ultimate step ... out into space. Join Mario as he ushers in a new era of video games defying gravity across all the planets in the galaxy. When some creature escapes into space with Princess Peach Mario gives chase exploring bizarre planets all across the galaxy. Mario Peach and enemies new and old are here. Players run jump and battle enemies as they explore all the planets in the galaxy. Since this game makes full use of all the features of the Wii Remote players have to do all kinds of things to succeed: pressing buttons swinging the Wii Remote and the Nunchuk and even pointing at and dragging things with the pointer. Since he's in space Mario can perform mind-bending jumps unlike anything he's done before. He'll also have a wealth of new moves that are all based around tilting pointing and shaking the Wii Remote. Shake tilt and point! Mario takes advantage of all the unique aspects of the Wii Remote and Nunchuk controller unleashing new moves as players shake the controller and even point at and drag items with the pointer. [Nintendo],97,9.1"),
                        Game.of("Super Mario Galaxy 2,Wii,23-May-2010,Super Mario Galaxy 2 the sequel to the galaxy-hopping original game includes the gravity-defying physics-based exploration from the first game but is loaded with entirely new galaxies and features to challenge players. On some stages Mario can pair up with his dinosaur buddy Yoshi and use his tongue to grab items and spit them back at enemies. Players can also have fun with new items such as a drill that lets our hero tunnel through solid rock. [Nintendo],97,9.1")
                )
        );
        Map<String, Set<Game>> actual = gameRecommender.getAllGamesByPlatform();
        Set<Map.Entry<String, Set<Game>>> entries = actual.entrySet();

        for (var entry : entries) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }

//        assertArrayEquals(new Stream[]{Arrays.stream(expected.keySet().toArray(new String[0])).sorted()}, new Stream[]{Arrays.stream(actual.keySet().toArray(new String[0])).sorted()});

    }

    @org.junit.jupiter.api.Test
    void getYearsActive() {
        assertEquals(gameRecommender.getYearsActive("Wii"), 3);
        assertEquals(gameRecommender.getYearsActive("PlayStation 3"), 5);
        assertEquals(gameRecommender.getYearsActive("dummy"), 0);
    }

    @org.junit.jupiter.api.Test
    void getGamesSimilarTo() {
        Game one = Game.of("Grand Theft Auto V,PlayStation 3,17-Sep-2013,Los Santos is a vast sun-soaked metropolis full of self-help gurus starlets and once-important formerly-known-as celebrities. The city was once the envy of the Western world but is now struggling to stay afloat in an era of economic uncertainty and reality TV. Amidst the chaos three unique criminals plot their own chances of survival and success: Franklin a former street gangster in search of real opportunities and serious cheddar; Michael a professional ex-con whose retirement is a lot less rosy than he hoped it would be; and Trevor a violent maniac driven by the chance of a cheap high and the next big score. Quickly running out of options the crew risks it all in a sequence of daring and dangerous heists that could set them up for life.,97,8.3");
        Game two = Game.of("Grand Theft Auto IV,PlayStation 3,29-Apr-2008,[Metacritic's 2008 PS3 Game of the Year; Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.7");
        Game three = Game.of("Grand Theft Auto IV,Xbox 360,29-Apr-2008,[Metacritic's 2008 Xbox 360 Game of the Year; Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.9");
        assertIterableEquals(gameRecommender.getGamesSimilarTo("criminal"), List.of(two, three, one));
    }

    public static Reader gameListToBeTested() {
        String[] games = {
                "",
                "The Legend of Zelda: Ocarina of Time,Nintendo 64,23-Nov-1998,As a young boy Link is tricked by Ganondorf the King of the Gerudo Thieves. The evil human uses Link to gain access to the Sacred Realm where he places his tainted hands on Triforce and transforms the beautiful Hyrulean landscape into a barren wasteland. Link is determined to fix the problems he helped to create so with the help of Rauru he travels through time gathering the powers of the Seven Sages.,99,9.1",
                "Tony Hawk's Pro Skater 2,PlayStation,20-Sep-2000,As most major publishers' development efforts shift to any number of next-generation platforms Tony Hawk 2 will likely stand as one of the last truly fantastic games to be released on the PlayStation.,98,7.4",
                "Grand Theft Auto IV,PlayStation 3,29-Apr-2008,[Metacritic's 2008 PS3 Game of the Year; Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.7",
                "SoulCalibur,Dreamcast,08-Sep-1999,This is a tale of souls and swords transcending the world and all its history told for all eternity... The greatest weapons-based fighter returns this time on Sega Dreamcast. Soul Calibur unleashes incredible graphics fantastic fighters and combos so amazing they'll make your head spin!,98,8.4",
                "Grand Theft Auto IV,Xbox 360,29-Apr-2008,[Metacritic's 2008 Xbox 360 Game of the Year; Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.9",
                "Super Mario Galaxy,Wii,12-Nov-2007,[Metacritic's 2007 Wii Game of the Year] The ultimate Nintendo hero is taking the ultimate step ... out into space. Join Mario as he ushers in a new era of video games defying gravity across all the planets in the galaxy. When some creature escapes into space with Princess Peach Mario gives chase exploring bizarre planets all across the galaxy. Mario Peach and enemies new and old are here. Players run jump and battle enemies as they explore all the planets in the galaxy. Since this game makes full use of all the features of the Wii Remote players have to do all kinds of things to succeed: pressing buttons swinging the Wii Remote and the Nunchuk and even pointing at and dragging things with the pointer. Since he's in space Mario can perform mind-bending jumps unlike anything he's done before. He'll also have a wealth of new moves that are all based around tilting pointing and shaking the Wii Remote. Shake tilt and point! Mario takes advantage of all the unique aspects of the Wii Remote and Nunchuk controller unleashing new moves as players shake the controller and even point at and drag items with the pointer. [Nintendo],97,9.1",
                "Super Mario Galaxy 2,Wii,23-May-2010,Super Mario Galaxy 2 the sequel to the galaxy-hopping original game includes the gravity-defying physics-based exploration from the first game but is loaded with entirely new galaxies and features to challenge players. On some stages Mario can pair up with his dinosaur buddy Yoshi and use his tongue to grab items and spit them back at enemies. Players can also have fun with new items such as a drill that lets our hero tunnel through solid rock. [Nintendo],97,9.1",
                "Red Dead Redemption 2,Xbox One,26-Oct-2018,Developed by the creators of Grand Theft Auto V and Red Dead Redemption Red Dead Redemption 2 is an epic tale of life in Americaâ€™s unforgiving heartland. The gameâ€™s vast and atmospheric world also provides the foundation for a brand new online multiplayer experience. America 1899. The end of the Wild West era has begun. After a robbery goes badly wrong in the western town of Blackwater Arthur Morgan and the Van der Linde gang are forced to flee. With federal agents and the best bounty hunters in the nation massing on their heels the gang has to rob steal and fight their way across the rugged heartland of America in order to survive. As deepening internal fissures threaten to tear the gang apart Arthur must make a choice between his own ideals and loyalty to the gang that raised him. [Rockstar],97,8",
                "Grand Theft Auto V,Xbox One,18-Nov-2014,Grand Theft Auto 5 melds storytelling and gameplay in unique ways as players repeatedly jump in and out of the lives of the game's three protagonists playing all sides of the game's interwoven story.,97,7.9",
                "Grand Theft Auto V,PlayStation 3,17-Sep-2013,Los Santos is a vast sun-soaked metropolis full of self-help gurus starlets and once-important formerly-known-as celebrities. The city was once the envy of the Western world but is now struggling to stay afloat in an era of economic uncertainty and reality TV. Amidst the chaos three unique criminals plot their own chances of survival and success: Franklin a former street gangster in search of real opportunities and serious cheddar; Michael a professional ex-con whose retirement is a lot less rosy than he hoped it would be; and Trevor a violent maniac driven by the chance of a cheap high and the next big score. Quickly running out of options the crew risks it all in a sequence of daring and dangerous heists that could set them up for life.,97,8.3"
        };

        return new StringReader(Arrays.stream(games).collect(Collectors.joining(System.lineSeparator())));
    }

}