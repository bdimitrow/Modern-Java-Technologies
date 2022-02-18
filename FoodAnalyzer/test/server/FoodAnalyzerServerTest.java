package server;

import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mock;

import java.io.IOException;

class FoodAnalyzerServerTest {
    @Mock
    private FoodHttpClient foodHttpClientMock;

    private static FoodAnalyzerServer foodAnalyzerServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        foodAnalyzerServer = new FoodAnalyzerServer(7777);

    }

}