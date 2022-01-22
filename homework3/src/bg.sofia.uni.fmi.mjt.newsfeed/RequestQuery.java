package bg.sofia.uni.fmi.mjt.newsfeed;

import java.util.Optional;

public class RequestQuery {
    private final String keywords;
    private final String categories;
    private final String countries;
    private final int pageSize;
    private final int page;

    public RequestQuery(RequestQueryBuilder builder) {
        this.keywords = builder.keywords;
        this.categories = builder.categories;
        this.countries = builder.countries;
        this.pageSize = builder.pageSize;
        this.page = builder.page;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getCategories() {
        return categories;
    }

    public String getCountries() {
        return countries;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
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
        private int page;

        public RequestQueryBuilder keywords(String... keywords) {
            String concated = String.join("+", keywords);
            this.keywords = concated;
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
            pageSize.ifPresentOrElse(integer -> this.pageSize = integer, () -> this.pageSize = 20);
            return this;
        }

        public RequestQueryBuilder page(int page) {
            this.page = page;
            return this;
        }

        public RequestQuery build() {
            RequestQuery query = new RequestQuery(this);
            return query;
        }
    }
}
