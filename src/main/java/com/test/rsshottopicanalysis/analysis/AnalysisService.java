package com.test.rsshottopicanalysis.analysis;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.test.rsshottopicanalysis.report.AnalysisReport;
import com.test.rsshottopicanalysis.report.AnalysisReportRepository;
import com.test.rsshottopicanalysis.report.Feed;
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

    public AnalysisService(SimpleAnalysisService simpleAnalysisService,
                           AnalysisReportRepository analysisReportRepository) {
        this.simpleAnalysisService = simpleAnalysisService;
        this.analysisReportRepository = analysisReportRepository;
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

        List<AnalysedFeed> analysedFeeds = analyseFromUrls(rssUrls);

        AnalysisReport analysisReport = new AnalysisReport();
        List<Feed> feeds = analysedFeeds.stream()
                .map(analysedFeed -> new Feed(
                        analysedFeed.getRelevance(),
                        analysedFeed.getId(),
                        analysedFeed.getTitle(),
                        analysedFeed.getLink())
                )
                .collect(Collectors.toList());
        analysisReport.setFeeds(feeds);
        analysisReport.setCreatedAt(LocalDateTime.now());
        analysisReportRepository.save(analysisReport);
        return analysisReport.getId();
    }

    /**
     * Analyses list of rss feeds.
     *
     * @param rssUrls list of rss feeds to analyse.
     * @return list of analysed feeds.
     */
    public List<AnalysedFeed> analyseFromUrls(List<URL> rssUrls) {

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
     * @return list of analysed feeds.
     */
    public List<AnalysedFeed> analyseFeedsFromFile(File file) {
        return parseFeedFromFile(file)
                .map(syndFeed -> analyse(List.of(syndFeed)))
                .orElse(Collections.emptyList());
    }

    private List<AnalysedFeed> analyse(List<SyndFeed> feeds) {
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

        return simpleAnalysisService.findHotTopics(candidates);
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
