//package org.example;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class HomePageControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void testDisplayHomePage() throws Exception {
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("homepage"))
//                .andExpect(model().attributeExists("surveyName"))
//                .andExpect(model().attribute("surveyName", ""));
//    }
//
////    WIP: Did not pass Maven build upon pull request
////    @Test
////    public void testSearchSurvey_Success() throws Exception {
////        String surveyName = "Customer Satisfaction Survey";
////        mockMvc.perform(post("/survey")
////                        .param("name", surveyName))
////                .andExpect(status().is3xxRedirection());
////
////        mockMvc.perform(post("/")
////                        .param("surveyName", surveyName))
////                .andExpect(status().is3xxRedirection())
////                .andExpect(redirectedUrl("/survey/getbyname/" + surveyName));
////    }
//
//    /**
//     * WIP: RuntimeException needs to be handled but is currently not
//     */
////    @Test
////    public void testSearchSurvey_NotFound() throws Exception {
////        String surveyName = "Nonexistent Survey";
////
////        mockMvc.perform(post("/")
////                        .param("surveyName", surveyName))
////                .andExpect(status().isOk())
////                .andExpect(view().name("error"))
////                .andExpect(model().attribute("errorMessage", "Survey not found"));
////    }
//}
