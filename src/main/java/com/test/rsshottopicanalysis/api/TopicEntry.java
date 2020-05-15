package com.test.rsshottopicanalysis.api;


import java.util.List;

/**
 * Result topic entry.
 */
public class TopicEntry {

    private final String name;

    private final Long frequency;

    private final List<FeedEntry> feeds;

    public TopicEntry(String name, Long frequency, List<FeedEntry> feeds) {
        this.name = name;
        this.frequency = frequency;
        this.feeds = feeds;
    }

    public String getName() {
        return name;
    }

    public Long getFrequency() {
        return frequency;
    }

    public List<FeedEntry> getFeeds() {
        return feeds;
    }
}
