package com.test.rsshottopicanalysis.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entity for single analysed feed.
 */
@Entity
public class Feed {

    @Id
    @GeneratedValue
    private Long id;
    private Long relevance;
    private String uri;
    private String title;
    @Column(length = 511)
    private String link;

    public Feed() {
    }

    public Feed(Long relevance, String uri, String title, String link) {
        this.relevance = relevance;
        this.uri = uri;
        this.title = title;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getRelevance() {
        return relevance;
    }

    public void setRelevance(Long relevance) {
        this.relevance = relevance;
    }

}
