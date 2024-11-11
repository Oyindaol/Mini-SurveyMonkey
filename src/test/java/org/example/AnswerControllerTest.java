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
public class AnswerControllerTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private Model model;

    @InjectMocks
    private AnswerController answerController;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDisplayAnswerForm(){
        String viewName = answerController.displayAnswerForm(1L, 2L, model);
        assertEquals("submitanswer", viewName);
        verify(model).addAttribute("surveyId", 1L);
        verify(model).addAttribute("questionId", 2L);
        verify(model).addAttribute(eq("answer"), any(Answer.class));
    }

    @Test
    public void testCreateAnswer(){
        Question question = new Question();
        question.setId(2L);
        when(questionRepository.findById(2L)).thenReturn(Optional.of(question));

        Answer answer = new Answer();
        answer.setId(3L);

        when(answerRepository.save(any(Answer.class))).thenReturn(answer);

        String redirectUrl = answerController.createAnswer(1L, 2L, answer);
        assertEquals("redirect:/survey/getbyid/1", redirectUrl);

        verify(answerRepository).save(answer);
        assertEquals(question, answer.getQuestion());
    }

    @Test
    public void testCreateAnswer_QuestionNotFound(){
        when(questionRepository.findById(2L)).thenReturn(Optional.empty());
        Answer answer = new Answer();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            answerController.createAnswer(1L, 2L, answer);
        });

        assertEquals("Survey not found", exception.getMessage());
    }
}
