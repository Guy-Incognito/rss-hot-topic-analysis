package com.test.rsshottopicanalysis.analysis;

import java.util.Objects;

/**
 * News entry candidate for analysis.
 */
public class AnalysisCandidate {

    private final String id;
    private final String link;
    private final String title;
    private final String text;


    public AnalysisCandidate(String id, String link, String title, String text) {
        this.id = id;
        this.link = link;
        this.title = title;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisCandidate that = (AnalysisCandidate) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
