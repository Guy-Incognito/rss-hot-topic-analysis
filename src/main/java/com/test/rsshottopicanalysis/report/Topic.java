package com.test.rsshottopicanalysis.report;

import javax.persistence.*;
import java.util.List;

@Entity
public class Topic {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Long frequency;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Feed> feeds;


    public Topic(String name, Long frequency, List<Feed> feeds) {
        this.name = name;
        this.frequency = frequency;
        this.feeds = feeds;
    }

    public Topic() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }
}
