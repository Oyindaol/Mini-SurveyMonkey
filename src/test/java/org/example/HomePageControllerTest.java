package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class HomePageControllerTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private Model model;

    @InjectMocks
    private HomePageController homePageController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDisplayHomePage() {
        String viewName = homePageController.displayHomePage(model);

        assertEquals("homepage", viewName);
        verify(model).addAttribute("surveyName", "");
    }

    @Test
    public void testSearchSurvey_Success() {
        String surveyName = "Customer Satisfaction Survey";
        Survey survey = new Survey();
        survey.setId(1L);
        survey.setName(surveyName);

        when(surveyRepository.findByName(surveyName)).thenReturn(Optional.of(survey));

        String redirectUrl = homePageController.searchSurvey(surveyName, model);

        assertEquals("redirect:/survey/" + survey.getId() + "/respond", redirectUrl);
        verify(surveyRepository).findByName(surveyName);
    }

    @Test
    public void testSearchSurvey_SurveyNotFound() {
        String surveyName = "Non-Existent Survey";

        when(surveyRepository.findByName(surveyName)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            homePageController.searchSurvey(surveyName, model);
        });

        assertEquals("Survey not found with name: Non-Existent Survey", exception.getMessage());
        verify(surveyRepository).findByName(surveyName);
        verify(model, never()).addAttribute(eq("survey"), any());
    }
}
