package bg.sofia.uni.fmi.mjt.boardgames;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public record BoardGame(int id, String name, String description, int maxPlayers, int minAge, int minPlayers,
                        int playingTimeMins, Collection<String> categories, Collection<String> mechanics) {

    private static final String BOARDGAME_ATTRIBUTE_DELIMITER = ";";
    private static final String BOARDGAME_COLLECTION_DELIMITER = ",";
    private static final int BOARDGAME_ID = 0;
    private static final int BOARDGAME_MAX_PLAYERS = 1;
    private static final int BOARDGAME_MIN_AGE = 2;
    private static final int BOARDGAME_MIN_PLAYERS = 3;
    private static final int BOARDGAME_NAME = 4;
    private static final int BOARDGAME_PLAYING_TIME_MINS = 5;
    private static final int BOARDGAME_CATEGORY = 6;
    private static final int BOARDGAME_MECHANIC = 7;
    private static final int BOARDGAME_DESCRIPTION = 8;


    public static BoardGame of(String line) {
        final String[] tokens = line.split(BOARDGAME_ATTRIBUTE_DELIMITER);
        var categories = Arrays.asList(tokens[BOARDGAME_CATEGORY].split(BOARDGAME_COLLECTION_DELIMITER));
        var mechanics = Arrays.asList(tokens[BOARDGAME_MECHANIC].split(BOARDGAME_COLLECTION_DELIMITER));
        return new BoardGame(Integer.parseInt(tokens[BOARDGAME_ID]),
                tokens[BOARDGAME_NAME],
                tokens[BOARDGAME_DESCRIPTION],
                Integer.parseInt(tokens[BOARDGAME_MAX_PLAYERS]),
                Integer.parseInt(tokens[BOARDGAME_MIN_AGE]),
                Integer.parseInt(tokens[BOARDGAME_MIN_PLAYERS]),
                Integer.parseInt(tokens[BOARDGAME_PLAYING_TIME_MINS]),
                categories,
                mechanics);
    }

    // Calculates common words between description and keywords(stop words are excluded)
    public int numberOfCommonWords(Set<String> allStopWords, String... keywords) {
        return CollectionUtils.intersection(
                CollectionUtils.toLowerCase(this.getDescriptionAsCollection()),
                CollectionUtils.toLowerCase(CollectionUtils.difference(Arrays.asList(keywords), allStopWords))
        ).size();
    }

    public int numberOfCommonCategories(BoardGame other) {
        return CollectionUtils.intersection(this.categories(), other.categories()).size();
    }

    public Collection<String> getDescriptionAsCollection() {
        return List.of(this.description.split("[\\p{IsPunctuation}\\p{IsWhite_Space}]+"));
    }

    public double calculateDistance(BoardGame other) {
        return this.numericDistance(other) + this.nonNumericDistance(other);
    }

    private double numericDistance(BoardGame other) {
        return Math.sqrt(
                Math.pow((this.playingTimeMins - other.playingTimeMins), 2)
                        + Math.pow((this.maxPlayers - other.maxPlayers), 2)
                        + Math.pow((this.minAge - other.minAge), 2)
                        + Math.pow((this.minPlayers - other.minPlayers), 2));
    }

    private double nonNumericDistance(BoardGame other) {
        Collection<String> thisBoardGameWords = CollectionUtils.union(this.categories, this.mechanics);
        Collection<String> otherBoardGameWords = CollectionUtils.union(other.categories, other.mechanics);

        Collection<String> union = CollectionUtils.union(thisBoardGameWords, otherBoardGameWords);
        Collection<String> intersection = CollectionUtils.intersection(thisBoardGameWords, otherBoardGameWords);

        return union.size() - intersection.size();
    }
}
