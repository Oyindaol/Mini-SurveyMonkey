package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class AnswerStatistics {

    // Method to calculate statistics for numeric answers
    public static Map<String, Object> calculateNumericStats(List<Answer> answers) {
        Map<String, Object> numericAnswerStatistics = new HashMap<>();
        List<Integer> numericAnswers = answers.stream()
                .map(answer -> Integer.parseInt(answer.getSurveyAnswer()))
                .collect(Collectors.toList());

        if (numericAnswers.isEmpty()) {
            return numericAnswerStatistics;
        }

        // Calculations for the mean
        double mean = numericAnswers.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        // Create a sorted list for the following statistics
        List<Integer> sorted = new ArrayList<>(numericAnswers);
        Collections.sort(sorted);

        // Calculations for the median
        double median = sorted.size() % 2 == 0
                ? (sorted.get(sorted.size() / 2 - 1) + sorted.get(sorted.size() / 2)) / 2.0
                : sorted.get(sorted.size() / 2);

        // Calculations for the standard deviation
        double stdDev = Math.sqrt(numericAnswers.stream()
                .mapToDouble(num -> Math.pow(num - mean, 2))
                .sum() / numericAnswers.size());

        // Calculations for the minimum and maximum value
        int min = Collections.min(numericAnswers);
        int max = Collections.max(numericAnswers);

        numericAnswerStatistics.put("Mean", mean);
        numericAnswerStatistics.put("Median", median);
        numericAnswerStatistics.put("Standard Deviation", stdDev);
        numericAnswerStatistics.put("Min", min);
        numericAnswerStatistics.put("Max", max);

        return numericAnswerStatistics;
    }

    // Method to calculate counts for multiple-choice answers
    public static Map<String, Integer> calculateMultipleChoiceCounts(List<Answer> answers, List<String> options) {
        Map<String, Integer> choiceCounts = new HashMap<>();

        // Initialize choiceCounts for each option
        for (String option : options) {
            choiceCounts.put(option, 0);
        }

        // Count occurrences of each answer
        for (Answer answer : answers) {
            String response = answer.getSurveyAnswer();
            if (choiceCounts.containsKey(response)) {
                choiceCounts.put(response, choiceCounts.get(response) + 1);
            }
        }

        return choiceCounts;
    }

    // Method to calculate percentages for multiple-choice answers
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
