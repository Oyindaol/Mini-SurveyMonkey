package org.example;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SurveyTest {

    @Test
    public void testNoArgConstructor() {
        Survey survey = new Survey();
        assertNull(survey.getName());
        assertNotNull(survey.getQuestions());
        assertTrue(survey.getQuestions().isEmpty());
        assertNull(survey.getId());
    }

    @Test
    public void testConstructorWithName() {
        String surveyName = "Customer Satisfaction Survey";
        Survey survey = new Survey(surveyName);

        assertEquals(surveyName, survey.getName());
        assertNotNull(survey.getQuestions());
        assertTrue(survey.getQuestions().isEmpty());
        assertNull(survey.getId());
    }

    @Test
    public void testGetSetId() {
        Survey survey = new Survey();
        Long idValue = 42L;
        survey.setId(idValue);
        assertEquals(idValue, survey.getId());
    }

    @Test
    public void testGetSetName() {
        String surveyName = "Employee Feedback Survey";
        Survey survey = new Survey();
        survey.setName(surveyName);

        String retrievedName = survey.getName();
        assertEquals(surveyName, retrievedName);
    }

    @Test
    public void testGetQuestionsInitiallyEmpty() {
        Survey survey = new Survey();
        List<Question> questions = survey.getQuestions();

        assertNotNull(questions);
        assertTrue(questions.isEmpty());
    }

    @Test
    public void testSetQuestions() {
        Survey survey = new Survey();
        List<Question> newQuestions = new ArrayList<>();
        Question q1 = new Question();
        Question q2 = new Question();
        newQuestions.add(q1);
        newQuestions.add(q2);

        survey.setQuestions(newQuestions);
        List<Question> retrievedQuestions = survey.getQuestions();

        assertEquals(newQuestions, retrievedQuestions);
        assertEquals(2, retrievedQuestions.size());
        assertTrue(retrievedQuestions.contains(q1));
        assertTrue(retrievedQuestions.contains(q2));
    }

    @Test
    public void testAddQuestion() {
        Survey survey = new Survey("Add Question Test");
        Question q = new Question();
        assertTrue(survey.getQuestions().isEmpty());

        survey.addQuestion(q);
        assertEquals(1, survey.getQuestions().size());
        assertEquals(q, survey.getQuestions().get(0));
    }
}
