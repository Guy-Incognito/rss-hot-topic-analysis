package com.test.rsshottopicanalysis.analysis.topic;

import com.test.rsshottopicanalysis.analysis.AnalysisCandidate;

import java.util.List;

/**
 * Frequency analysed topic.
 */
public class AnalysedTopic {

    private final String topic;
    private final Long frequency;
    private final List<AnalysisCandidate> entries;

    public AnalysedTopic(String topic, Long frequency, List<AnalysisCandidate> entries) {
        this.topic = topic;
        this.frequency = frequency;
        this.entries = entries;
    }

    public String getTopic() {
        return topic;
    }

    public Long getFrequency() {
        return frequency;
    }

    public List<AnalysisCandidate> getEntries() {
        return entries;
    }
}
