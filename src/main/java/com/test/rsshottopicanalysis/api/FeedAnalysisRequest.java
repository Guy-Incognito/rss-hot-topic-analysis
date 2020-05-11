package com.test.rsshottopicanalysis.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.util.List;

public class FeedAnalysisRequest {
    private final List<URL> rssUrls;

    @JsonCreator
    public FeedAnalysisRequest(@JsonProperty("rssUrls") List<URL> rssUrls) {
        this.rssUrls = rssUrls;
    }

    public List<URL> getRssUrls() {
        return rssUrls;
    }
}
