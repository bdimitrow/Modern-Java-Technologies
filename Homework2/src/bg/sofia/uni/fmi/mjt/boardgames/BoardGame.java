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


    // [...]
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

    public int differenceInWords(Set<String> allStopWords, String... keywords) {
        return Utils.intersection(this.getDescriptionAsCollection().stream().map(String::toLowerCase).toList(),
                Utils.difference(Arrays.asList(keywords), allStopWords).stream().map(String::toLowerCase).toList()
        ).size();
    }

    private Collection<String> getDescriptionAsCollection() {
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
        Collection<String> thisBoardGameWords = Utils.union(this.categories, this.mechanics);
        Collection<String> otherBoardGameWords = Utils.union(other.categories, other.mechanics);

        Collection<String> union = Utils.union(thisBoardGameWords, otherBoardGameWords);
        Collection<String> intersection = Utils.union(thisBoardGameWords, otherBoardGameWords);

        return union.size() - intersection.size();
    }

}
