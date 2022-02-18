package bg.sofia.uni.fmi.mjt.newsfeed;

import bg.sofia.uni.fmi.mjt.newsfeed.dto.News;
import bg.sofia.uni.fmi.mjt.newsfeed.dto.NewsFeed;
import bg.sofia.uni.fmi.mjt.newsfeed.dto.NewsSource;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsFeedClientException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ServerErrorException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.SourcesTooManyRequestsException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NewsFeedClientTest {
    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private static NewsFeedClient client;

    @BeforeEach
    void beforeEach() throws IOException, InterruptedException {
        client = new NewsFeedClient(httpClientMock);
        HttpClient httpClientMock = mock(HttpClient.class)
        when(httpClientMock.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
    }

    @Test
    void testNoKeywords() throws IOException, InterruptedException {
        assertThrows(BadRequestException.class,
                () -> client.getNewsFeed(null, Optional.empty(), Optional.empty(), Optional.empty()));
    }

    @Test
    void testBadRequest() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        assertThrows(BadRequestException.class,
                () -> client.getNewsFeed(new String[]{"test"}, Optional.empty(), Optional.empty(), Optional.of(20)));
    }

    @Test
    void testUnauthorized() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);

        assertThrows(UnauthorizedException.class,
                () -> client.getNewsFeed(new String[]{"test"}, Optional.empty(), Optional.empty(), Optional.of(20)));
    }

    @Test
    void testSourceTooManyRequest() {
        when(httpResponseMock.statusCode()).thenReturn(419);

        assertThrows(SourcesTooManyRequestsException.class,
                () -> client.getNewsFeed(new String[]{"test"}, Optional.empty(), Optional.empty(), Optional.of(20)));
    }

    @Test
    void testServerError() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);

        assertThrows(ServerErrorException.class,
                () -> client.getNewsFeed(new String[]{"test"}, Optional.empty(), Optional.empty(), Optional.of(20)));
    }

    @Test
    void testUnknownCodeError() {
        when(httpResponseMock.statusCode()).thenReturn(9999);

        assertThrows(NewsFeedClientException.class,
                () -> client.getNewsFeed(new String[]{"test"}, Optional.empty(), Optional.empty(), Optional.of(20)));
    }

    @Test
    void testWithResults() throws NewsFeedClientException, SourcesTooManyRequestsException, UnauthorizedException, BadRequestException, ServerErrorException, URISyntaxException, IOException, InterruptedException {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(responsed);
        NewsFeed newsFeed = client.getNewsFeed(new String[]{"health"}, Optional.empty(), Optional.empty(), Optional.of(30));

        assertTrue(newsFeed.getNews().contains(new News(new NewsSource("test", "test name"),
                "author",
                "testTitle",
                "testDesc",
                "testUrl",
                "2022-01-23T09:34:09+00:00",
                "contentTest")));
        assertEquals(19, newsFeed.getTotalResults());
    }

    @Test
    void testWithResultsSmallPage() throws NewsFeedClientException, SourcesTooManyRequestsException, UnauthorizedException, BadRequestException, ServerErrorException, URISyntaxException, IOException, InterruptedException {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(responsed);
        NewsFeed newsFeed = client.getNewsFeed(new String[]{"health"}, Optional.empty(), Optional.empty(), Optional.of(5));

        assertTrue(newsFeed.getNews().contains(new News(new NewsSource("test", "test name"),
                "author",
                "testTitle",
                "testDesc",
                "testUrl",
                "2022-01-23T09:34:09+00:00",
                "contentTest")));
        assertEquals(19, newsFeed.getTotalResults());
    }


    String responsed = """
            {
            "status": "ok",
                "totalResults": 19,
                "articles": [
                    {
                        "source": {
                            "id": "test",
                            "name": "test name"
                        },
                        "author": "author",
                        "title": "testTitle",
                        "description": "testDesc",
                        "url": "testUrl",
                        "urlToImage": "urlImage",
                        "publishedAt": "2022-01-23T09:34:09+00:00",
                        "content": "contentTest"
                    },
                    {
                        "source": {
                            "id": "google-news",
                            "name": "Google News"
                        },
                        "author": "Tori Tori",
                        "title": "Senator Kyrsten Sinema formally censured by Arizona Democratic Party",
                        "description": "Sinema's censure came as \\"a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said.",
                        "url": "https://www.cbsnews.com/news/kyrsten-sinema-censure-arizona-democratic-party/",
                        "urlToImage": "https://cbsnews1.cbsistatic.com/hub/i/r/2018/11/13/39aa56dc-f150-4233-9607-3aabbef3b476/thumbnail/1200x630g2/29296e4bb936652b6aa2acbff367b26b/ap-18289048241536.jpg",
                        "publishedAt": "2022-01-23T01:52:00+00:00",
                        "content": "The Arizona Democratic Party has voted to formally censure Senator Kyrsten Sinema \\"as a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said Saturday. … [+2594 chars]"
                    },
                    {
                        "source": {
                            "id": "google-news",
                            "name": "Google News"
                        },
                        "author": "Tori Tori",
                        "title": "Senator Kyrsten Sinema formally censured by Arizona Democratic Party",
                        "description": "Sinema's censure came as \\"a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said.",
                        "url": "https://www.cbsnews.com/news/kyrsten-sinema-censure-arizona-democratic-party/",
                        "urlToImage": "https://cbsnews1.cbsistatic.com/hub/i/r/2018/11/13/39aa56dc-f150-4233-9607-3aabbef3b476/thumbnail/1200x630g2/29296e4bb936652b6aa2acbff367b26b/ap-18289048241536.jpg",
                        "publishedAt": "2022-01-23T01:52:00+00:00",
                        "content": "The Arizona Democratic Party has voted to formally censure Senator Kyrsten Sinema \\"as a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said Saturday. … [+2594 chars]"
                    },
                    {
                        "source": {
                            "id": "google-news",
                            "name": "Google News"
                        },
                        "author": "Tori Tori",
                        "title": "Senator Kyrsten Sinema formally censured by Arizona Democratic Party",
                        "description": "Sinema's censure came as \\"a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said.",
                        "url": "https://www.cbsnews.com/news/kyrsten-sinema-censure-arizona-democratic-party/",
                        "urlToImage": "https://cbsnews1.cbsistatic.com/hub/i/r/2018/11/13/39aa56dc-f150-4233-9607-3aabbef3b476/thumbnail/1200x630g2/29296e4bb936652b6aa2acbff367b26b/ap-18289048241536.jpg",
                        "publishedAt": "2022-01-23T01:52:00+00:00",
                        "content": "The Arizona Democratic Party has voted to formally censure Senator Kyrsten Sinema \\"as a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said Saturday. … [+2594 chars]"
                    },
                    {
                        "source": {
                            "id": "google-news",
                            "name": "Google News"
                        },
                        "author": "Tori Tori",
                        "title": "Senator Kyrsten Sinema formally censured by Arizona Democratic Party",
                        "description": "Sinema's censure came as \\"a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said.",
                        "url": "https://www.cbsnews.com/news/kyrsten-sinema-censure-arizona-democratic-party/",
                        "urlToImage": "https://cbsnews1.cbsistatic.com/hub/i/r/2018/11/13/39aa56dc-f150-4233-9607-3aabbef3b476/thumbnail/1200x630g2/29296e4bb936652b6aa2acbff367b26b/ap-18289048241536.jpg",
                        "publishedAt": "2022-01-23T01:52:00+00:00",
                        "content": "The Arizona Democratic Party has voted to formally censure Senator Kyrsten Sinema \\"as a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said Saturday. … [+2594 chars]"
                    },
                    {
                        "source": {
                            "id": "google-news",
                            "name": "Google News"
                        },
                        "author": "Tori Tori",
                        "title": "Senator Kyrsten Sinema formally censured by Arizona Democratic Party",
                        "description": "Sinema's censure came as \\"a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said.",
                        "url": "https://www.cbsnews.com/news/kyrsten-sinema-censure-arizona-democratic-party/",
                        "urlToImage": "https://cbsnews1.cbsistatic.com/hub/i/r/2018/11/13/39aa56dc-f150-4233-9607-3aabbef3b476/thumbnail/1200x630g2/29296e4bb936652b6aa2acbff367b26b/ap-18289048241536.jpg",
                        "publishedAt": "2022-01-23T01:52:00+00:00",
                        "content": "The Arizona Democratic Party has voted to formally censure Senator Kyrsten Sinema \\"as a result of her failure to do whatever it takes to ensure the health of our democracy,\\" the party said Saturday. … [+2594 chars]"
                    }
                ]
            }""";
}