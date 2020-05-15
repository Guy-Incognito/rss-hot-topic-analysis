package com.test.rsshottopicanalysis.api;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response containing analysis result.
 */
public class AnalysisResultResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final List<TopicEntry> topics;

    public AnalysisResultResponse(Long id, LocalDateTime createdAt, List<TopicEntry> topics) {
        this.id = id;
        this.createdAt = createdAt;
        this.topics = topics;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<TopicEntry> getTopics() {
        return topics;
    }
}
