package com.test.rsshottopicanalysis.analysis;

import com.test.rsshottopicanalysis.analysis.topic.AnalysedTopic;

import java.util.List;

/**
 * Combined analysis result.
 */
public class AnalysisResult {

    private final List<AnalysedTopic> hotTopics;

    private final List<AnalysedFeed> hotFeeds;

    public AnalysisResult(List<AnalysedTopic> hotTopics, List<AnalysedFeed> hotFeeds) {
        this.hotTopics = hotTopics;
        this.hotFeeds = hotFeeds;
    }

    public List<AnalysedTopic> getHotTopics() {
        return hotTopics;
    }

    public List<AnalysedFeed> getHotFeeds() {
        return hotFeeds;
    }
}
