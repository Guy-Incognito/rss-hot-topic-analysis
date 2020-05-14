package com.test.rsshottopicanalysis.analysis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class SimpleAnalysisServiceTest {

    @Autowired
    private SimpleAnalysisService simpleAnalysisService;

    @Test
    public void testAnalysis_candidatesRankedCorrectly() {
        AnalysisCandidate coronavirus_trump_cats_dogs =
                new AnalysisCandidate("1", "", "Coronavirus Trump Cats Dogs", "");
        AnalysisCandidate cats = new AnalysisCandidate("2", "", "Cats", "");
        AnalysisCandidate dogs = new AnalysisCandidate("3", "", "Dogs", "");
        AnalysisCandidate turtles = new AnalysisCandidate("4", "", "Turtles", "");
        AnalysisCandidate trump = new AnalysisCandidate("5", "", "trump", "");

        List<AnalysisCandidate> candidateList = List.of(
                coronavirus_trump_cats_dogs,
                cats,
                dogs,
                turtles,
                trump
        );

        List<AnalysedFeed> hotTopics = simpleAnalysisService.findHotTopics(candidateList);

        assertFalse(hotTopics.isEmpty());
        assertEquals(3, hotTopics.size());

        // First result
        assertEquals(coronavirus_trump_cats_dogs.getId(), hotTopics.get(0).getId());
        // relevance must be 3, since 3 similar titles are present (i.e. cats,dogs and trump)
        assertEquals(3L, hotTopics.get(0).getRelevance());

        // Second result must have relevance 1
        assertEquals(1L, hotTopics.get(1).getRelevance());

        // Third result must have relevance 1
        assertEquals(1L, hotTopics.get(2).getRelevance());

    }

}