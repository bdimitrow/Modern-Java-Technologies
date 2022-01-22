package bg.sofia.uni.fmi.mjt.newsfeed;

import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mock;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;


import static org.junit.jupiter.api.Assertions.*;

class NewsFeedClientTest {
    @Mock
    private static HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private static NewsFeedClient newsFeedClient;

    @BeforeAll
    public static void setUp(){
        newsFeedClient = new NewsFeedClient(httpClientMock);
    }


}