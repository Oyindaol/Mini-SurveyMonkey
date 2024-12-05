package org.example;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class AnswerStatisticsTest {

    private Answer createAnswer(String surveyAnswer) {
        Answer answer = new Answer();
        answer.setSurveyAnswer(surveyAnswer);
        return answer;
    }

    @Test
    public void testCalculateNumericStats_EmptyList() {
        List<Answer> answers = Collections.emptyList();
        Map<String, Object> result = AnswerStatistics.calculateNumericStats(answers);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCalculateNumericStats_OddCount() {
        List<Answer> answers = Arrays.asList(
                createAnswer("1"),
                createAnswer("3"),
                createAnswer("5")
        );
        Map<String, Object> result = AnswerStatistics.calculateNumericStats(answers);

        assertEquals(3.0, (double) result.get("Mean"), 0.0001);
        assertEquals(3.0, (double) result.get("Median"), 0.0001);
        double stdDev = (double) result.get("Standard Deviation");
        assertEquals(1.6329, stdDev, 0.001);
        assertEquals(1, (int) result.get("Min"));
        assertEquals(5, (int) result.get("Max"));
    }

    @Test
    public void testCalculateNumericStats_EvenCount() {
        List<Answer> answers = Arrays.asList(
                createAnswer("2"),
                createAnswer("4"),
                createAnswer("6"),
                createAnswer("8")
        );
        Map<String, Object> result = AnswerStatistics.calculateNumericStats(answers);

        assertEquals(5.0, (double) result.get("Mean"), 0.0001);
        assertEquals(5.0, (double) result.get("Median"), 0.0001);
        double stdDev = (double) result.get("Standard Deviation");
        assertEquals(2.2360, stdDev, 0.001);
        assertEquals(2, (int) result.get("Min"));
        assertEquals(8, (int) result.get("Max"));
    }

    @Test
    public void testCalculateMultipleChoiceCounts_EmptyAnswers() {
        List<Answer> answers = Collections.emptyList();
        List<String> options = Arrays.asList("OptionA", "OptionB");
        Map<String, Integer> result = AnswerStatistics.calculateMultipleChoiceCounts(answers, options);

        assertEquals(2, result.size());
        assertEquals(0, (int) result.get("OptionA"));
        assertEquals(0, (int) result.get("OptionB"));
    }

    @Test
    public void testCalculateMultipleChoiceCounts_SomeMatches() {
        List<Answer> answers = Arrays.asList(
                createAnswer("OptionA"),
                createAnswer("InvalidOption"),
                createAnswer("OptionB"),
                createAnswer("OptionA")
        );
        List<String> options = Arrays.asList("OptionA", "OptionB");
        Map<String, Integer> result = AnswerStatistics.calculateMultipleChoiceCounts(answers, options);

        assertEquals(2, result.size());
        assertEquals(2, (int) result.get("OptionA"));
        assertEquals(1, (int) result.get("OptionB"));
    }


    @Test
    public void testCalculateMultipleChoicePercentages_NoAnswersNoOptions() {
        List<Answer> answers = Collections.emptyList();
        List<String> options = Collections.emptyList();
        Map<String, Double> result = AnswerStatistics.calculateMultipleChoicePercentages(answers, options);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCalculateMultipleChoicePercentages_WithAnswers() {
        List<Answer> answers = Arrays.asList(
                createAnswer("OptionA"),
                createAnswer("OptionA"),
                createAnswer("OptionB")
        );
        List<String> options = Arrays.asList("OptionA", "OptionB", "OptionC");

        Map<String, Double> result = AnswerStatistics.calculateMultipleChoicePercentages(answers, options);

        assertEquals(3, result.size());
        assertEquals(66.6667, result.get("OptionA"), 0.001);
        assertEquals(33.3333, result.get("OptionB"), 0.001);
        assertEquals(0.0, result.get("OptionC"), 0.0001);
    }

}
