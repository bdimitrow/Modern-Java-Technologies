package server;

import exceptions.BadRequestException;
import exceptions.FoodNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.dto.FoodNutrient;
import server.dto.FoodReport;
import server.dto.Nutrient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodAnalyzerServerTest {
    private FoodAnalyzerServer foodAnalyzerServer;

    @Mock
    private FoodHttpClient foodHttpClientMock;

    @BeforeEach
    void setUp() throws IOException {
        foodAnalyzerServer = new FoodAnalyzerServer();
//        foodHttpClientMock = mock(FoodHttpClient.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        foodAnalyzerServer.close();
    }

    @Test
    void handleCommandNull() throws Exception {
        assertNull(foodAnalyzerServer.handleCommand(null));
    }

    @Test
    void handleCommandInvalid() throws Exception {
        assertEquals("Invalid command. ", foodAnalyzerServer.handleCommand("invalidCommand"));
    }

    @Test
    void handleCommandGetFoodReportBadRequest() throws Exception {
        assertThrows(BadRequestException.class, () -> foodAnalyzerServer.handleCommand("get-food-report badRequest"));
    }

    @Test
    void handleCommandGetFoodReportNotFound() throws Exception {
        assertThrows(FoodNotFoundException.class, () -> foodAnalyzerServer.handleCommand("get-food-report 111"));
    }

    @Test
    void handleCommandGetFoodBarcode() throws Exception {
        assertEquals("No GTIN/UPC was provided. ", foodAnalyzerServer.handleCommand("get-food-by-barcode --coding=as"));
    }

    @Test
    void handleCommandUnknown() throws Exception {
        assertEquals("Unknown command!", foodAnalyzerServer.handleCommand("unknown command"));
    }

    @Disabled
    @Test
    void handleCommandFoodReportHappyPath() throws Exception {
        FoodReport expected = new FoodReport("test", "test", 123, List.of(new FoodNutrient(new Nutrient("nut1", "nut"), 12)));
        when(foodHttpClientMock.getFoodReport("2041155")).thenReturn(expected);

        assertEquals(expected.toString(), foodAnalyzerServer.handleCommand("get-food-report 2041155"));
    }
}