package bg.sofia.uni.fmi.mjt.newsfeed;

import java.util.Optional;

public class RequestQuery {
    private static final int PAGE_SIZE_DEFAULT_VALUE = 20;

    private final String keywords;
    private final String categories;
    private final String countries;
    private final int pageSize;

    public RequestQuery(RequestQueryBuilder builder) {
        this.keywords = builder.keywords;
        this.categories = builder.categories;
        this.countries = builder.countries;
        this.pageSize = builder.pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("q=").append(keywords);
        if (categories != null) {
            result.append("&category=").append(categories);
        }
        if (countries != null) {
            result.append("&country=").append(countries);
        }


        return result.append("&pageSize=").append(pageSize).toString();
    }

    public static class RequestQueryBuilder {
        private String keywords;
        private String categories;
        private String countries;
        private int pageSize;

        public RequestQueryBuilder keywords(String... keywords) {
            this.keywords = String.join("+", keywords);
            return this;
        }

        public RequestQueryBuilder categories(Optional<String[]> categories) {
            categories.ifPresent(cat -> this.categories = String.join("+", cat));
            return this;
        }

        public RequestQueryBuilder countries(Optional<String[]> countries) {
            countries.ifPresent(country -> this.countries = String.join("+", country));
            return this;
        }

        public RequestQueryBuilder pageSize(Optional<Integer> pageSize) {
            pageSize.ifPresentOrElse(integer -> this.pageSize = integer, () -> this.pageSize = PAGE_SIZE_DEFAULT_VALUE);
            return this;
        }

        public RequestQuery build() {
            return new RequestQuery(this);
        }
    }
}
