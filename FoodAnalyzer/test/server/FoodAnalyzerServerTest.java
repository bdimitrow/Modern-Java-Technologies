package server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FoodAnalyzerServerTest {
    @Mock
    private FoodHttpClient foodHttpClientMock;

    private static FoodAnalyzerServer foodAnalyzerServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        foodAnalyzerServer = new FoodAnalyzerServer(7777);

    }

    @Test
    void getLogger() {
    }

    @Test
    void main() {
    }

    @Test
    void start() {
    }

    @Test
    void handleKeyIsReadable() {
    }

    @Test
    void handleKeyIsAcceptable() {
    }

    @Test
    void stopServer() {
    }

    @Test
    void close() {
    }
}