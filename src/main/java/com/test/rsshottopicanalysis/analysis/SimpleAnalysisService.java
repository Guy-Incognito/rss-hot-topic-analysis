package com.test.rsshottopicanalysis.analysis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.CosineDistance;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Simple News Analysis service.
 * Calculates relevance of single entry by comparing the cosineDistance.
 */
@Service
public class SimpleAnalysisService {

    private static final CosineDistance COSINE_DISTANCE = new CosineDistance();

    /**
     * Analyses list of news and calculates top 3 news entries.
     *
     * @param candidates list of news candidates for analysis
     * @return List of top 3 hot topics.
     */
    public List<AnalysedFeed> findHotTopics(List<AnalysisCandidate> candidates) {

        return candidates
                .stream()
                .map(candidate -> new RelevanceResult(candidate, calculateRelevance(computeSimilarities(candidate, candidates))))
                .sorted(Comparator.comparing(RelevanceResult::getRelevance).reversed())
                .limit(3)
                .map(relevanceResult -> new AnalysedFeed(relevanceResult.candidate, relevanceResult.relevance))
                .collect(Collectors.toList());
    }


    private long calculateRelevance(List<SimilarityResult> similarities) {
        return similarities.stream()
                .filter(similarityResult -> similarityResult.getCosineDistance() < 0.9)
                .count();
    }

    private List<SimilarityResult> computeSimilarities(AnalysisCandidate candidate, List<AnalysisCandidate> candidates) {

        return candidates
                .stream()
                .filter(c -> !c.getId().equals(candidate.getId()))
                .map(c -> new SimilarityResult(c,
                        candidate,
                        COSINE_DISTANCE.apply(prepare(candidate.getTitle()), prepare(c.getTitle())))
                )
                .sorted(Comparator.comparing(SimilarityResult::getCosineDistance))
                .filter(similarityResult -> similarityResult.getCosineDistance() < 1)
                .collect(Collectors.toList());
    }

    private static String prepare(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        String prune = PruningUtils.removeChannelSuffix(string);
        return PruningUtils.removeStopWords(prune).toLowerCase();
    }

    private static class RelevanceResult {
        private final AnalysisCandidate candidate;
        private final Long relevance;

        private RelevanceResult(AnalysisCandidate candidate, Long relevance) {
            this.candidate = candidate;
            this.relevance = relevance;
        }

        public AnalysisCandidate getCandidate() {
            return candidate;
        }

        public Long getRelevance() {
            return relevance;
        }
    }


    private static class SimilarityResult {

        private final AnalysisCandidate left;
        private final AnalysisCandidate right;
        private final Double cosineDistance;

        private SimilarityResult(AnalysisCandidate left, AnalysisCandidate right, Double cosineDistance) {
            this.left = left;
            this.right = right;
            this.cosineDistance = cosineDistance;
        }

        public AnalysisCandidate getLeft() {
            return left;
        }

        public AnalysisCandidate getRight() {
            return right;
        }

        public Double getCosineDistance() {
            return cosineDistance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimilarityResult similarityResult = (SimilarityResult) o;
            return Objects.equals(left, similarityResult.left) &&
                    Objects.equals(right, similarityResult.right) &&
                    Objects.equals(cosineDistance, similarityResult.cosineDistance);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right, cosineDistance);
        }
    }
}
