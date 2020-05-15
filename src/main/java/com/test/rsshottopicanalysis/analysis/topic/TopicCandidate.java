package com.test.rsshottopicanalysis.analysis.topic;

/**
 * Topic with number of occurrences.
 */
class TopicCandidate {
    private final String name;
    private final Long frequency;

    TopicCandidate(String name, Long frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public Long getFrequency() {
        return frequency;
    }
}
