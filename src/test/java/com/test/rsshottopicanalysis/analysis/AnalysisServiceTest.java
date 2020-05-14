package com.test.rsshottopicanalysis.analysis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;

@SpringBootTest
class AnalysisServiceTest {

    @Autowired
    private AnalysisService analysisService;

    @Value("classpath:testfeed")
    private Resource testfeed;

    @Value("classpath:feed-no-description.xml")
    private Resource feedWithEmptyDescription;

    @Test
    void analyseFeedsFromFile_3candidatesDetected() throws IOException {

        var analysisCandidates = analysisService.analyseFeedsFromFile(testfeed.getFile());

        assert analysisCandidates.size() == 3;
    }

    @Test
    void analyseFeedsFromFile_noDescription() throws IOException {

        var analysisCandidates = analysisService.analyseFeedsFromFile(feedWithEmptyDescription.getFile());

        assert analysisCandidates.size() == 1;
    }
}
