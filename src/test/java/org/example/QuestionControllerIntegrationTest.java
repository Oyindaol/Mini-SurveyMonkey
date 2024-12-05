package org.example;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuestionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long createSurveyAndGetId(String name) throws Exception {
        String redirectUrl = mockMvc.perform(post("/survey")
                        .param("name", name))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse().getRedirectedUrl();

        Pattern pattern = Pattern.compile("/survey/(\\d+)/question/create");
        Matcher matcher = pattern.matcher(redirectUrl);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Cannot extract survey ID from redirect URL");
        }
    }

    @Test
    public void testDisplayQuestionForm_SurveyFound() throws Exception {
        Long surveyId = createSurveyAndGetId("Product Feedback Survey");

        mockMvc.perform(get("/survey/{surveyId}/question/create", surveyId))
                .andExpect(status().isOk())
                .andExpect(view().name("createquestion"))
                .andExpect(model().attributeExists("surveyId"))
                .andExpect(model().attribute("surveyId", surveyId))
                .andExpect(model().attributeExists("question"));
    }

    @Test
    public void testCreateQuestion_MultipleChoiceSuccess() throws Exception {
        Long surveyId = createSurveyAndGetId("Survey MC Success");
        mockMvc.perform(post("/survey/{surveyId}/question/create", surveyId)
                        .param("surveyQuestion", "Favourite fruit?")
                        .param("choices", "MULTIPLE_CHOICE")
                        .param("multipleChoiceInput", "Apple,Banana,Cherry"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/survey/getbyid/" + surveyId));
    }

    @Test
    public void testCreateQuestion_OpenEndedSuccess() throws Exception {
        Long surveyId = createSurveyAndGetId("Survey Open-Ended");
        mockMvc.perform(post("/survey/{surveyId}/question/create", surveyId)
                        .param("surveyQuestion", "Any comments?")
                        .param("choices", "OPEN_ENDED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/survey/getbyid/" + surveyId));
    }

    @Test
    public void testCreateQuestion_NumericSuccess() throws Exception {
        Long surveyId = createSurveyAndGetId("Survey Numeric");
        mockMvc.perform(post("/survey/{surveyId}/question/create", surveyId)
                        .param("surveyQuestion", "Rate 1-5")
                        .param("choices", "NUMERIC"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/survey/getbyid/" + surveyId));
    }
}
