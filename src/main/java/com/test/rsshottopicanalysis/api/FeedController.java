package com.test.rsshottopicanalysis.api;

import com.test.rsshottopicanalysis.analysis.AnalysisService;
import com.test.rsshottopicanalysis.report.AnalysisReport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FeedController {

    private final AnalysisService analysisService;

    public FeedController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }


    /**
     * @param uriComponentsBuilder will be injected
     * @param feedAnalysisRequest  request containing multiple rss feeds
     * @return id of caluclation.
     */
    @PostMapping("/analyse/new")
    public ResponseEntity<FeedAnalysisResponse> analyseFeeds(UriComponentsBuilder uriComponentsBuilder,
                                                             @RequestBody FeedAnalysisRequest feedAnalysisRequest) {
        Long id = analysisService.analyseFeedsFromUrls(feedAnalysisRequest.getRssUrls());
        UriComponents uriComponents =
                uriComponentsBuilder.path("/frequency/{id}").buildAndExpand(id);
        return ResponseEntity.created(uriComponents.toUri()).body(new FeedAnalysisResponse(id));
    }


    /**
     * @param id id of calculation for which report should be fetched
     * @return the report.
     */
    @GetMapping("/frequency/{id}")
    public ResponseEntity<AnalysisResultResponse> getAnalysisResult(@PathVariable Long id) {
        return analysisService.fetchReport(id)
                .map(this::map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private AnalysisResultResponse map(AnalysisReport analysisReport) {
        List<TopicEntry> topics = analysisReport.getTopics().stream()
                .map(topic -> new TopicEntry(
                        topic.getName(),
                        topic.getFrequency(),
                        topic.getFeeds().stream()
                                .map(feed -> new FeedEntry(feed.getTitle(), feed.getLink()))
                                .collect(Collectors.toList()))
                ).collect(Collectors.toList());

        return new AnalysisResultResponse(analysisReport.getId(), analysisReport.getCreatedAt(), topics);

    }


}
