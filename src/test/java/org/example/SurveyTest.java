//package org.example;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SurveyTest {
//
//    @Test
//    public void testConstructor(){
//        String surveyName = "Customer Satisfaction Survey";
//
//        Survey survey = new Survey(surveyName);
//
//        assertEquals(surveyName, survey.getName());
//        assertNotNull(survey.getQuestions());
//        assertTrue(survey.getQuestions().isEmpty());
//
//    }
//
//    @Test
//    public void testGetName(){
//        String surveyName = "Employee Feedback Survey";
//        Survey survey = new Survey();
//        survey.setName(surveyName);
//
//        String retrievedName = survey.getName();
//
//        assertEquals(surveyName, retrievedName);
//    }
//
//    @Test
//    public void testGetQuestions(){
//        Survey survey = new Survey();
//
//        List<Question> questions = survey.getQuestions();
//
//        assertNotNull(questions);
//        assertTrue(questions.isEmpty());
//    }
//
//    @Test
//    public void testSetQuestions(){
//        Survey survey = new Survey();
//        List<Question> newQuestions = new ArrayList<>();
//        newQuestions.add(new Question());
//        newQuestions.add(new Question());
//
//        survey.setQuestions(newQuestions);
//        List<Question> retrievedQuestions = survey.getQuestions();
//
//        assertEquals(newQuestions, retrievedQuestions);
//        assertEquals(2, retrievedQuestions.size());
//    }
//}
