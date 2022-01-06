package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardGamesRecommenderTest {
    private BoardGamesRecommender boardGamesRecommender;

    @BeforeEach
    void setUp() {
        boardGamesRecommender = new BoardGamesRecommender(boardGameListToBeTested(), stopWordsForTesting());
    }

    @Test
    void testGetSimilarToWithNull() {
        assertThrows(IllegalArgumentException.class, () -> boardGamesRecommender.getSimilarTo(null, 5));
    }

    @Test
    void testGetSimilarToWithNegativeN() {
        assertThrows(IllegalArgumentException.class, () -> boardGamesRecommender.getSimilarTo(
                new BoardGame(2, "test", "test", 1, 1, 1, 1,
                        List.of("test"), List.of("test")), -5));
    }

    @Test
    void testGetSimilarTo() {
        BoardGame forTest = new BoardGame(1, "Game 1", "this is game number one", 5, 14, 3, 240, List.of("Category1", "Category2", "Category3"), List.of("Mech1", "Mech2", "Mech3"));
        List<BoardGame> actual = boardGamesRecommender.getSimilarTo(forTest, 2);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.get(0).calculateDistance(forTest) >= actual.get(1).calculateDistance(forTest));
        assertEquals("Game 7", actual.get(0).name());
    }

    @Test
    void testGetByDescriptionNull() {
        assertThrows(IllegalArgumentException.class, () -> boardGamesRecommender.getByDescription(null));
    }

    @Test
    void testGetByDescriptionNoArguments() {
        List<BoardGame> actual = boardGamesRecommender.getByDescription();
        assertEquals(0, actual.size());
        assertTrue(actual.isEmpty());
    }

    @Test
    void testGetByDescriptionEmptyString() {
        List<BoardGame> actual = boardGamesRecommender.getByDescription("");
        assertEquals(0, actual.size());
        assertTrue(actual.isEmpty());
    }

    @Test
    void testGetByDescriptionSingleWord() {
        List<BoardGame> actual = boardGamesRecommender.getByDescription("kings");
        BoardGame test = BoardGame.of("7;2;8;2;Game 7;20;Category3;Mech3,Mech4,Mech5,Mech6;In Cathedral, he board. Players then place kings there");
        BoardGame test2 = BoardGame.of("9;4;13;2;Game 9;90;Category1;Mech3,Mech4;Although referred to as a sequel to El Grande, El Caballero shares few aspects of kings");
        assertEquals(2, actual.size());
        assertTrue(actual.contains(test));
        assertTrue(actual.contains(test2));
    }

    @Test
    void testGetByDescriptionMultipleWords() {
        List<BoardGame> actual = boardGamesRecommender.getByDescription("kings", "tou", "this");
        BoardGame test = BoardGame.of("7;2;8;2;Game 7;20;Category3;Mech3,Mech4,Mech5,Mech6;In Cathedral, he board. Players then place kings there");
        BoardGame test2 = BoardGame.of("9;4;13;2;Game 9;90;Category1;Mech3,Mech4;Although referred to as a sequel to El Grande, El Caballero shares few aspects of kings");
        BoardGame test3 = BoardGame.of("3;4;10;2;Game 3;60;Category2,Category3;Mech1,Mech2,Mech3,Mech4;Did tou say different");
        BoardGame test4 = BoardGame.of("4;4;12;2;Game 4;60;Category3;Mech5,Mech3,Mech2,Mech6;Who is tou the game inventor");
        BoardGame test5 = BoardGame.of("8;5;12;2;Game 8;120;Category2,Category4;Mech2;In this interesting offering from Warfrog, players be first part .");

        assertEquals(4, actual.size());
        assertTrue(actual.contains(test));
        assertTrue(actual.contains(test2));
        assertTrue(actual.contains(test3));
        assertTrue(actual.contains(test4));
        // Ensure that stop words are excluded
        assertFalse(actual.contains(test5));
    }

    private Reader boardGameListToBeTested() {
        String[] boardGames = {
                "1;5;14;3;Game 1;240;Category1,Category2,Category3;Mech1,Mech2,Mech3,Mech4,Mech5;Dis is a geim edno!",
                "2;4;12;3;Game 2;30;Category1,Category2;Mech1;this is game two a little bit different",
                "3;4;10;2;Game 3;60;Category2,Category3;Mech1,Mech2,Mech3,Mech4;Did tou say different",
                "4;4;12;2;Game 4;60;Category3;Mech5,Mech3,Mech2,Mech6;Who is tou the game inventor",
                "5;6;12;3;Game 5;90;Category1;Mech1t,Mech3,Mech5;In Acquire all you have to do in to conquer.",
                "6;6;12;2;Game 6;240;Category3,Category4;Mech1;In the ancient lands to trade with other cities in ",
                "7;2;8;2;Game 7;20;Category3;Mech3,Mech4,Mech5,Mech6;In Cathedral, he board. Players then place kings there",
                "8;5;12;2;Game 8;120;Category2,Category4;Mech2;In this interesting offering from Warfrog, players be first part .",
                "9;4;13;2;Game 9;90;Category1;Mech3,Mech4;Although referred to as a sequel to El Grande, El Caballero shares few aspects of kings"
        };
        return new StringReader(Arrays.stream(boardGames).collect(Collectors.joining(System.lineSeparator())));
    }

    private Reader stopWordsForTesting() {
        String[] stopWords = {"and", "or", "I", "me", "he", "you", "this"};
        return new StringReader(Arrays.stream(stopWords).collect(Collectors.joining(System.lineSeparator())));
    }
}