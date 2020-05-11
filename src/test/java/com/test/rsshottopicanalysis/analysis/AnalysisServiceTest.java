package com.test.rsshottopicanalysis.analysis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@SpringBootTest
class AnalysisServiceTest {

    @Autowired
    private AnalysisService analysisService;

    @Value("classpath:testfeed")
    private Resource testfeed;

    @Test
    void analyseFeeds() throws MalformedURLException {

//        URL url1 = new URL("https://news.google.com/rss?cf=all&hl=en-US&pz=1&gl=US&ceid=US:en");
//
//        analysisService.analyseFeeds(List.of(url1));
    }

    @Test
    void analyseFeedsFromFile() throws IOException {

        var analysisCandidates = analysisService.analyseFeedsFromFile(testfeed.getFile());

        assert analysisCandidates.size() == 3;
    }
}
