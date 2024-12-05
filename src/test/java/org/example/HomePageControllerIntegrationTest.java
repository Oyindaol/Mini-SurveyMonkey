package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class HomePageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testDisplayHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeExists("surveyName"))
                .andExpect(model().attribute("surveyName", ""));
    }

    @Test
    public void testSearchSurvey_Success() throws Exception {
        String surveyName = "Customer Satisfaction Survey";

        MvcResult result = mockMvc.perform(post("/survey")
                        .param("name", surveyName))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        Long surveyId = extractSurveyId(result.getResponse().getRedirectedUrl());

        mockMvc.perform(post("/")
                        .param("surveyName", surveyName))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/survey/" + surveyId + "/respond"));
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
