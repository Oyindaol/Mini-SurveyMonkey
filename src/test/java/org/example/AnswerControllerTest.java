//package org.example;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.ui.Model;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//@ExtendWith(SpringExtension.class)
//public class AnswerControllerTest {
//
//    @Mock
//    private AnswerRepository answerRepository;
//
//    @Mock
//    private QuestionRepository questionRepository;
//
//    @Mock
//    private SurveyRepository surveyRepository;
//
//    @Mock
//    private Model model;
//
//    @InjectMocks
//    private AnswerController answerController;
//
//    @BeforeEach
//    public void setup(){
//        MockitoAnnotations.openMocks(this);
//    }
//
////    @Test
////    public void testDisplayAnswerForm(){
////        String viewName = answerController.displayAnswerForm(1L, 2L, model);
////        assertEquals("submitanswer", viewName);
////        verify(model).addAttribute("surveyId", 1L);
////        verify(model).addAttribute("questionId", 2L);
////        verify(model).addAttribute(eq("answer"), any(Answer.class));
////    }
////
////    @Test
////    public void testCreateAnswer(){
////        Question question = new Question();
////        question.setId(2L);
////        when(questionRepository.findById(2L)).thenReturn(Optional.of(question));
////
////        Answer answer = new Answer();
////        answer.setId(3L);
////
////        when(answerRepository.save(any(Answer.class))).thenReturn(answer);
////
////        String redirectUrl = answerController.createAnswer(1L, 2L, answer);
////        assertEquals("redirect:/survey/getbyid/1", redirectUrl);
////
////        verify(answerRepository).save(answer);
////        assertEquals(question, answer.getQuestion());
////    }
////
////    @Test
////    public void testCreateAnswer_QuestionNotFound(){
////        when(questionRepository.findById(2L)).thenReturn(Optional.empty());
////        Answer answer = new Answer();
////
////        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
////            answerController.createAnswer(1L, 2L, answer);
////        });
////
////        assertEquals("Survey not found", exception.getMessage());
////    }
//
//    @Test
//    public void testDisplaySurveyQuestions_Success(){
//        Long surveyId = 1L;
//        Survey survey = new Survey();
//        survey.setId(surveyId);
//        survey.setName("Test Survey");
//
//        Question question1 = new Question();
//        question1.setId(101L);
//        question1.setSurveyQuestion("Question 1");
//        question1.setQuestionType(Question.QuestionType.OPEN_ENDED);
//
//        Question question2 = new Question();
//        question2.setId(102L);
//        question2.setSurveyQuestion("Question 2");
//        question2.setQuestionType(Question.QuestionType.NUMERIC);
//
//        survey.setQuestions(Arrays.asList(question1, question2));
//
//        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
//
//        String viewName = answerController.displaySurveyQuestions(surveyId, model);
//
//        assertEquals("answersurvey", viewName);
//        verify(model).addAttribute("survey", survey);
//        verify(model).addAttribute(eq("answer"), any(Answer.class));
//    }
//
//    @Test
//    public void testDisplaySurveyQuestions_SurveyNotFound(){
//        Long surveyId = 1L;
//        when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            answerController.displaySurveyQuestions(surveyId, model);
//        });
//
//        assertEquals("Survey not found", exception.getMessage());
//    }
//
////    @Test
////    public void testSubmitAnswers_Success(){
////        Map<String, String> answers = new HashMap<>();
////        answers.put("answers[1].response", "Answer 1");
////        answers.put("answers[2].response", "Answer 2");
////
////        Long questionId1 = 1L;
////        Long questionId2 = 2L;
////
////        Question question1 = new Question();
////        question1.setId(questionId1);
////        question1.setSurveyQuestion("Question 1");
////        question1.setQuestionType(Question.QuestionType.OPEN_ENDED);
////        question1.setAnswers(new ArrayList<>());
////
////        Question question2 = new Question();
////        question2.setId(questionId2);
////        question2.setSurveyQuestion("Question 2");
////        question2.setQuestionType(Question.QuestionType.OPEN_ENDED);
////        question2.setAnswers(new ArrayList<>());
////
////        when(questionRepository.findById(questionId1)).thenReturn(Optional.of(question1));
////        when(questionRepository.findById(questionId2)).thenReturn(Optional.of(question2));
////
////        when(answerRepository.save(any(Answer.class))).thenAnswer(invocation -> {
////            Answer savedAnswer = invocation.getArgument(0);
////            savedAnswer.setId(new Random().nextLong());
////            return savedAnswer;
////        });
////
////        String redirectUrl = answerController.submitAnswers(answers);
////
////        assertEquals("redirect:/survey/getbyid/1", redirectUrl);
////
////        ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);
////        verify(answerRepository, times(2)).save(answerCaptor.capture());
////
////        List<Answer> capturedAnswers = answerCaptor.getAllValues();
////        assertEquals(2, capturedAnswers.size());
////
////        for (Answer capturedAnswer : capturedAnswers) {
////            if (capturedAnswer.getQuestion().equals(question1)) {
////                assertEquals("Answer 1", capturedAnswer.getSurveyAnswer());
////                assertTrue(question1.getAnswers().contains(capturedAnswer));
////            } else if (capturedAnswer.getQuestion().equals(question2)) {
////                assertEquals("Answer 2", capturedAnswer.getSurveyAnswer());
////                assertTrue(question2.getAnswers().contains(capturedAnswer));
////            } else {
////                fail("Captured answer has unexpected question: " + capturedAnswer.getQuestion());
////            }
////        }
////
////        assertEquals(1, question1.getAnswers().size());
////        assertEquals(1, question2.getAnswers().size());
////    }
//
//    @Test
//    public void testSubmitAnswers_QuestionNotFound(){
//        Map<String, String> answers = Map.of(
//                "answers[1].response", "Answer 1"
//        );
//
//        Long questionId = 1L;
//
//        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            answerController.submitAnswers(answers);
//        });
//
//        assertEquals("Survey not found", exception.getMessage());
//        verify(questionRepository, times(1)).findById(questionId);
//        verify(answerRepository, never()).save(any(Answer.class));
//    }
//
//}
