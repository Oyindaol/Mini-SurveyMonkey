package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRegisterMenu() throws Exception {
        mockMvc.perform(get("/account/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("createaccount"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testLoginMenu_NoMessage() throws Exception {
        mockMvc.perform(get("/account/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginaccount"))
                .andExpect(model().attribute("message", ""))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testLoginMenu_WithMessage() throws Exception {
        String message = "SomeMessage";
        mockMvc.perform(get("/account/login").param("message", message))
                .andExpect(status().isOk())
                .andExpect(view().name("loginaccount"))
                .andExpect(model().attribute("message", message))
                .andExpect(model().attributeExists("user"));
    }
}
