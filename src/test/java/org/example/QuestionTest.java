package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class QuestionTest {

    @Test
    public void testConstructor() {
        Survey survey = new Survey("Customer Feedback Survey");
        survey.setId(1L);
        String questionText = "How satisfied are you with our service?";

        Question question = new Question(survey, questionText, Question.QuestionType.OPEN_ENDED);

        assertEquals(survey, question.getSurvey());
        assertEquals(questionText, question.getSurveyQuestion());
        assertNotNull(question.getAnswers());
        assertTrue(question.getAnswers().isEmpty());
    }

    @Test
    public void testGetSetSurveyQuestion() {
        Survey survey = new Survey("Customer Feedback Survey");
        survey.setId(1L);
        String initialQuestion = "How satisfied are you with our service?";
        Question question = new Question(survey, initialQuestion, Question.QuestionType.OPEN_ENDED);

        assertEquals(initialQuestion, question.getSurveyQuestion());

        String updatedQuestion = "How likely are you to recommend our product?";
        question.setSurveyQuestion(updatedQuestion);
        assertEquals(updatedQuestion, question.getSurveyQuestion());
    }

    @Test
    public void testGetSetAnswers() {
        Survey survey = new Survey("Customer Feedback Survey");
        survey.setId(1L);
        Question question = new Question(survey, "How satisfied are you?", Question.QuestionType.OPEN_ENDED);

        List<Answer> newAnswers = new ArrayList<>();
        Answer answer1 = new Answer();
        answer1.setId(1L);
        answer1.setSurveyAnswer("Very Satisfied");
        answer1.setQuestion(question);

        Answer answer2 = new Answer();
        answer2.setId(2L);
        answer2.setSurveyAnswer("Satisfied");
        answer2.setQuestion(question);

        newAnswers.add(answer1);
        newAnswers.add(answer2);

        question.setAnswers(newAnswers);
        List<Answer> retrievedAnswers = question.getAnswers();

        assertEquals(newAnswers, retrievedAnswers);
        assertEquals(2, retrievedAnswers.size());
        assertTrue(retrievedAnswers.contains(answer1));
        assertTrue(retrievedAnswers.contains(answer2));
    }

    @Test
    public void testAddAnswer() {
        Survey survey = new Survey("Survey");
        Question question = new Question(survey, "Q?", Question.QuestionType.OPEN_ENDED);
        Answer ans = new Answer();
        ans.setSurveyAnswer("test");
        ans.setQuestion(question);

        question.addAnswer(ans);
        assertEquals(1, question.getAnswers().size());
        assertEquals(ans, question.getAnswers().get(0));
    }

    @Test
    public void testGetSetMinValue() {
        Question question = new Question();
        Integer minValue = 1;
        question.setMinValue(minValue);
        assertEquals(minValue, question.getMinValue());
    }

    @Test
    public void testGetSetMaxValue() {
        Question question = new Question();
        Integer maxValue = 10;
        question.setMaxValue(maxValue);
        assertEquals(maxValue, question.getMaxValue());
    }

    @Test
    public void testGetSetOptions() {
        Question question = new Question();
        List<String> options = Arrays.asList("Option A", "Option B", "Option C");
        question.setOptions(options);
        assertEquals(options, question.getOptions());
    }

    @Test
    public void testValidateAnswer_OpenEnded() {
        Question question = new Question();
        question.setQuestionType(Question.QuestionType.OPEN_ENDED);

        assertTrue(question.validateAnswer("Any answer"));
        assertTrue(question.validateAnswer(""));
        assertTrue(question.validateAnswer(null));
    }

    @Test
    public void testValidateAnswer_Numeric_Valid() {
        Question question = new Question();
        question.setQuestionType(Question.QuestionType.NUMERIC);
        question.setMinValue(1);
        question.setMaxValue(10);

        assertTrue(question.validateAnswer("5"));
        assertTrue(question.validateAnswer("1"));
        assertTrue(question.validateAnswer("10"));
    }

    @Test
    public void testValidateAnswer_Numeric_Invalid() {
        Question question = new Question();
        question.setQuestionType(Question.QuestionType.NUMERIC);
        question.setMinValue(1);
        question.setMaxValue(10);

        assertFalse(question.validateAnswer("0"));
        assertFalse(question.validateAnswer("11"));
        assertFalse(question.validateAnswer("abc"));
        assertFalse(question.validateAnswer(null));
    }

    @Test
    public void testValidateAnswer_MultipleChoice_Valid() {
        Question question = new Question();
        question.setQuestionType(Question.QuestionType.MULTIPLE_CHOICE);
        List<String> options = Arrays.asList("Option A", "Option B", "Option C");
        question.setOptions(options);

        assertTrue(question.validateAnswer("Option A"));
        assertTrue(question.validateAnswer("Option B"));
    }

    @Test
    public void testValidateAnswer_MultipleChoice_Invalid() {
        Question question = new Question();
        question.setQuestionType(Question.QuestionType.MULTIPLE_CHOICE);
        List<String> options = Arrays.asList("Option A", "Option B", "Option C");
        question.setOptions(options);

        assertFalse(question.validateAnswer("Option D"));
        assertFalse(question.validateAnswer(null));
        assertFalse(question.validateAnswer(""));

        question.setOptions(null);
        assertFalse(question.validateAnswer("Option A"));
    }

    @Test
    public void testCalculateAndSaveStatistics_Numeric_WithData() {
        Survey survey = new Survey("Numeric Data Survey");
        Question question = new Question(survey, "Rate 1-5", Question.QuestionType.NUMERIC);
        question.setMinValue(1);
        question.setMaxValue(5);
        List<Answer> answers = new ArrayList<>();
        answers.add(createAnswer(question, "3"));
        answers.add(createAnswer(question, "1"));
        answers.add(createAnswer(question, "5"));
        question.setAnswers(answers);

        question.calculateAndSaveStatistics();

        assertEquals(3, question.getMean());
        assertEquals(3, question.getMedian());
        assertTrue(question.getStandardDeviation() >= 1 && question.getStandardDeviation() <= 2);
        assertEquals(1, question.getSmallestAnswer());
        assertEquals(5, question.getLargestAnswer());
    }

    @Test
    public void testCalculateAndSaveStatistics_MultipleChoice_WithData() {
        Survey survey = new Survey("MC Data");
        Question question = new Question(survey, "Favourite fruit?", Question.QuestionType.MULTIPLE_CHOICE);
        question.setOptions(Arrays.asList("Apple","Banana","Cherry"));

        List<Answer> answers = new ArrayList<>();
        answers.add(createAnswer(question, "Apple"));
        answers.add(createAnswer(question, "Apple"));
        answers.add(createAnswer(question, "Banana"));
        question.setAnswers(answers);

        question.calculateAndSaveStatistics();
        assertEquals(2, (int) question.getOptionsCount().get("Apple"));
        assertEquals(1, (int) question.getOptionsCount().get("Banana"));
        assertEquals(0, (int) question.getOptionsCount().get("Cherry"));

        assertEquals((2*100.0)/3, question.getOptionsPercentage().get("Apple"), 0.001);
        assertEquals((1*100.0)/3, question.getOptionsPercentage().get("Banana"), 0.001);
        assertEquals(0.0, question.getOptionsPercentage().get("Cherry"), 0.001);
    }

    @Test
    public void testSetStatisticFieldsDirectly() {
        Question question = new Question();
        question.setMean(5);
        question.setMedian(3);
        question.setStandardDeviation(2);
        question.setLargestAnswer(10);
        question.setSmallestAnswer(1);

        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("OptionA", 2);
        question.setOptionsCount(countMap);

        Map<String, Double> percMap = new HashMap<>();
        percMap.put("OptionA", 50.0);
        question.setOptionsPercentage(percMap);

        assertEquals(5, question.getMean());
        assertEquals(3, question.getMedian());
        assertEquals(2, question.getStandardDeviation());
        assertEquals(10, question.getLargestAnswer());
        assertEquals(1, question.getSmallestAnswer());
        assertEquals(countMap, question.getOptionsCount());
        assertEquals(percMap, question.getOptionsPercentage());
    }

    private Answer createAnswer(Question question, String response) {
        Answer ans = new Answer();
        ans.setSurveyAnswer(response);
        ans.setQuestion(question);
        return ans;
    }
}
