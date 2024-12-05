package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
public class AnswerControllerIntegrationTest {

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

    private void createQuestion(Long surveyId, String questionText, String questionType, String... extraParams) throws Exception {
        var request = post("/survey/{surveyId}/question/create", surveyId)
                .param("surveyQuestion", questionText)
                .param("choices", questionType);

        if (questionType.equals("NUMERIC") && extraParams.length == 2) {
            request = request.param("minValue", extraParams[0])
                    .param("maxValue", extraParams[1]);
        }

        if (questionType.equals("MULTIPLE_CHOICE") && extraParams.length == 1) {
            request = request.param("multipleChoiceInput", extraParams[0]);
        }

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testDisplaySurveyQuestions_Success() throws Exception {
        String surveyName = "Customer Satisfaction Survey";
        Long surveyId = createSurveyAndGetId(surveyName);

        createQuestion(surveyId, "How satisfied are you with our service?", "OPEN_ENDED");

        mockMvc.perform(get("/survey/{surveyId}/respond", surveyId))
                .andExpect(status().isOk())
                .andExpect(view().name("answersurvey"))
                .andExpect(model().attributeExists("survey"))
                .andExpect(model().attribute("survey", hasProperty("id", is(surveyId))))
                .andExpect(model().attribute("survey", hasProperty("questions", hasSize(1))));
    }
}
