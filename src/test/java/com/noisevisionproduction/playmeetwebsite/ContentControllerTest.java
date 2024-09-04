package com.noisevisionproduction.playmeetwebsite;

import com.noisevisionproduction.playmeetwebsite.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ContentController.class)
@Import(SecurityConfig.class)
class ContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPrivatePolicy() throws Exception {
        mockMvc.perform(get("/privacy_policy"))
                .andExpect(status().isOk())
                .andExpect(view().name("privacy_policy"));
    }

    @Test
    public void testDeleteAccountForm() throws Exception {
        mockMvc.perform(get("/faq"))
                .andExpect(status().isOk())
                .andExpect(view().name("faq"));
    }

    @Test
    public void testLandingPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("landing_page"));
    }

    @Test
    public void testAccessDenied() throws Exception {
        mockMvc.perform(get("/403"))
                .andExpect(status().isOk())
                .andExpect(view().name("403"));
    }
}