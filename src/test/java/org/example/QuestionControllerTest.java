package org.example;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class QuestionControllerTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private Model model;

    @InjectMocks
    private QuestionController questionController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDisplayQuestionForm() {
        Long surveyId = 1L;
        Survey survey = new Survey("Test Survey");
        survey.setId(surveyId);
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        String viewName = questionController.displayQuestionFrom(surveyId, model);
        assertEquals("createquestion", viewName);
        verify(model).addAttribute(eq("surveyId"), eq(surveyId));
        verify(model).addAttribute(eq("question"), any(Question.class));
    }

    @Test
    public void testDisplayQuestionForm_SurveyNotFound() {
        Long surveyId = 1L;
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            questionController.displayQuestionFrom(surveyId, model);
        });

        assertTrue(exception.getMessage().contains("Survey not found with ID: 1"));
    }

    @Test
    public void testCreateQuestion() {
        Long surveyId = 1L;
        Survey survey = new Survey("Customer Survey");
        survey.setId(surveyId);
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        Question question = new Question();
        question.setId(2L);

        when(questionRepository.save(any(Question.class))).thenReturn(question);
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        String redirectUrl = questionController.createQuestion(surveyId, question, "OPEN_ENDED", null);
        assertEquals("redirect:/survey/getbyid/1", redirectUrl);

        verify(questionRepository).save(question);
        verify(surveyRepository).save(survey);
        assertEquals(survey, question.getSurvey());
    }

    @Test
    public void testCreateQuestion_SurveyNotFound() {
        Long surveyId = 1L;
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

        Question question = new Question();

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            questionController.createQuestion(surveyId, question, "OPEN_ENDED", null);
        });

        assertTrue(exception.getMessage().contains("Survey not found with ID: 1"));
    }

    @Test
    public void testCreateQuestion_MultipleChoice() {
        Long surveyId = 1L;
        Survey survey = new Survey("Test Survey");
        survey.setId(surveyId);
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        Question question = new Question();
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> {
            Question q = invocation.getArgument(0);
            q.setId(2L);
            return q;
        });

        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        String questionType = "MULTIPLE_CHOICE";
        String answerChoice = "Option A,Option B,Option C";

        String redirectUrl = questionController.createQuestion(surveyId, question, questionType, answerChoice);
        assertEquals("redirect:/survey/getbyid/" + surveyId, redirectUrl);

        assertEquals(Question.QuestionType.MULTIPLE_CHOICE, question.getQuestionType());
        List<String> expectedOptions = Arrays.asList("Option A", "Option B", "Option C");
        assertEquals(expectedOptions, question.getOptions());
        assertEquals(survey, question.getSurvey());
        assertTrue(survey.getQuestions().contains(question));

        verify(questionRepository).save(question);
        verify(surveyRepository).save(survey);
    }

    @Test
    public void testCreateQuestion_InvalidQuestionType() {
        Long surveyId = 1L;
        Survey survey = new Survey("Invalid Type Survey");
        survey.setId(surveyId);
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        Question question = new Question();
        String invalidType = "UNKNOWN_TYPE";

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            questionController.createQuestion(surveyId, question, invalidType, null);
        });

        assertTrue(exception.getMessage().contains("Invalid question type: " + invalidType));
    }

    @Test
    public void testCreateQuestion_MissingMultipleChoiceOptions() {
        Long surveyId = 1L;
        Survey survey = new Survey("No Options Survey");
        survey.setId(surveyId);
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        Question question = new Question();
        String questionType = "MULTIPLE_CHOICE";
        String answerChoice = "";

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            questionController.createQuestion(surveyId, question, questionType, answerChoice);
        });

        assertTrue(exception.getMessage().contains("Multiple choice options are required"));
    }
}
