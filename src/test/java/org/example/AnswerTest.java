package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AnswerTest {

    @Test
    public void testConstructor(){
        Survey survey = new Survey();
        survey.setId(1L);
        survey.setName("Customer Feedback Survey");
        Question question = new Question(survey, "How satisfied are you with our service?");
        String answerText = "Very Satisfied";

        Answer answer = new Answer(question, answerText);

        assertEquals(question, answer.getQuestion());
        assertEquals(answerText, answer.getSurveyAnswer());
        assertNull(answer.getId());
    }

    @Test
    public void testGetId(){
        Answer answer = new Answer();
        Long expectedId = 100L;
        answer.setId(expectedId);

        Long actualId = answer.getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetSurveyAnswer(){
        Answer answer = new Answer();
        String expectedAnswer = "Satisfied";
        answer.setSurveyAnswer(expectedAnswer);

        String actualAnswer = answer.getSurveyAnswer();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testSetQuestion(){
        Survey survey = new Survey();
        survey.setId(1L);
        survey.setName("Customer Feedback Survey");
        Question initialQuestion = new Question(survey, "How satisfied are you with our service?");
        Answer answer = new Answer(initialQuestion, "Satisfied");
        Question newQuestion = new Question(survey, "How likely are you to recommend our product?");

        answer.setQuestion(newQuestion);
        Question retrievedQuestion = answer.getQuestion();

        assertEquals(newQuestion, retrievedQuestion);

    }

    @Test
    public void testSetSurveyAnswer(){
        Answer answer = new Answer();
        String initialAnswer = "Neutral";
        answer.setSurveyAnswer(initialAnswer);
        String updatedAnswer = "Very Satisfied";

        answer.setSurveyAnswer(updatedAnswer);
        String retrievedAnswer = answer.getSurveyAnswer();

        assertEquals(updatedAnswer, retrievedAnswer);
    }
}
