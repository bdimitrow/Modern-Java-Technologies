package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BoardGamesRecommender implements Recommender {
    private List<BoardGame> allBoardGames = new ArrayList<>();
    private Set<String> allStopWords = new LinkedHashSet<>();

    /**
     * Constructs an instance using the provided file names.
     *
     * @param datasetZipFile  ZIP file containing the board games dataset file
     * @param datasetFileName the name of the dataset file (inside the ZIP archive)
     * @param stopwordsFile   the stopwords file
     */
    BoardGamesRecommender(Path datasetZipFile, String datasetFileName, Path stopwordsFile) {

    }

    /**
     * Constructs an instance using the provided Reader streams.
     *
     * @param dataset   Reader from which the dataset can be read
     * @param stopwords Reader from which the stopwords list can be read
     */
    BoardGamesRecommender(Reader dataset, Reader stopwords) {
        try (
                var reader = new BufferedReader(dataset);
                var secondReader = new BufferedReader(stopwords)
        ) {
            allBoardGames = reader.lines().skip(1).map(BoardGame::of).toList();
            allStopWords = secondReader.lines().collect(Collectors.toSet());
        } catch (IOException e) {
            throw new IllegalStateException("Problem occurred while reading the file.");
        }
    }

    @Override
    public Collection<BoardGame> getGames() {
        return Collections.unmodifiableCollection(this.allBoardGames);
    }

    @Override
    public List<BoardGame> getSimilarTo(BoardGame game, int n) {
        if (game == null || !allBoardGames.contains(game)) {
            throw new IllegalArgumentException("Invalid argument! Such a game could not be found.");
        }
        if (n < 0) {
            throw new IllegalArgumentException("Invalid argument! The size of a list can not be negative.");
        }

        return allBoardGames.stream()
                .filter(curr -> Utils.intersection(curr.categories(), game.categories()).size() >= 1)
                .sorted(Comparator.comparing(game::calculateDistance))
                .limit(n)
                .toList();
    }

    @Override
    public List<BoardGame> getByDescription(String... keywords) {
        if (keywords == null) {
            throw new IllegalArgumentException("Invalid argument! No keywords were provided.");
        }

        return allBoardGames.stream()
                .filter(curr -> curr.differenceInWords(allStopWords, keywords) >= 1)
                .sorted(Comparator.comparingInt(
                        (BoardGame boardGame) -> boardGame.differenceInWords(allStopWords, keywords)).reversed()
                )
                .toList();
    }

    @Override
    public void storeGamesIndex(Writer writer) {
    }


}
