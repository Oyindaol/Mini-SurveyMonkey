package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AnswerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testDisplaySurveyQuestions_Success() throws Exception {
        String surveyName = "Customer Satisfaction Survey";
        MvcResult surveyResult = mockMvc.perform(post("/survey")
                        .param("name", surveyName))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        Long surveyId = extractSurveyId(surveyResult.getResponse().getRedirectedUrl());

        mockMvc.perform(post("/survey/{surveyId}/question/create", surveyId)
                        .param("surveyQuestion", "How satisfied are you with our service?")
                        .param("choices", "OPEN_ENDED"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/survey/{surveyId}/respond", surveyId))
                .andExpect(status().isOk())
                .andExpect(view().name("answersurvey"))
                .andExpect(model().attributeExists("survey"))
                .andExpect(model().attribute("survey", hasProperty("id", is(surveyId))))
                .andExpect(model().attribute("survey", hasProperty("questions", hasSize(1))));
    }

    /**
     * WIP:
     * testDisplaySurveyQuestions_SurveyNotFound(): ends with unhandled exception
     * testSubmitAnswers_Success(): seems like there is a hardcoded survey ID
     * testSubmitAnswers_InvalidData(): look to have controller handle validation error
     */
//    @Test
//    public void testDisplaySurveyQuestions_SurveyNotFound() throws Exception {
//        Long invalidSurveyId = 999L; // assuming this ID doesn't exist
//
//        mockMvc.perform(get("/survey/{surveyId}/respond", invalidSurveyId))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    public void testSubmitAnswers_Success() throws Exception {
//        String surveyName = "Product Feedback Survey";
//        MvcResult surveyResult = mockMvc.perform(post("/survey")
//                        .param("name", surveyName))
//                .andExpect(status().is3xxRedirection())
//                .andReturn();
//
//        Long surveyId = extractSurveyId(surveyResult.getResponse().getRedirectedUrl());
//
//        mockMvc.perform(post("/survey/{surveyId}/question/create", surveyId)
//                        .param("surveyQuestion", "What do you think of our new product?")
//                        .param("choices", "OPEN_ENDED"))
//                .andExpect(status().is3xxRedirection());
//
//        mockMvc.perform(post("/survey/{surveyId}/question/create", surveyId)
//                        .param("surveyQuestion", "Rate our product from 1 to 5")
//                        .param("choices", "NUMERIC")
//                        .param("minValue", "1")
//                        .param("maxValue", "5"))
//                .andExpect(status().is3xxRedirection());
//
//        mockMvc.perform(post("/survey/{surveyId}/respond", surveyId)
//                        .param("answers[1].response", "It's great!")
//                        .param("answers[2].response", "5"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/survey/getbyid/" + surveyId));
//
//    }
//
//    @Test
//    public void testSubmitAnswers_InvalidData() throws Exception {
//        String surveyName = "Employee Feedback Survey";
//        MvcResult surveyResult = mockMvc.perform(post("/survey")
//                        .param("name", surveyName))
//                .andExpect(status().is3xxRedirection())
//                .andReturn();
//
//        Long surveyId = extractSurveyId(surveyResult.getResponse().getRedirectedUrl());
//
//        mockMvc.perform(post("/survey/{surveyId}/question/create", surveyId)
//                        .param("surveyQuestion", "Rate your job satisfaction from 1 to 10")
//                        .param("choices", "NUMERIC")
//                        .param("minValue", "1")
//                        .param("maxValue", "10"))
//                .andExpect(status().is3xxRedirection());
//
//        mockMvc.perform(post("/survey/{surveyId}/respond", surveyId)
//                        .param("answers[1].response", "invalid"))
//                .andExpect(status().isInternalServerError());
//    }

    private Long extractSurveyId(String redirectUrl) {
        Pattern pattern = Pattern.compile("/survey/(\\d+)/question/create");
        Matcher matcher = pattern.matcher(redirectUrl);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Cannot extract survey ID from redirect URL");
        }
    }
}
