package com.test.rsshottopicanalysis.analysis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PruningUtilsTest {

    @Test
    void removeChannelSuffix_channelSuffixWithMinus_channelRemoved() {

        String input = "tralala - Business Insider - Business Insider";
        String expectedResult = "tralala";

        String result = PruningUtils.removeChannelSuffix(input);

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void removeChannelSuffix_channelSuffixWithPipe_channelRemoved() {

        String input = "tralala | Business Insider - Business Insider";
        String expectedResult = "tralala";

        String result = PruningUtils.removeChannelSuffix(input);

        Assertions.assertEquals(expectedResult, result);
    }


    @Test
    void removeStopWords_multipleStopWordsProvided_allStopWordsRemoved() {
        String input = "I am at the zoo";
        String expectedResult = "zoo";
        String result = PruningUtils.removeStopWords(input);

        Assertions.assertEquals(expectedResult, result);
    }
}
