package com.test.rsshottopicanalysis.analysis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for removing irrelevant information from strings.
 */
public final class PruningUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PruningUtils.class);
    private static final List<String> SEPARATORS = List.of(" - ", " | ");

    private PruningUtils() {

    }

    /**
     * Prepares string for analysing.
     *
     * @param string string to normalize.
     * @return string prepared for analysing.
     */
    public static String prepare(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        string = string.toLowerCase();
        String prune = PruningUtils.removeChannelSuffix(string);
        String withoutStopWords = PruningUtils.removeStopWords(prune);
        return removeNonAlphaNumericalChars(withoutStopWords);
    }

    /**
     * Removes common news channel suffixes.
     *
     * @param string a string to be processed.
     * @return string without common news channel suffixes.
     */
    public static String removeChannelSuffix(@NotNull String string) {
        return SEPARATORS.stream()
                .map(string::indexOf)
                .filter(integer -> integer > 1)
                .min(Comparator.naturalOrder())
                .map(integer -> string.substring(0, integer))
                .orElse(string);
    }

    /**
     * Removes common stopWords from string.
     *
     * @param string a string to be processed.
     * @return string without stopWords.
     */
    public static String removeStopWords(@NotNull String string) {
        try {
            URI uri = PruningUtils.class.getResource("/stoplists/en.txt").toURI();
            Path path = Paths.get(uri);
            List<String> strings = Files.readAllLines(path, Charset.defaultCharset());
            Pattern stopWordsRegex = compileStopWordsRegex(strings);
            Matcher matcher = stopWordsRegex.matcher(string);
            return matcher.replaceAll("");
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Error removing stopWords:", e);
            return string;
        }
    }

    private static Pattern compileStopWordsRegex(List<String> stopWords) {
        String pipedStopWords = String.join("|", stopWords);
        return Pattern.compile("\\b(?:" + pipedStopWords + ")\\b\\s*", Pattern.CASE_INSENSITIVE);
    }

    private static String removeNonAlphaNumericalChars(String string) {
        return string.replaceAll("[^\\p{Alnum}\\s]", "");
    }
}
