package bg.sofia.uni.fmi.mjt.newsfeed.dto;

import java.util.Objects;

public class News {
    private final NewsSource source;
    private final String author;
    private final String title;
    private final String description;
    private final String url;
    private final String publishedAt;
    private final String content;

    public News(NewsSource source, String author, String title,
                String description, String url, String publishedAt, String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    @Override
    public String toString() {
        return "News{" +
                " author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(source, news.source) &&
                Objects.equals(author, news.author) &&
                Objects.equals(title, news.title) &&
                Objects.equals(description, news.description) &&
                Objects.equals(url, news.url) &&
                Objects.equals(publishedAt, news.publishedAt) &&
                Objects.equals(content, news.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, author, title, description, url, publishedAt, content);
    }
}
