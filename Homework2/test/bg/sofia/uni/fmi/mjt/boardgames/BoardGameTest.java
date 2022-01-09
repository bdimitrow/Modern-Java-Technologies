package bg.sofia.uni.fmi.mjt.boardgames;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardGameTest {

    @Test
    void testOf() {
        BoardGame expected = new BoardGame(1, "testName", "testDescription", 1, 2, 3, 4, List.of("cat1"), List.of("mech1"));
        BoardGame actual = BoardGame.of("1;1;2;3;testName;4;cat1;mech1;testDescription");

        assertEquals(expected, actual);
    }

    @Test
    void numberOfCommonWords() {
        BoardGame game = BoardGame.of("1;1;2;3;testName;4;cat1;mech1;testDescription1 this is game one");
        Set<String> stopWords = Set.of("this", "is");
        String[] keywords = {"this", "game", "test", "one", "two"};
        // should return 2 (game one)
        assertEquals(2, game.numberOfCommonWords(stopWords, keywords));
    }

    @Test
    void numberOfCommonCategories() {
        BoardGame game1 = new BoardGame(1, "testName", "testDescription", 1, 2, 3, 4,
                List.of("cat1", "cat4"), List.of("mech1"));

        BoardGame game2 = BoardGame.of("2;1;2;3;testName;4;cat1,cat2;mech1;testDescription");
        assertEquals(1, game1.numberOfCommonCategories(game2));

        game2 = BoardGame.of("2;1;2;3;testName;4;cat3,cat2;mech1;testDescription");
        assertEquals(0, game1.numberOfCommonCategories(game2));

        game2 = BoardGame.of("2;1;2;3;testName;4;cat4,cat1,cat2;mech1;testDescription");
        assertEquals(2, game1.numberOfCommonCategories(game2));
    }

    @Test
    void getDescriptionAsCollection() {
        BoardGame game = BoardGame.of("1;1;2;3;testName;4;cat1,cat2;mech1;testDescription DA ne OkkaY");
        List<String> expected = List.of("testDescription", "DA", "ne", "OkkaY");

        assertEquals(expected, game.getDescriptionAsCollection());
    }

    @Test
    void calculateDistance() {
        BoardGame game1 = BoardGame.of("1;1;2;3;testName;4;cat1,cat2;mech1;testDescription");
        BoardGame game2 = BoardGame.of("2;1;2;3;testName;4;cat1,cat2;mech1;testDescription");
        assertEquals(0, game1.calculateDistance(game2));

        game2 = BoardGame.of("2;1;4;3;testName;4;cat1,cat5,cat2;mech1,mech2;testDescription");
        assertEquals(4, game1.calculateDistance(game2));

    }
}