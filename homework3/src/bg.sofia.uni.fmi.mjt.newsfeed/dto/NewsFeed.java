package bg.sofia.uni.fmi.mjt.newsfeed.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsFeed {
    @SerializedName("articles")
    private List<News> news = new ArrayList<>();
    @SerializedName("totalResults")
    private int totalResults;

    public NewsFeed() {
    }

    public List<News> getNews() {
        return news;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsFeed newsFeed = (NewsFeed) o;
        return Objects.equals(news, newsFeed.news);
    }

    @Override
    public int hashCode() {
        return Objects.hash(news);
    }

    public void addNews(NewsFeed otherNews) {
        this.news.addAll(otherNews.getNews());
    }
}