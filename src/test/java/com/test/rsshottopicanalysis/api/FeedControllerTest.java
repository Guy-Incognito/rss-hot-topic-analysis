package com.test.rsshottopicanalysis.api;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void analyseFeeds() throws MalformedURLException, URISyntaxException {
        URL url1 = new URL("https://news.google.com/rss?cf=all&hl=en-US&pz=1&gl=US&ceid=US:en");
        URI postUri = new URI("http://localhost:" + port + "/analyse/new");

        ResponseEntity<FeedAnalysisResponse> response = restTemplate.postForEntity(postUri, new FeedAnalysisRequest(List.of(url1)), FeedAnalysisResponse.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
}
