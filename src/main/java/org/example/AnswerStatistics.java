package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class AnswerStatistics {

    // Calculate statistics for numeric answers
    public static Map<String, Object> calculateNumericStats(List<Answer> answers) {
        Map<String, Object> numericAnswerStatistics = new HashMap<>();
        List<Integer> numericAnswers = answers.stream()
                .map(answer -> Integer.parseInt(answer.getSurveyAnswer()))
                .collect(Collectors.toList());

        if (numericAnswers.isEmpty()) {
            return numericAnswerStatistics;
        }

        double mean = numericAnswers.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        List<Integer> sorted = new ArrayList<>(numericAnswers);
        Collections.sort(sorted);

        double median = sorted.size() % 2 == 0
                ? (sorted.get(sorted.size() / 2 - 1) + sorted.get(sorted.size() / 2)) / 2.0
                : sorted.get(sorted.size() / 2);

        double stdDev = Math.sqrt(numericAnswers.stream()
                .mapToDouble(num -> Math.pow(num - mean, 2))
                .sum() / numericAnswers.size());

        int min = Collections.min(numericAnswers);
        int max = Collections.max(numericAnswers);

        numericAnswerStatistics.put("Mean", mean);
        numericAnswerStatistics.put("Median", median);
        numericAnswerStatistics.put("Standard Deviation", stdDev);
        numericAnswerStatistics.put("Min", min);
        numericAnswerStatistics.put("Max", max);

        return numericAnswerStatistics;
    }

    // Calculate counts for multiple-choice answers
    public static Map<String, Integer> calculateMultipleChoiceCounts(List<Answer> answers, List<String> options) {
        Map<String, Integer> choiceCounts = new HashMap<>();

        for (String option : options) {
            choiceCounts.put(option, 0);
        }

        for (Answer answer : answers) {
            String response = answer.getSurveyAnswer();
            if (choiceCounts.containsKey(response)) {
                choiceCounts.put(response, choiceCounts.get(response) + 1);
            }
        }

        return choiceCounts;
    }

    // Calculate percentages for multiple-choice answers
    public static Map<String, Double> calculateMultipleChoicePercentages(List<Answer> answers, List<String> options) {
        Map<String, Integer> choiceCounts = calculateMultipleChoiceCounts(answers, options);
        int totalAnswers = answers.size();

        return choiceCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (entry.getValue() * 100.0) / totalAnswers
                ));
    }
}
