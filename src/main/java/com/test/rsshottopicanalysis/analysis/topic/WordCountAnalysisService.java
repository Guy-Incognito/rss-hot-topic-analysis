package com.test.rsshottopicanalysis.analysis.topic;

import com.test.rsshottopicanalysis.analysis.AnalysisCandidate;
import com.test.rsshottopicanalysis.analysis.PruningUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Service identifying hot topics via word counting.
 */
@Service
public class WordCountAnalysisService {

    /**
     * Analyses news feeds and calculates hot topics.
     *
     * @param candidates list of analysis candidates
     * @return list of 3 hottest topics.
     */
    public List<AnalysedTopic> findHotTopics(List<AnalysisCandidate> candidates) {

        List<TopicCandidate> topicCandidates = getTopicCandidates(candidates);

        return topicCandidates.stream()
                .map(topicCandidate -> new AnalysedTopic(
                                topicCandidate.getName(),
                                topicCandidate.getFrequency(),
                                candidates.stream()
                                        .filter(analysisCandidate -> PruningUtils.prepare(analysisCandidate.getTitle())
                                                .contains(topicCandidate.getName()))
                                        .collect(Collectors.toList())
                        )
                ).collect(Collectors.toList());
    }

    private List<TopicCandidate> getTopicCandidates(List<AnalysisCandidate> candidates) {
        // Calculate map of possible topics, by generating list of filtered words.
        Map<String, Long> topicCandidates = candidates.stream()
                .map(AnalysisCandidate::getTitle)
                .map(PruningUtils::prepare)
                .map(s -> Arrays.stream(s.split(" "))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // return top 3 topics
        return topicCandidates.entrySet().stream()
                .map(stringLongEntry -> new TopicCandidate(stringLongEntry.getKey(), stringLongEntry.getValue()))
                .sorted(Comparator.comparing(TopicCandidate::getFrequency).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }


}
