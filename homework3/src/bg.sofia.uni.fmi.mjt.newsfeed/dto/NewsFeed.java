package bg.sofia.uni.fmi.mjt.newsfeed.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class NewsFeed {
    @SerializedName("articles")
    private News[] news;

    public NewsFeed(News[] news) {
        this.news = news;
    }

    public News[] getNews() {
        return news;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsFeed newsFeed = (NewsFeed) o;
        return Arrays.equals(news, newsFeed.news);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(news);
    }
}
