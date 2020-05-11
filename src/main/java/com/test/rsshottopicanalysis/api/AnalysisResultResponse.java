package com.test.rsshottopicanalysis.api;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response containing analysis result.
 */
public class AnalysisResultResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final List<AnalysisResultResponseEntry> entries;


    public AnalysisResultResponse(Long id, LocalDateTime createdAt, List<AnalysisResultResponseEntry> entries) {
        this.id = id;
        this.createdAt = createdAt;
        this.entries = entries;
    }

    public Long getId() {
        return id;
    }

    public List<AnalysisResultResponseEntry> getEntries() {
        return entries;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
