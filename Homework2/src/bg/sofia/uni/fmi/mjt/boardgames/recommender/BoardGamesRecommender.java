package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.CollectionUtils;
import bg.sofia.uni.fmi.mjt.boardgames.FileException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BoardGamesRecommender implements Recommender {
    private final List<BoardGame> allBoardGames;
    private final Set<String> allStopWords;

    /**
     * Constructs an instance using the provided file names.
     *
     * @param datasetZipFile  ZIP file containing the board games dataset file
     * @param datasetFileName the name of the dataset file (inside the ZIP archive)
     * @param stopwordsFile   the stopwords file
     */
    public BoardGamesRecommender(Path datasetZipFile, String datasetFileName, Path stopwordsFile) {
        try {
            ZipFile zip = new ZipFile(datasetZipFile.toString());
            StringBuilder fileContent = new StringBuilder();
            for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = e.nextElement();
                if (!entry.isDirectory() && entry.getName().equals(datasetFileName)) {
                    fileContent = getTxtFiles(zip.getInputStream(entry));
                }
            }
            List<String> stringList = List.of(fileContent.toString().split(System.lineSeparator()));
            allBoardGames = stringList.stream().skip(1).map(BoardGame::of).toList();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(stopwordsFile.toFile()));
            allStopWords = bufferedReader.lines().collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException("Problem while processing files: ", e);
        }
    }

    private static StringBuilder getTxtFiles(InputStream in) {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException("Problem while reading file!", e);
        }
        return out;
    }

    /**
     * Constructs an instance using the provided Reader streams.
     *
     * @param dataset   Reader from which the dataset can be read
     * @param stopwords Reader from which the stopwords list can be read
     */
    public BoardGamesRecommender(Reader dataset, Reader stopwords) {
        try (
                var reader = new BufferedReader(dataset);
                var secondReader = new BufferedReader(stopwords)
        ) {
            allBoardGames = reader.lines().skip(1).map(BoardGame::of).toList();
            allStopWords = secondReader.lines().collect(Collectors.toSet());
        } catch (IOException e) {
            throw new FileException("Problem occurred while reading the file: ", e);
        }
    }

    @Override
    public Collection<BoardGame> getGames() {
        return Collections.unmodifiableCollection(this.allBoardGames);
    }

    public Collection<String> getStopWords() {
        return Collections.unmodifiableCollection(this.allStopWords);
    }

    @Override
    public List<BoardGame> getSimilarTo(BoardGame game, int n) {
        checkNull(game, "game");

        if (n < 0) {
            throw new IllegalArgumentException("Invalid argument! The size of a list can not be negative.");
        }

        return allBoardGames.stream()
                .filter(curr -> !curr.equals(game))
                .filter(curr -> curr.numberOfCommonCategories(game) >= 1)
                .sorted(Comparator.comparing(game::calculateDistance))
                .limit(n)
                .toList();
    }

    @Override
    public List<BoardGame> getByDescription(String... keywords) {
        return allBoardGames.stream()
                .filter(curr -> curr.numberOfCommonWords(allStopWords, keywords) >= 1)
                .sorted(Comparator.comparingInt(
                        (BoardGame boardGame) -> boardGame.numberOfCommonWords(allStopWords, keywords)).reversed()
                )
                .toList();
    }

    @Override
    public void storeGamesIndex(Writer writer) {
        checkNull(writer, "Writer");

        Map<String, List<Integer>> indexMap = createIndexMap();
        for (var mapEntry : indexMap.entrySet()) {
            try {
                writer.write(parseMapEntry(mapEntry));
                writer.flush();
            } catch (IOException e) {
                throw new FileException("Error while writing to a writer.", e);
            }
        }
    }

    private Map<String, List<Integer>> createIndexMap() {
        Map<String, List<Integer>> index = new HashMap<>();
        for (var game : allBoardGames) {
            var descriptionInLowerCase = CollectionUtils.toLowerCase(game.getDescriptionAsCollection());
            var descriptionWithoutStopwords = CollectionUtils.difference(descriptionInLowerCase, allStopWords);
            for (var word : descriptionWithoutStopwords) {
                if (index.containsKey(word)) {
                    List<Integer> inGames = index.get(word);
                    inGames.add(game.id());
                    index.put(word, inGames);
                } else {
                    List<Integer> newList = new ArrayList<>();
                    newList.add(game.id());
                    index.put(word, newList);
                }
            }
        }
        return index;
    }

    private String parseMapEntry(Map.Entry<String, List<Integer>> mapEntry) {
        StringBuilder result = new StringBuilder();
        result.append(mapEntry.getKey());
        result.append(": ");
        for (var id : mapEntry.getValue()) {
            result.append(id.toString()).append(", ");
        }

        String wholeString = result.toString();
        // returns the string without trailing comma
        return wholeString.substring(0, wholeString.length() - 2) + System.lineSeparator();
    }

    private void checkNull(Object o, String field) {
        if (o == null) {
            throw new IllegalArgumentException(String.format("%s cannot be null", field));
        }
    }
}
