package server;

import com.google.gson.Gson;
import exceptions.BadRequestException;
import exceptions.FoodNotFoundException;
import server.dto.FoodList;
import server.dto.FoodReport;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class FoodHttpClient {
    public static final String URL_DELIMITER = "%20";
    private static final String API_KEY = "We5UALb9buICMpssP0NRPDneFLC9pAhctVG07lPv";
    private static final String FOOD_URL = "https://api.nal.usda.gov/fdc/v1/foods/search?query=%s&requireAllWords=true&api_key=%s";
    private static final String FOOD_REPORT_URL_TEMPLATE = "https://api.nal.usda.gov/fdc/v1/food/%s?api_key=%s";

    private final HttpClient httpClient;
    private final Gson gson;

    public FoodHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.gson = new Gson();
    }

    public FoodReport getFoodReport(String fcdId) throws FoodNotFoundException, BadRequestException {
        String uriFormatted = String.format(FOOD_REPORT_URL_TEMPLATE, fcdId, API_KEY);
        URI uri = URI.create(uriFormatted);
        String response = getResponse(uri);
        if (response == null) {
            return null;
        }
        return gson.fromJson(response, FoodReport.class);
    }

    public FoodList getFoodsBySearch(String words) throws FoodNotFoundException, BadRequestException {
        String[] searchWords = words.trim().split("\\s");
        String searchTerm;
        if (searchWords.length == 1) {
            searchTerm = words.trim();
        } else {
            searchTerm = String.join("+", searchWords);
        }
        String uriFormatted = String.format(FOOD_URL, searchTerm, API_KEY);
        URI uri = URI.create(uriFormatted);
        String response = getResponse(uri);
        if (response == null) {
            return null;
        }

        return gson.fromJson(response, FoodList.class);
    }


    public String getResponse(URI uri) throws FoodNotFoundException, BadRequestException {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).build();

        String response = null;
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == HTTP_NOT_FOUND) {
                throw new FoodNotFoundException("Food could not be found!");
            }
            if (httpResponse.statusCode() == HTTP_BAD_REQUEST) {
                throw new BadRequestException("Bad input parameters!");
            }
            response = httpResponse.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while sending request: ", e);
        }

        return response;
    }

}
