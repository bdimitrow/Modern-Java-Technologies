package bg.sofia.uni.fmi.mjt.newsfeed.dto;

import java.util.Arrays;
import java.util.Objects;

public class NewsFeedPage {
    private NewsFeed[] news;
    int pageIndex;
    int pageSize;

    public NewsFeedPage(NewsFeed[] news, int pageIndex, int pageSize) {
        this.news = news;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsFeedPage that = (NewsFeedPage) o;
        return pageIndex == that.pageIndex && pageSize == that.pageSize && Arrays.equals(news, that.news);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(pageIndex, pageSize);
        result = 31 * result + Arrays.hashCode(news);
        return result;
    }
}
