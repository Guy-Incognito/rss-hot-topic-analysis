package com.test.rsshottopicanalysis.analysis.topic;

import com.test.rsshottopicanalysis.analysis.AnalysisCandidate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class WordCountAnalysisServiceTest {


    @Autowired
    private WordCountAnalysisService wordCountAnalysisService;

    @Test
    void findHotTopics() {
        AnalysisCandidate coronavirus_trump_cats_dogs =
                new AnalysisCandidate("1", "", "Coronavirus Trump Cats Dogs", "");
        AnalysisCandidate cats = new AnalysisCandidate("2", "", "Cats", "");
        AnalysisCandidate dogs = new AnalysisCandidate("3", "", "Dogs", "");
        AnalysisCandidate turtles = new AnalysisCandidate("4", "", "Turtles love trump", "");
        AnalysisCandidate trump = new AnalysisCandidate("5", "", "trump eats banana", "");

        List<AnalysisCandidate> candidateList = List.of(
                coronavirus_trump_cats_dogs,
                cats,
                dogs,
                turtles,
                trump
        );

        List<AnalysedTopic> hotTopics = wordCountAnalysisService.findHotTopics(candidateList);

        assertFalse(hotTopics.isEmpty());
        assertEquals(3, hotTopics.size());

        // first topic must be trump since it occurs in 3 candidates.
        assertEquals("trump", hotTopics.get(0).getTopic());
        assertEquals(3, hotTopics.get(0).getFrequency());
    }
}