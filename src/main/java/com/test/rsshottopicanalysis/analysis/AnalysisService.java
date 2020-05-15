package com.test.rsshottopicanalysis.analysis;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.test.rsshottopicanalysis.analysis.simple.SimpleAnalysisService;
import com.test.rsshottopicanalysis.analysis.topic.AnalysedTopic;
import com.test.rsshottopicanalysis.analysis.topic.WordCountAnalysisService;
import com.test.rsshottopicanalysis.report.AnalysisReport;
import com.test.rsshottopicanalysis.report.AnalysisReportRepository;
import com.test.rsshottopicanalysis.report.Feed;
import com.test.rsshottopicanalysis.report.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for analysing rss feeds and calculating hot topics.
 */
@Service
public class AnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisService.class);
    private final SimpleAnalysisService simpleAnalysisService;
    private final AnalysisReportRepository analysisReportRepository;
    private final WordCountAnalysisService wordCountAnalysisService;

    public AnalysisService(SimpleAnalysisService simpleAnalysisService,
                           AnalysisReportRepository analysisReportRepository,
                           WordCountAnalysisService wordCountAnalysisService) {
        this.simpleAnalysisService = simpleAnalysisService;
        this.analysisReportRepository = analysisReportRepository;
        this.wordCountAnalysisService = wordCountAnalysisService;
    }

    /**
     * Fetches analysis report.
     *
     * @param id id of calculation
     * @return report, if present.
     */
    public Optional<AnalysisReport> fetchReport(Long id) {
        return analysisReportRepository.findById(id);
    }

    /**
     * Analyses list of rss feeds.
     *
     * @param rssUrls list of rss feeds to analyse.
     * @return id of calculation (result)
     */
    public Long analyseFeedsFromUrls(List<URL> rssUrls) {

        AnalysisResult analysisResult = analyseFromUrls(rssUrls);

        AnalysisReport analysisReport = new AnalysisReport();
        List<Topic> topics = analysisResult.getHotTopics().stream()
                .map(analysedTopic -> new Topic(
                                analysedTopic.getTopic(),
                                analysedTopic.getFrequency(),
                                analysedTopic.getEntries().stream()
                                        .map(analysisCandidate -> new Feed(null,
                                                        analysisCandidate.getId(),
                                                        analysisCandidate.getTitle(),
                                                        analysisCandidate.getLink()
                                                )
                                        ).collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());

        analysisReport.setTopics(topics);
        analysisReport.setCreatedAt(LocalDateTime.now());
        analysisReportRepository.save(analysisReport);
        return analysisReport.getId();
    }

    /**
     * Analyses list of rss feeds.
     *
     * @param rssUrls list of rss feeds to analyse.
     * @return list of analysis results.
     */
    public AnalysisResult analyseFromUrls(List<URL> rssUrls) {

        List<SyndFeed> feeds = rssUrls.stream()
                .map(AnalysisService::parseFeedFromUrl)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return analyse(feeds);
    }

    /**
     * Analyses list of rss feeds.
     *
     * @param file rss feed as file.
     * @return analysis result.
     */
    public AnalysisResult analyseFeedsFromFile(File file) {
        return parseFeedFromFile(file)
                .map(syndFeed -> analyse(List.of(syndFeed)))
                .orElse(new AnalysisResult(Collections.emptyList(), Collections.emptyList()));
    }

    private AnalysisResult analyse(List<SyndFeed> feeds) {
        List<AnalysisCandidate> candidates = feeds.stream()
                .map(syndFeed -> syndFeed.getEntries().stream()
                        .map(syndEntry -> new AnalysisCandidate(
                                syndEntry.getUri(),
                                syndEntry.getLink(),
                                syndEntry.getTitle(),
                                Optional.ofNullable(syndEntry.getDescription())
                                        .map(SyndContent::getValue)
                                        .orElse(null))
                        )
                )
                .flatMap(Stream::distinct)
                .collect(Collectors.toList());

        List<AnalysedTopic> hotTopics = wordCountAnalysisService.findHotTopics(candidates);

        List<AnalysedFeed> hotFeeds = simpleAnalysisService.findHotTopics(candidates);

        return new AnalysisResult(hotTopics, hotFeeds);
    }

    private static Optional<SyndFeed> parseFeedFromFile(File file) {
        try {
            SyndFeedInput input = new SyndFeedInput();
            return Optional.of(input.build(new XmlReader(file)));
        } catch (IOException | FeedException e) {
            LOGGER.error("Could not parse feed: ", e);
            return Optional.empty();
        }
    }

    private static Optional<SyndFeed> parseFeedFromUrl(URL url) {
        try {
            SyndFeedInput input = new SyndFeedInput();
            return Optional.of(input.build(new XmlReader(url)));
        } catch (IOException | FeedException e) {
            LOGGER.error("Could not parse feed: ", e);
            return Optional.empty();
        }
    }
}
