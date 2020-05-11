package com.test.rsshottopicanalysis.analysis;

/**
 * Result of an analysed news feed.
 */
public class AnalysedFeed extends AnalysisCandidate {

    /**
     * Relevance as long. The higher this value the more relevant a single news entry is.
     */
    private final Long relevance;

    public AnalysedFeed(AnalysisCandidate analysisCandidate, Long relevance) {
        super(analysisCandidate.getId(), analysisCandidate.getLink(), analysisCandidate.getTitle(), analysisCandidate.getText());
        this.relevance = relevance;
    }

    public Long getRelevance() {
        return relevance;
    }


}
