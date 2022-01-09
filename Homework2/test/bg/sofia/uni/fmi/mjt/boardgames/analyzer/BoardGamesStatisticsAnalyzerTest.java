package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BoardGamesStatisticsAnalyzerTest {
    private static Collection<BoardGame> allBoardGames;
    private static BoardGamesStatisticsAnalyzer boardGamesStatisticsAnalyzer;

    @BeforeEach
    void setUp() {
        allBoardGames = List.of(
                new BoardGame(1, "game1", "world conquer game", 4, 18, 2, 10, List.of("category1", "category3"), List.of("mech1", "mech2")),
                new BoardGame(2, "game2", "map conquer fire", 2, 16, 2, 40, List.of("category1", "category3"), List.of("mech1")),
                new BoardGame(3, "game3", "tree conquer water", 3, 14, 1, 21, List.of("category1"), List.of("mech1")),
                new BoardGame(4, "game4", "world tree game", 4, 16, 2, 30, List.of("category3"), List.of("mech3", "mech2")),
                new BoardGame(5, "game5", "desk conquer pen", 5, 4, 3, 37, List.of("category4"), List.of("mech3", "mech2")),
                new BoardGame(6, "game6", "world air game", 6, 6, 4, 65, List.of("category5", "category1", "category4"), List.of("mech3", "mech2")),
                new BoardGame(7, "game7", "triangle square cube", 7, 8, 2, 60, List.of("category1"), List.of("mech1")),
                new BoardGame(8, "game8", "map conquer game", 8, 12, 6, 25, List.of("category2"), List.of("mech2")),
                new BoardGame(9, "game9", "water car paper", 6, 8, 2, 20, List.of("category2", "category3"), List.of("mech2")),
                new BoardGame(10, "game10", "world conquer charger", 4, 10, 1, 15, List.of("category4"), List.of("mech3"))
        );
        boardGamesStatisticsAnalyzer = new BoardGamesStatisticsAnalyzer(allBoardGames);
    }

    @Test
    void testGetNMostPopularCategoriesNegative() {
        assertThrows(IllegalArgumentException.class, () -> boardGamesStatisticsAnalyzer.getNMostPopularCategories(-5));
    }

    @Test
    void testGetNMostPopularCategoriesZero() {
        assertTrue(boardGamesStatisticsAnalyzer.getNMostPopularCategories(0).isEmpty());
        assertEquals(0, boardGamesStatisticsAnalyzer.getNMostPopularCategories(0).size());
    }

    @Test
    void testGetNMostPopularCategoriesGreater() {
        List<String> result = boardGamesStatisticsAnalyzer.getNMostPopularCategories(10);

        assertEquals(5, result.size());
        assertEquals(result.get(0), "category1");
        assertEquals(result.get(1), "category3");
        assertEquals(result.get(2), "category4");
        assertEquals(result.get(3), "category2");
        assertEquals(result.get(4), "category5");
    }

    @Test
    void testGetNMostPopularCategories() {
        List<String> result = boardGamesStatisticsAnalyzer.getNMostPopularCategories(2);

        assertEquals(2, result.size());
        assertEquals(result.get(0), "category1");
        assertEquals(result.get(1), "category3");
    }

    @Test
    void testGetAverageMinAge() {
        double actual = boardGamesStatisticsAnalyzer.getAverageMinAge();
        assertEquals(11.2, actual);
    }

    @Test
    void testGetAverageMinAgeEmptyList() {
        boardGamesStatisticsAnalyzer = new BoardGamesStatisticsAnalyzer(Collections.emptyList());
        double actual = boardGamesStatisticsAnalyzer.getAverageMinAge();

        assertEquals(0, actual);
    }

    @Test
    void testGetAveragePlayingTimeByCategoryNull() {
        assertThrows(IllegalArgumentException.class, () -> boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory(null));
    }

    @Test
    void testGetAveragePlayingTimeByCategoryEmpty() {
        assertThrows(IllegalArgumentException.class, () -> boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory(""));
    }


    @Test
    void testGetAveragePlayingTimeByCategoryNonExisting() {
        assertEquals(0, boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory("nonExisting"));
    }

    @Test
    void testGetAveragePlayingTimeByCategory() {
        assertEquals(39.2, boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory("category1"));
        assertEquals(22.5, boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory("category2"));
        assertEquals(25, boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory("category3"));
        assertEquals(39, boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory("category4"));
        assertEquals(65, boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory("category5"));
    }

    @Test
    void getAveragePlayingTimeByCategory() {
        Map<String, Double> result = boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory();
        assertTrue(result.containsKey("category1"));
        assertEquals(39.2, result.get("category1"));
    }
}