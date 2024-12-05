//package org.example;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//
//public class QuestionTest {
//
//    @Test
//    public void testConstructor(){
//        Survey survey = new Survey();
//        survey.setId(1L);
//        survey.setName("Customer Feedback Survey");
//        String questionText = "How satisfied are you with our service?";
//
//        Question question = new Question(survey, questionText, Question.QuestionType.OPEN_ENDED);
//
//        assertEquals(survey, question.getSurvey());
//        assertEquals(questionText, question.getSurveyQuestion());
//        assertNotNull(question.getAnswers());
//        assertTrue(question.getAnswers().isEmpty());
//    }
//
//    @Test
//    public void testGetSurveyQuestion(){
//        Survey survey = new Survey();
//        survey.setId(1L);
//        survey.setName("Customer Feedback Survey");
//        String questionText = "How satisfied are you with our service?";
//        Question question = new Question(survey, questionText, Question.QuestionType.OPEN_ENDED);
//
//        String retrievedQuestion = question.getSurveyQuestion();
//
//        assertEquals(questionText, retrievedQuestion);
//    }
//
//    @Test
//    public void testSetSurveyQuestion(){
//        Survey survey = new Survey();
//        survey.setId(1L);
//        survey.setName("Customer Feedback Survey");
//        String initialQuestion = "How satisfied are you with our service?";
//        Question question = new Question(survey, initialQuestion, Question.QuestionType.OPEN_ENDED);
//        String updatedQuestion = "How likely are you to recommend our product?";
//
//        question.setSurveyQuestion(updatedQuestion);
//        String retrievedQuestion = question.getSurveyQuestion();
//
//        assertEquals(updatedQuestion, retrievedQuestion);
//    }
//
//    @Test
//    public void testGetAnswers(){
//        Survey survey = new Survey();
//        survey.setId(1L);
//        survey.setName("Customer Feedback Survey");
//        Question question = new Question(survey, "How satisfied are you with our service?", Question.QuestionType.OPEN_ENDED);
//
//        List<Answer> answers = question.getAnswers();
//
//        assertNotNull(answers);
//        assertTrue(answers.isEmpty());
//    }
//
//    @Test
//    public void testSetAnswers(){
//        Survey survey = new Survey();
//        survey.setId(1L);
//        survey.setName("Customer Feedback Survey");
//        Question question = new Question(survey, "How satisfied are you with our service?", Question.QuestionType.OPEN_ENDED);
//
//        List<Answer> newAnswers = new ArrayList<>();
//        Answer answer1 = new Answer();
//        answer1.setId(1L);
//        answer1.setSurveyAnswer("Very Satisfied");
//        answer1.setQuestion(question);
//
//        Answer answer2 = new Answer();
//        answer2.setId(2L);
//        answer2.setSurveyAnswer("Satisfied");
//        answer2.setQuestion(question);
//
//        newAnswers.add(answer1);
//        newAnswers.add(answer2);
//
//        question.setAnswers(newAnswers);
//        List<Answer> retrievedAnswers = question.getAnswers();
//
//        assertEquals(newAnswers, retrievedAnswers);
//        assertEquals(2, retrievedAnswers.size());
//        assertTrue(retrievedAnswers.contains(answer1));
//        assertTrue(retrievedAnswers.contains(answer2));
//    }
//
//    @Test
//    public void testGetAndSetMinValue(){
//        Question question = new Question();
//        Integer minValue = 1;
//        question.setMinValue(minValue);
//        assertEquals(minValue, question.getMinValue());
//    }
//
//    @Test
//    public void testGetAndSetMaxValue(){
//        Question question = new Question();
//        Integer maxValue = 10;
//        question.setMaxValue(maxValue);
//        assertEquals(maxValue, question.getMaxValue());
//    }
//
//    @Test
//    public void testSetOptions(){
//        Question question = new Question();
//        List<String> options = Arrays.asList("Option A", "Option B", "Option C");
//        question.setOptions(options);
//        assertEquals(options, question.getOptions());
//    }
//
//    @Test
//    public void testValidateAnswer_OpenEnded(){
//        Question question = new Question();
//        question.setQuestionType(Question.QuestionType.OPEN_ENDED);
//
//        assertTrue(question.validateAnswer("Any answer"));
//        assertTrue(question.validateAnswer(""));
//        assertTrue(question.validateAnswer(null));
//    }
//
//    @Test
//    public void testValidateAnswer_Numeric_Valid(){
//        Question question = new Question();
//        question.setQuestionType(Question.QuestionType.NUMERIC);
//        question.setMinValue(1);
//        question.setMaxValue(10);
//
//        assertTrue(question.validateAnswer("5"));
//        assertTrue(question.validateAnswer("1"));
//        assertTrue(question.validateAnswer("10"));
//    }
//
//    @Test
//    public void testValidateAnswer_Numeric_Invalid(){
//        Question question = new Question();
//        question.setQuestionType(Question.QuestionType.NUMERIC);
//        question.setMinValue(1);
//        question.setMaxValue(10);
//
//        assertFalse(question.validateAnswer("0"));
//        assertFalse(question.validateAnswer("11"));
//        assertFalse(question.validateAnswer("abc"));
//        assertFalse(question.validateAnswer(null));
//    }
//
//    @Test
//    public void testValidateAnswer_MultipleChoice_Valid(){
//        Question question = new Question();
//        question.setQuestionType(Question.QuestionType.MULTIPLE_CHOICE);
//        List<String> options = Arrays.asList("Option A", "Option B", "Option C");
//        question.setOptions(options);
//
//        assertTrue(question.validateAnswer("Option A"));
//        assertTrue(question.validateAnswer("Option B"));
//    }
//
//    @Test
//    public void testValidateAnswer_MultipleChoice_Invalid() {
//        Question question = new Question();
//        question.setQuestionType(Question.QuestionType.MULTIPLE_CHOICE);
//        List<String> options = Arrays.asList("Option A", "Option B", "Option C");
//        question.setOptions(options);
//
//        assertFalse(question.validateAnswer("Option D"));
//        assertFalse(question.validateAnswer(null));
//        assertFalse(question.validateAnswer(""));
//
//        question.setOptions(null);
//        assertFalse(question.validateAnswer("Option A"));
//    }
//}
