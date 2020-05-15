package com.test.rsshottopicanalysis.api;

/**
 * Feed result entry.
 */
public class FeedEntry {

    private final String title;
    private final String link;

    public FeedEntry(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
