package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardGamesStatisticsAnalyzer implements StatisticsAnalyzer {
    private final Collection<BoardGame> boardGames;
    private final HashMap<String, Integer> countByCategory = new HashMap<>();
    private final Map<String, Integer> totalPlayingTimeByCategory = new HashMap<>();

    public BoardGamesStatisticsAnalyzer(Collection<BoardGame> games) {
        this.boardGames = games;
        setCountByCategory(games);
        setTotalPlayingTimeByCategory(games);
    }

    @Override
    public List<String> getNMostPopularCategories(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid argument! The size of a list can not be negative.");
        }
        if (n == 0) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        HashMap<String, Integer> sortedCountByCategories = sortByValue(countByCategory);
        for (Map.Entry<String, Integer> entry : sortedCountByCategories.entrySet()) {
            result.add(entry.getKey());
            if (--n == 0) {
                return result;
            }
        }

        return result;
    }

    @Override
    public double getAverageMinAge() {
        if (this.boardGames.isEmpty()) {
            return 0;
        }

        double sumOfMinAge = 0.0;
        for (var game : boardGames) {
            sumOfMinAge += game.minAge();
        }

        return sumOfMinAge / boardGames.size();
    }

    @Override
    public double getAveragePlayingTimeByCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Invalid argument! Such a category does not exist.");
        }
        if(!countByCategory.containsKey(category)){
            return 0.0;
        }

        return (double) totalPlayingTimeByCategory.get(category) / countByCategory.get(category);
    }

    @Override
    public Map<String, Double> getAveragePlayingTimeByCategory() {
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : countByCategory.entrySet()) {
            var currentCategory = entry.getKey();
            result.put(currentCategory, getAveragePlayingTimeByCategory(currentCategory));
        }

        return result;
    }

    private void setCountByCategory(Collection<BoardGame> allBoardGames) {
        for (var game : allBoardGames) {
            for (var category : game.categories()) {
                if (!countByCategory.containsKey(category)) {
                    countByCategory.put(category, 1);
                } else {
                    countByCategory.put(category, countByCategory.get(category) + 1);
                }
            }
        }
    }

    private void setTotalPlayingTimeByCategory(Collection<BoardGame> allBoardGames) {
        for (var game : allBoardGames) {
            for (var category : game.categories()) {
                if (!totalPlayingTimeByCategory.containsKey(category)) {
                    totalPlayingTimeByCategory.put(category, game.playingTimeMins());
                } else {
                    var newTotalPlayingTime = totalPlayingTimeByCategory.get(category) + game.playingTimeMins();
                    totalPlayingTimeByCategory.put(category, newTotalPlayingTime);
                }
            }
        }
    }

    private HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        return hm.entrySet()
                .stream()
                .sorted((i1, i2) -> i2.getValue().compareTo(i1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

}
