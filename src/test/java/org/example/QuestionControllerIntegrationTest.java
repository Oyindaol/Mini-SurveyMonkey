package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuestionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testDisplayQuestionForm() throws Exception {
        String surveyName = "Product Feedback Survey";
        mockMvc.perform(post("/survey")
                        .param("name", surveyName))
                .andExpect(status().is3xxRedirection());

        Long surveyId = 1L;

        mockMvc.perform(get("/survey/{surveyId}/question/create", surveyId))
                .andExpect(status().isOk())
                .andExpect(view().name("createquestion"))
                .andExpect(model().attributeExists("surveyId"))
                .andExpect(model().attributeExists("question"));
    }

    @Test
    public void testCreateQuestion() throws Exception {
        String surveyName = "Service Quality Survey";
        mockMvc.perform(post("/survey")
                        .param("name", surveyName))
                .andExpect(status().is3xxRedirection());

        Long surveyId = 1L;

        // Create a question
        mockMvc.perform(post("/survey/{surveyId}/question/create", surveyId)
                        .param("surveyQuestion", "How would you rate our service?")
                        .param("choices", "OPEN_ENDED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/survey/getbyid/" + surveyId));
    }
}
