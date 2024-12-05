package org.example;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class AnswerTest {

    private Question createOpenEndedQuestion() {
        Survey survey = new Survey("Test Survey");
        survey.setId(1L);
        return new Question(survey, "Open-ended question?", Question.QuestionType.OPEN_ENDED);
    }

    private Question createNumericQuestion() {
        Survey survey = new Survey("Numeric Survey");
        survey.setId(2L);
        Question q = new Question(survey, "Rate 1-10", Question.QuestionType.NUMERIC);
        q.setMinValue(1);
        q.setMaxValue(10);
        return q;
    }

    @Test
    public void testNoArgConstructor() {
        Answer answer = new Answer();
        assertNull(answer.getId());
        assertNull(answer.getSurveyAnswer());
        assertNull(answer.getQuestion());
    }

    @Test
    public void testConstructor_ValidAnswer() {
        Question question = createOpenEndedQuestion();
        String answerText = "Any answer";
        Answer answer = new Answer(question, answerText);

        assertEquals(question, answer.getQuestion());
        assertEquals(answerText, answer.getSurveyAnswer());
        assertNull(answer.getId());
    }

    @Test
    public void testConstructor_InvalidAnswer() {
        Question numericQuestion = createNumericQuestion();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new Answer(numericQuestion, "0");
        });
        assertTrue(ex.getMessage().contains("Invalid answer for question type: NUMERIC"));
    }

    @Test
    public void testGetSetId() {
        Answer answer = new Answer();
        Long expectedId = 100L;
        answer.setId(expectedId);
        assertEquals(expectedId, answer.getId());
    }

    @Test
    public void testGetSetSurveyAnswer_NoQuestion() {
        Answer answer = new Answer();
        answer.setSurveyAnswer("No question set");
        assertEquals("No question set", answer.getSurveyAnswer());
    }

    @Test
    public void testSetSurveyAnswer_Valid() {
        Question numericQuestion = createNumericQuestion();
        Answer answer = new Answer(numericQuestion, "5");
        answer.setSurveyAnswer("10");
        assertEquals("10", answer.getSurveyAnswer());
    }

    @Test
    public void testSetSurveyAnswer_Invalid() {
        Question numericQuestion = createNumericQuestion();
        Answer answer = new Answer(numericQuestion, "5");
        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> {
            answer.setSurveyAnswer("0");
        });
        assertTrue(ex.getMessage().contains("Invalid answer for question type: NUMERIC"));
    }

    @Test
    public void testGetSetQuestion() {
        Answer answer = new Answer();
        Question q = createOpenEndedQuestion();
        answer.setQuestion(q);
        assertEquals(q, answer.getQuestion());
        answer.setSurveyAnswer("Valid even after question set");
        assertEquals("Valid even after question set", answer.getSurveyAnswer());
    }

    @Test
    public void testSetSurveyAnswer_NullQuestion() {
        Answer answer = new Answer();
        answer.setSurveyAnswer("Some answer");
        assertEquals("Some answer", answer.getSurveyAnswer());

        Question q = createNumericQuestion();
        answer.setQuestion(q);
        answer.setSurveyAnswer("5");
        assertEquals("5", answer.getSurveyAnswer());
    }

    @Test
    public void testSetSurveyAnswer_MultipleChoice() {
        Survey survey = new Survey("MC Survey");
        Question mcQuestion = new Question(survey, "Favourite colour?", Question.QuestionType.MULTIPLE_CHOICE);
        mcQuestion.setOptions(Collections.singletonList("Red"));

        Answer answer = new Answer(mcQuestion, "Red");
        assertEquals("Red", answer.getSurveyAnswer());

        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> {
            answer.setSurveyAnswer("Blue");
        });
        assertTrue(ex.getMessage().contains("Invalid answer for question type: MULTIPLE_CHOICE"));
    }
}
