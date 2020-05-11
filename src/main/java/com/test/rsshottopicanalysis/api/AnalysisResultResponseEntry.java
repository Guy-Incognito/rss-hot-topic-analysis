package com.test.rsshottopicanalysis.api;


/**
 * Analysis response for a single news entry.
 */
public class AnalysisResultResponseEntry {

    private final String uri;
    private final String title;
    private final String link;
    private final Long relevance;

    public AnalysisResultResponseEntry(String uri, String title, String link, Long relevance) {
        this.uri = uri;
        this.title = title;
        this.link = link;
        this.relevance = relevance;
    }

    public String getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public Long getRelevance() {
        return relevance;
    }
}
