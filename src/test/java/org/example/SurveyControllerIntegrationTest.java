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
public class SurveyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testDisplaySurveyForm() throws Exception {
        mockMvc.perform(get("/survey"))
                .andExpect(status().isOk())
                .andExpect(view().name("createsurvey"))
                .andExpect(model().attributeExists("survey"));
    }

    @Test
    public void testCreateSurvey_Success() throws Exception {
        mockMvc.perform(post("/survey")
                        .param("name", "Customer Satisfaction Survey"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/survey/*/question/create"));
    }

    @Test
    public void testGetSurveyById_Success() throws Exception {
        MvcResult result = mockMvc.perform(post("/survey")
                        .param("name", "Employee Engagement Survey"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        Long surveyId = extractSurveyId(result.getResponse().getRedirectedUrl());

        mockMvc.perform(get("/survey/getbyid/{id}", surveyId))
                .andExpect(status().isOk())
                .andExpect(view().name("displaysurvey"))
                .andExpect(model().attributeExists("survey"))
                .andExpect(model().attribute("survey", hasProperty("id", is(surveyId))));
    }

    @Test
    public void testGetSurveyByName_Success() throws Exception {
        String surveyName = "Market Research Survey";

        mockMvc.perform(post("/survey")
                        .param("name", surveyName))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/survey/getbyname/{name}", surveyName))
                .andExpect(status().isOk())
                .andExpect(view().name("displaysurvey"))
                .andExpect(model().attributeExists("survey"))
                .andExpect(model().attribute("survey", hasProperty("name", is(surveyName))));
    }


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
