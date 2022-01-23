package bg.sofia.uni.fmi.mjt.newsfeed;

//f17a089db93b47de8d13c989a67fb232

import bg.sofia.uni.fmi.mjt.newsfeed.dto.NewsFeed;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsFeedClientException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ServerErrorException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.SourcesTooManyRequestsException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.UnauthorizedException;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class NewsFeedClient {
    private static final String API_KEY = "f17a089db93b47de8d13c989a67fb232";
    private static final String API_KEY_QUERY = "&apiKey=" + API_KEY;
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";
    private static final Gson GSON = new Gson();

    private static final int TOO_MANY_REQUESTS_STATUS_CODE = 419;


    private final HttpClient newsFeedClient;

    public NewsFeedClient(HttpClient newsFeedClient) {
        this.newsFeedClient = newsFeedClient;
    }

    public NewsFeed getNewsFeed(String[] keywords,
                                Optional<String[]> categories,
                                Optional<String[]> countries,
                                Optional<Integer> pageSize)
            throws NewsFeedClientException, SourcesTooManyRequestsException,
            ServerErrorException, UnauthorizedException, BadRequestException,
            URISyntaxException, IOException, InterruptedException {

        if (keywords == null) {
            throw new BadRequestException("Bad arguments. ");
        }

        NewsFeed newsFeedResult = new NewsFeed();
        RequestQuery rq = new RequestQuery.RequestQueryBuilder()
                .keywords(keywords)
                .categories(categories)
                .countries(countries)
                .pageSize(pageSize)
                .build();

        HttpResponse<String> response;
        int totalResults;
        int currentPage = 1;
        try {
            do {
                URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH,
                        rq.toString() + "&page=" + currentPage + API_KEY_QUERY, null);

                HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
                response = newsFeedClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != HTTP_OK) {
                    errorHandler(response);
                }

                NewsFeed newsFeed = GSON.fromJson(response.body(), NewsFeed.class);
                totalResults = newsFeed.getTotalResults();
                newsFeedResult.addNews(newsFeed);
            } while (currentPage++ * rq.getPageSize() < totalResults);
        } catch (BadRequestException | UnauthorizedException |
                SourcesTooManyRequestsException | ServerErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new NewsFeedClientException("Error while retrieving news: " + e.getMessage(), e);
        }

        newsFeedResult.setTotalResults(totalResults);
        return newsFeedResult;
    }

    private void errorHandler(HttpResponse<String> response)
            throws NewsFeedClientException, UnauthorizedException,
            SourcesTooManyRequestsException, ServerErrorException, BadRequestException {
        if (response.statusCode() == HTTP_BAD_REQUEST) {
            throw new BadRequestException("Bad request was sent. ");
        }
        if (response.statusCode() == HTTP_UNAUTHORIZED) {
            throw new UnauthorizedException("Your API key was missing from the request, or wasn't correct. ");
        }
        if (response.statusCode() == TOO_MANY_REQUESTS_STATUS_CODE) {
            throw new SourcesTooManyRequestsException("You made too many requests within a " +
                    "window of time and have been rate limited. Back off for a while.");
        }
        if (response.statusCode() == HTTP_INTERNAL_ERROR) {
            throw new ServerErrorException("Something went wrong on our side. ");
        }
        throw new NewsFeedClientException("Unexpected code received from service. ");
    }

}
