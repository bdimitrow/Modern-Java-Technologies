package bg.sofia.uni.fmi.mjt.newsfeed;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ServerErrorException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.SourcesTooManyRequestsException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsFeedClientTest {
    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private static NewsFeedClient client;

    @BeforeEach
    void beforeEach() {
        client = new NewsFeedClient(httpClientMock);
    }

    @Test
    void testNoKeywords() {
        assertThrows(BadRequestException.class,
                () -> client.getNewsFeed(null, Optional.empty(), Optional.empty(), Optional.empty()));
    }

    @Test
    void testBadRequest() throws IOException, InterruptedException {
        when(httpClientMock.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        assertThrows(BadRequestException.class,
                () -> client.getNewsFeed(new String[]{"test"}, Optional.empty(), Optional.empty(), Optional.of(20)));
    }

    @Test
    void testUnauthorized() throws IOException, InterruptedException {
        when(httpClientMock.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);

        assertThrows(UnauthorizedException.class,
                () -> client.getNewsFeed(new String[]{"test"}, Optional.empty(), Optional.empty(), Optional.of(20)));
    }

    @Test
    void testSourceTooManyRequest() throws IOException, InterruptedException {
        when(httpClientMock.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(419);

        assertThrows(SourcesTooManyRequestsException.class,
                () -> client.getNewsFeed(new String[]{"test"}, Optional.empty(), Optional.empty(), Optional.of(20)));
    }

    @Test
    void testServerError() throws IOException, InterruptedException {
        when(httpClientMock.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);

        assertThrows(ServerErrorException.class,
                () -> client.getNewsFeed(new String[]{"test"}, Optional.empty(), Optional.empty(), Optional.of(20)));
    }
}