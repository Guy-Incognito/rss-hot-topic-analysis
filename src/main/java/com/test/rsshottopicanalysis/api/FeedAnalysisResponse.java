package com.test.rsshottopicanalysis.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FeedAnalysisResponse {

    private final Long id;

    @JsonCreator
    public FeedAnalysisResponse(@JsonProperty("id") Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
