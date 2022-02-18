package server;

import exceptions.BadRequestException;
import exceptions.FoodNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import server.dto.Food;
import server.dto.FoodList;
import server.dto.FoodNutrient;
import server.dto.FoodReport;
import server.dto.Nutrient;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodHttpClientTest {

//    @Mock
//    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private static FoodHttpClient foodHttpClient;

    @BeforeEach
    void beforeEach() throws IOException, InterruptedException {
        HttpClient httpClientMock = mock(HttpClient.class);
        foodHttpClient = new FoodHttpClient(httpClientMock);
        when(httpClientMock.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
    }

    @Test
    void getFoodReportNotFound() throws FoodNotFoundException, BadRequestException {
        when(httpResponseMock.statusCode()).thenReturn(HTTP_NOT_FOUND);

        assertThrows(FoodNotFoundException.class,
                () -> foodHttpClient.getFoodReport("123"),
                "HttpResponse was 404 food not found.");
    }

    @Test
    void getFoodReportBadRequest() {
        when(httpResponseMock.statusCode()).thenReturn(HTTP_BAD_REQUEST);

        assertThrows(BadRequestException.class,
                () -> foodHttpClient.getFoodReport("123"),
                "HttpResponse was 404 food not found.");
    }

    @Test
    void getFoodReportNull() throws FoodNotFoundException, BadRequestException {
        when(httpResponseMock.body()).thenReturn(null);
        assertNull(foodHttpClient.getFoodReport("123"));
    }

    private String responseBody;


    @Disabled
    @Test
    void getFoodReport() throws FoodNotFoundException, BadRequestException {
        String reponseReport = """
                {
                    "description": "HAM",
                    "foodNutrients": [
                        {
                            "type": "FoodNutrient",
                            "nutrient": {
                                "id": 1008,
                                "number": "208",
                                "name": "Energy",
                                "rank": 300,
                                "unitName": "kcal"
                            },
                            "amount": 90.00000000
                        }
                    ],
                    "dataType": "Branded",
                    "gtinUpc": "677684412056",
                    "ingredients": "HAM SALT, SUGAR",
                """;
        when(httpResponseMock.body()).thenReturn(reponseReport);
        FoodReport expected = new FoodReport("HAM","HAM SALT, SUGAR",2167267, List.of(new FoodNutrient(new Nutrient("Energy","kcal"),90.0)));

        System.out.println(expected.toString());
        System.out.println(foodHttpClient.getFoodReport("123").toString());

        assertEquals(expected.toString(), foodHttpClient.getFoodReport("123").toString());
    }

    @Disabled
    @Test
    void getFoodsBySearch() throws FoodNotFoundException, BadRequestException {
        when(httpResponseMock.body()).thenReturn("""
                {
                "totalHits": 1,
                "foods": [
                {
                "fdcId": 2167267,
                "description": "HAM",
                "dataType": "Branded",
                "gtinUpc": "677684412056",
                "ingredients": "HAM",
                 "foodNutrients": [{
                "nutrientId": 1008,
                "nutrientName": "Energy",
                "nutrientNumber": "208",
                "unitName": "KCAL",
                "derivationCode": "LCCS",
                "derivationDescription": "Calculated from value per serving size measure",
                "derivationId": 70,
                "value": 90,
                "foodNutrientSourceId": 9,
                "foodNutrientSourceCode": "12",
                "foodNutrientSourceDescription": "Manufacturer's analytical; partial documentation",
                "rank": 300,
                "indentLevel": 1,
                "foodNutrientId": 25938181
                }],
                }]
                }""");
        FoodList expected = new FoodList(Set.of(new Food(1,"test","t","12")),1);

        assertEquals(expected.toString(), foodHttpClient.getFoodsBySearch("123").toString());
    }


}