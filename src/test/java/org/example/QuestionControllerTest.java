package org.example;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDisplayQuestionForm(){
        String viewName = questionController.displayQuestionFrom(1L, model);
        assertEquals("createquestion", viewName);
        verify(model).addAttribute(eq("surveyId"), eq(1L));
        verify(model).addAttribute(eq("question"), any(Question.class));
    }

    @Test
    public void testCreateQuestion(){
        Survey survey = new Survey();
        survey.setId(1L);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        Question question = new Question();
        question.setId(2L);

        when(questionRepository.save(any(Question.class))).thenReturn(question);
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        String redirectUrl = questionController.createQuestion(1L, question);
        assertEquals("redirect:/survey/getbyid/1", redirectUrl);

        verify(questionRepository).save(question);
        verify(surveyRepository).save(survey);
        assertEquals(survey, question.getSurvey());
    }

    @Test
    public void testCreateQuestion_SurveyNotFound(){
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());
        Question question = new Question();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            questionController.createQuestion(1L, question);
        });

        assertEquals("Survey not found", exception.getMessage());
    }
}