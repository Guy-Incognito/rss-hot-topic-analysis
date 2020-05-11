package com.test.rsshottopicanalysis.api;


import com.test.rsshottopicanalysis.report.AnalysisReport;
import com.test.rsshottopicanalysis.report.AnalysisReportRepository;
import com.test.rsshottopicanalysis.report.Feed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AnalysisReportRepository analysisReportRepository;

    @BeforeEach
    public void cleanUp() {
        analysisReportRepository.deleteAll();
    }

    @Test
    void analyseFeeds_singleUrl_IdReturned() throws MalformedURLException, URISyntaxException {
        URL url1 = new URL("https://news.google.com/rss?cf=all&hl=en-US&pz=1&gl=US&ceid=US:en");
        URI postUri = new URI("http://localhost:" + port + "/analyse/new");

        ResponseEntity<FeedAnalysisResponse> response = restTemplate.postForEntity(postUri, new FeedAnalysisRequest(List.of(url1)), FeedAnalysisResponse.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
    }


    @Test
    void fetchAnalysis_singleFeed_idsMatch() throws URISyntaxException {

        Feed feed = new Feed();
        feed.setRelevance(15L);
        feed.setUri("uri");
        feed.setTitle("title");
        feed.setLink("link");

        AnalysisReport analysisReport = new AnalysisReport();
        analysisReport.setCreatedAt(LocalDateTime.now());
        analysisReport.setFeeds(List.of(feed));
        analysisReportRepository.save(analysisReport);

        Long id = analysisReport.getId();
        URI getUri = new URI("http://localhost:" + port + "/frequency/" + id);

        ResponseEntity<AnalysisResultResponse> response = restTemplate.getForEntity(getUri, AnalysisResultResponse.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        AnalysisResultResponse body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals(id, body.getId());
        Assertions.assertEquals(feed.getUri(), body.getEntries().get(0).getUri());

    }
}
