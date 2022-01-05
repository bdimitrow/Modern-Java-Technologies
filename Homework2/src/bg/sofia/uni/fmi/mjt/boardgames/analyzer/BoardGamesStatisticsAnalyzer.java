package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardGamesStatisticsAnalyzer implements StatisticsAnalyzer {
    private final Collection<BoardGame> boardGames;
    private final Map<String, Integer> countByCategory = new HashMap<>();
    private final Map<String, Integer> totalPlayingTimeByCategory = new HashMap<>();

    public BoardGamesStatisticsAnalyzer(Collection<BoardGame> games) {
        this.boardGames = games;
        setCountByCategory();
        setTotalPlayingTimeByCategory();
    }

    @Override
    public List<String> getNMostPopularCategories(int n) {
        if(n < 0){
            throw new IllegalArgumentException("Invalid argument! The size of a list can not be negative.");
        }

        List<String> result = new ArrayList<>();

        Map<String, Integer> sortedCountByCategories = new HashMap<>();
        // Sort the list
        countByCategory.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedCountByCategories.put(x.getKey(), x.getValue()));

        while (n > 0) {
            for (Map.Entry<String, Integer> entry : sortedCountByCategories.entrySet()) {
                result.add(entry.getKey());
                --n;
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
        if(category == null || category.isEmpty() || !countByCategory.containsKey(category)){
            throw new IllegalArgumentException("Invalid argument! Such a category does not exist.");
        }
        return (double) totalPlayingTimeByCategory.get(category) / countByCategory.get(category);
    }

    @Override
    public Map<String, Double> getAveragePlayingTimeByCategory() {
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : countByCategory.entrySet()) {
            var currentCategory = entry.getKey();
            var averagePlayingTimeCurrentCategory = (double) totalPlayingTimeByCategory.get(currentCategory) / countByCategory.get(currentCategory);
            result.put(currentCategory, averagePlayingTimeCurrentCategory);
        }

        return result;
    }

    private void setCountByCategory() {
        for (var game : boardGames) {
            for (var category : game.categories()) {
                if (!countByCategory.containsKey(category)) {
                    countByCategory.putIfAbsent(category, 1);
                } else {
                    countByCategory.put(category, countByCategory.get(category) + 1);
                }
            }
        }
    }

    private void setTotalPlayingTimeByCategory() {
        for (var game : boardGames) {
            for (var category : game.categories()) {
                if (!totalPlayingTimeByCategory.containsKey(category)) {
                    totalPlayingTimeByCategory.putIfAbsent(category, game.playingTimeMins());
                } else {
                    countByCategory.put(category, countByCategory.get(category) + game.playingTimeMins());
                }
            }
        }
    }
}
