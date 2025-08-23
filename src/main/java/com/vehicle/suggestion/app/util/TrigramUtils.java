package com.vehicle.suggestion.app.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TrigramUtils {

    public static List<String> generateTrigrams(String input) {
        if (input == null) return List.of();

        String normalized = input.toLowerCase().replaceAll("\\s+", ""); //to lowercase and remove spaces
        List<String> trigrams = new ArrayList<>();

        for (int i = 0; i < normalized.length() - 2; i++) {
            trigrams.add(normalized.substring(i, i + 3));
        }
        return trigrams;
    }

    public static boolean trigramMatch(String source, String query) {
        if (StringUtils.isBlank(query)) {
            return true;
        }
        List<String> sourceTrigrams = generateTrigrams(source);
        List<String> queryTrigrams = generateTrigrams(query);

        long matchCount = queryTrigrams.stream()
                .filter(sourceTrigrams::contains)
                .count();

        double matches = (double) matchCount / queryTrigrams.size();
        return matches >= 0.3; //threshold of 30% match
    }
}
