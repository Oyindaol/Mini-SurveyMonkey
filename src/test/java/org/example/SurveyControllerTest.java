//package org.example;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.ui.Model;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//@ExtendWith(SpringExtension.class)
//public class SurveyControllerTest {
//
//    @Mock
//    private SurveyRepository surveyRepository;
//
//    @Mock
//    private Model model;
//
//    @InjectMocks
//    private SurveyController surveyController;
//
//    @BeforeEach
//    public void setup(){
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testDisplaySurveyForm(){
//        String viewName = surveyController.displaySurveyForm(model);
//        assertEquals("createsurvey", viewName);
//        verify(model).addAttribute(eq("survey"), any(Survey.class));
//    }
//
//    @Test
//    public void testCreateSurvey(){
//        Survey survey = new Survey();
//        survey.setId(1L);
//        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);
//
//        String redirectUrl = surveyController.createSurvey(survey);
//        assertEquals("redirect:/survey/1/question/create", redirectUrl);
//        verify(surveyRepository).save(survey);
//    }
//
//    @Test
//    public void testGetSurveyById(){
//        Survey survey = new Survey();
//        survey.setId(1L);
//        survey.setName("Test Survey");
//        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
//
//        String viewName = surveyController.getSurveyById(1L, model);
//        assertEquals("displaysurvey", viewName);
//        verify(model).addAttribute("survey", survey);
//    }
//
//    @Test
//    public void testGetSurveyById_NotFound(){
//        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, ()-> {
//            surveyController.getSurveyById(1L, model);
//        });
//
//        assertEquals("Survey not found", exception.getMessage());
//    }
//
//    @Test
//    public void testGetSurveyByName() {
//        String surveyName = "Customer Satisfaction Survey";
//        Survey survey = new Survey();
//        survey.setId(1L);
//        survey.setName(surveyName);
//        when(surveyRepository.findByName(surveyName)).thenReturn(Optional.of(survey));
//
//        String viewName = surveyController.getSurveyByName(surveyName, model);
//
//        assertEquals("displaysurvey", viewName);
//        verify(surveyRepository).findByName(surveyName);
//        verify(model).addAttribute("survey", survey);
//    }
//
//    @Test
//    public void testGetSurveyByName_NotFound() {
//        String surveyName = "Non-Existent Survey";
//        when(surveyRepository.findByName(surveyName)).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            surveyController.getSurveyByName(surveyName, model);
//        });
//
//        assertEquals("Survey not found", exception.getMessage());
//        verify(surveyRepository).findByName(surveyName);
//        verify(model, never()).addAttribute(eq("survey"), any());
//    }
//}
