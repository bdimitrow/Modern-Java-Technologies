package bg.sofia.uni.fmi.mjt.newsfeed;

//f17a089db93b47de8d13c989a67fb232

import bg.sofia.uni.fmi.mjt.newsfeed.dto.NewsFeed;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsFeedClientException;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class NewsFeedClient {
    private static final String API_KEY = "f17a089db93b47de8d13c989a67fb232";
    private static final String API_KEY_QUERY = "&apiKey=" + API_KEY;
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";
    private static final Gson GSON = new Gson();

    private final HttpClient newsFeedClient;
    private String apiKey;

    public NewsFeedClient(HttpClient newsFeedClient) {
        this.newsFeedClient = newsFeedClient;
    }

    public NewsFeedClient(HttpClient newsFeedClient, String apiKey) {
        this.newsFeedClient = newsFeedClient;
        this.apiKey = apiKey;
    }

    public NewsFeed getNewsFeed(String[] keywords,
                                Optional<String[]> categories,
                                Optional<String[]> countries,
                                Optional<Integer> pageSize,
                                Optional<Integer> page) throws NewsFeedClientException {
        if (keywords == null) {
            throw new NewsFeedClientException("Bad arguments. ");
        }
        HttpResponse<String> response;

        try {
            URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH,
                    buildQuery(keywords, categories, countries), null);

            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

            response = newsFeedClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new NewsFeedClientException("Error while retrieving news: " + e.getMessage(), e);
        }

        if (response.statusCode() == HTTP_OK) {
            return GSON.fromJson(response.body(), NewsFeed.class);
        }

        if (response.statusCode() == HTTP_BAD_REQUEST) {
            throw new NewsFeedClientException("Bad request was sent. ");
        }

        if (response.statusCode() == HTTP_UNAUTHORIZED) {
            throw new NewsFeedClientException("Unauthorized. Api key is missing or incorrect. ");
        }
        throw new NewsFeedClientException("Unexpected code received from service. ");
    }

    public NewsFeed getNewsFeed(String[] keywords,
                                Optional<String[]> categories,
                                Optional<String[]> countries,
                                Optional<Integer> pageSize) throws NewsFeedClientException {
        if (keywords == null) {
            throw new NewsFeedClientException("Bad arguments. ");
        }
        if (pageSize.isEmpty()) {
            pageSize = Optional.of(20); // Default value for page size.
        }

        HttpResponse<String> response;
        int totalResults = 0;
        int currentPage = 1;

        NewsFeed newsFeedResult = new NewsFeed();
        try {
            RequestQuery rq = new RequestQuery.RequestQueryBuilder()
                    .keywords(keywords)
                    .categories(categories)
                    .countries(countries)
                    .pageSize(pageSize)
                    .build();

            do {
                URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH,
                        rq.toString() + "&page=" + currentPage + API_KEY_QUERY, null);
                ++currentPage;

                HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
                response = newsFeedClient.send(request, HttpResponse.BodyHandlers.ofString());

                NewsFeed newsFeed = GSON.fromJson(response.body(), NewsFeed.class);
                totalResults = newsFeed.getTotalResults();
                newsFeedResult.addNews(newsFeed);
            } while ((currentPage - 1) * pageSize.get() < totalResults);
        } catch (Exception e) {
            throw new NewsFeedClientException("Error while retrieving news: " + e.getMessage(), e);
        }

        if (response.statusCode() == HTTP_OK) {
            return newsFeedResult;
        }

        if (response.statusCode() == HTTP_BAD_REQUEST) {
            throw new NewsFeedClientException("Bad request was sent. ");
        }

        if (response.statusCode() == HTTP_UNAUTHORIZED) {
            throw new NewsFeedClientException("Unauthorized. Api key is missing or incorrect. ");
        }
        throw new NewsFeedClientException("Unexpected code received from service. ");
    }


    String buildQuery(String[] keywords, Optional<String[]> categories, Optional<String[]> countries) {
        StringBuilder result = new StringBuilder("q=");
        for (var keyword : keywords) {
            result.append(keyword).append("+");
        }
        result.deleteCharAt(result.length() - 1);

        if (categories.isPresent()) {
            result.append("&category=");
            var allCategories = categories.get();
            for (var category : allCategories) {
                result.append(category).append("+");
            }
            result.deleteCharAt(result.length() - 1);
        }

        if (countries.isPresent()) {
            result.append("&country=");
            var allCountries = countries.get();
            for (var country : allCountries) {
                result.append(country).append("+");
            }
            result.deleteCharAt(result.length() - 1);
        }

//        result.append("&pageSize=40");
        result.append("&apiKey=" + API_KEY);

        return result.toString();
    }

    public static void main(String[] args) throws NewsFeedClientException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        NewsFeedClient newsFeedClient = new NewsFeedClient(client);
        String[] keywords = {"president information"};
        String[] categories = {"business", "health"};
        String[] countries = {"us", "bg"};
//        NewsFeed result = newsFeedClient.getNewsFeed(keywords, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        NewsFeed result = newsFeedClient.getNewsFeed(keywords, Optional.of(categories), Optional.of(countries), Optional.of(15));

        System.out.println("================");
        for (var a : result.getNews()) {
            System.out.println(a.toString());
        }
    }
}
