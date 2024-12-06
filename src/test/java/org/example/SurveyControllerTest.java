package org.example;

import org.ff4j.FF4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SurveyControllerTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FF4j ff4j;

    @Mock
    private Model model;

    @InjectMocks
    private SurveyController surveyController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDisplaySurveyForm() {
        String viewName = surveyController.displaySurveyForm(model);
        assertEquals("createsurvey", viewName);
        verify(model).addAttribute(eq("survey"), any(Survey.class));
    }

    @Test
    public void testCreateSurvey() {
        Survey survey = new Survey();
        survey.setId(1L);
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        String redirectUrl = surveyController.createSurvey(survey);
        assertEquals("redirect:/survey/1/question/create", redirectUrl);
        verify(surveyRepository).save(survey);
    }

    @Test
    public void testDisplaySurveyFormOnAccount_AccountFound() {
        Long accountId = 10L;
        Account account = new Account("user", "Valid1!");
        account.setId(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        String viewName = surveyController.displaySurveyFormOnAcount(accountId, model);
        assertEquals("createsurveywithaccount", viewName);
        verify(model).addAttribute("accountId", accountId);
        verify(model).addAttribute(eq("survey"), any(Survey.class));
    }

    @Test
    public void testDisplaySurveyFormOnAccount_AccountNotFound() {
        Long accountId = 10L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            surveyController.displaySurveyFormOnAcount(accountId, model);
        });

        assertTrue(ex.getMessage().contains("Account not found with ID: 10"));
    }

    @Test
    public void testCreateSurveyOnAccount_AccountFound() {
        Long accountId = 20L;
        Account account = new Account("user2", "Valid1!");
        account.setId(accountId);

        Survey survey = new Survey("New Survey");
        survey.setId(2L);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        String redirectUrl = surveyController.createSurveyOnAccount(survey, accountId);
        assertEquals("redirect:/survey/2/question/create", redirectUrl);
        verify(surveyRepository).save(survey);
        verify(accountRepository).save(account);
        assertTrue(account.getSurveys().contains(survey));
    }

    @Test
    public void testCreateSurveyOnAccount_AccountNotFound() {
        Long accountId = 20L;
        Survey survey = new Survey("Survey Missing Account");
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            surveyController.createSurveyOnAccount(survey, accountId);
        });
        assertTrue(ex.getMessage().contains("Account not found with ID: 20"));
        verify(surveyRepository).save(survey);
    }

    @Test
    public void testGetSurveyById() {
        Survey survey = new Survey();
        survey.setId(1L);
        survey.setName("Test Survey");
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        String viewName = surveyController.getSurveyById(1L, model);
        assertEquals("displaysurvey", viewName);
        verify(model).addAttribute("survey", survey);
    }

    @Test
    public void testGetSurveyById_NotFound() {
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            surveyController.getSurveyById(1L, model);
        });

        assertEquals("Survey not found with ID: 1", exception.getMessage());
    }

    @Test
    public void testGetSurveyByName() {
        String surveyName = "Customer Satisfaction Survey";
        Survey survey = new Survey();
        survey.setId(1L);
        survey.setName(surveyName);
        when(surveyRepository.findByName(surveyName)).thenReturn(Optional.of(survey));

        String viewName = surveyController.getSurveyByName(surveyName, model);
        assertEquals("displaysurvey", viewName);
        verify(surveyRepository).findByName(surveyName);
        verify(model).addAttribute("survey", survey);
    }

    @Test
    public void testGetSurveyByName_NotFound() {
        String surveyName = "Non-Existent Survey";
        when(surveyRepository.findByName(surveyName)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            surveyController.getSurveyByName(surveyName, model);
        });

        assertEquals("Survey not found with name: Non-Existent Survey", exception.getMessage());
        verify(surveyRepository).findByName(surveyName);
        verify(model, never()).addAttribute(eq("survey"), any());
    }

    @Test
    public void testViewSurveyCharts_FeatureDisabled() {
        Long surveyId = 3L;
        when(ff4j.check("CHART_GENERATOR")).thenReturn(false);

        FeatureDisabledException ex = assertThrows(FeatureDisabledException.class, () -> {
            surveyController.viewSurveyCharts(surveyId, model);
        });
        assertTrue(ex.getMessage().contains("Chart generator feature is disabled"));
    }

    @Test
    public void testViewSurveyCharts_SurveyNotFound() {
        Long surveyId = 3L;
        when(ff4j.check("CHART_GENERATOR")).thenReturn(true);
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            surveyController.viewSurveyCharts(surveyId, model);
        });
        assertTrue(ex.getMessage().contains("Survey not found with ID: 3"));
    }

    @Test
    public void testViewSurveyCharts_Success() {
        Long surveyId = 3L;
        Survey survey = new Survey("Chart Survey");
        survey.setId(surveyId);

        when(ff4j.check("CHART_GENERATOR")).thenReturn(true);
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        String viewName = surveyController.viewSurveyCharts(surveyId, model);
        assertEquals("viewcharts", viewName);
        verify(model).addAttribute("survey", survey);
        verify(model).addAttribute("surveyId", surveyId);
    }

    @Test
    public void testGetSurveyChartData_SurveyNotFound() {
        Long id = 4L;
        when(surveyRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            surveyController.getSurveyChartData(id);
        });
        assertTrue(ex.getMessage().contains("Survey not found with ID: 4"));
    }

    @Test
    public void testGetSurveyChartData_VariousQuestionTypes() {
        Long id = 5L;
        Survey survey = new Survey("Data Survey");
        survey.setId(id);

        Question numericQ = new Question(survey, "Rate 1-5", Question.QuestionType.NUMERIC);
        numericQ.setId(10L);

        Question multipleChoiceQ = new Question(survey, "Favourite colour?", Question.QuestionType.MULTIPLE_CHOICE);
        multipleChoiceQ.setId(11L);
        multipleChoiceQ.setOptions(Arrays.asList("Red","Blue"));

        Question openEndedQ = new Question(survey, "Any comments?", Question.QuestionType.OPEN_ENDED);
        openEndedQ.setId(12L);

        numericQ.setAnswers(new ArrayList<>());
        Answer a1 = new Answer();
        a1.setSurveyAnswer("3"); numericQ.addAnswer(a1);
        Answer a2 = new Answer();
        a2.setSurveyAnswer("5"); numericQ.addAnswer(a2);

        multipleChoiceQ.setAnswers(new ArrayList<>());
        Answer a3 = new Answer();
        a3.setSurveyAnswer("Red"); multipleChoiceQ.addAnswer(a3);
        Answer a4 = new Answer();
        a4.setSurveyAnswer("Blue"); multipleChoiceQ.addAnswer(a4);
        Answer a5 = new Answer();
        a5.setSurveyAnswer("Red"); multipleChoiceQ.addAnswer(a5);

        openEndedQ.setAnswers(Collections.emptyList());

        survey.setQuestions(Arrays.asList(numericQ, multipleChoiceQ, openEndedQ));
        when(surveyRepository.findById(id)).thenReturn(Optional.of(survey));

        Map<String, Object> chartData = surveyController.getSurveyChartData(id);

        Map<String, Object> numericData = (Map<String, Object>) chartData.get("question_10");
        assertEquals("Rate 1-5", numericData.get("questionName"));
        assertEquals("numeric", numericData.get("type"));
        Map<String, Object> numericStats = (Map<String, Object>) numericData.get("statistics");
        assertTrue(numericStats.containsKey("mean"));
        assertTrue(numericStats.containsKey("median"));
        assertTrue(numericStats.containsKey("stdDev"));

        Map<String, Object> mcData = (Map<String, Object>) chartData.get("question_11");
        assertEquals("Favourite colour?", mcData.get("questionName"));
        assertEquals("multiple_choice", mcData.get("type"));
        Map<String, Double> percentages = (Map<String, Double>) mcData.get("percentages");

        assertEquals(2, percentages.size());
        assertEquals(66.6667, percentages.get("Red"), 0.001);
        assertEquals(33.3333, percentages.get("Blue"), 0.001);

        Map<String, Object> oeData = (Map<String, Object>) chartData.get("question_12");
        assertEquals("Any comments?", oeData.get("questionName"));
        assertFalse(oeData.containsKey("type"));
        assertFalse(oeData.containsKey("statistics"));
        assertFalse(oeData.containsKey("percentages"));
    }

}
