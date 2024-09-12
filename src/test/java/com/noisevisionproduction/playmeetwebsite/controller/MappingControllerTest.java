package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.configuration.SecurityConfiguration;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MappingController.class)
@Import(SecurityConfiguration.class)
class MappingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiController apiController;

    @MockBean
    private CookieService cookieService;

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

    @Test
    public void testLoginMapping() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login_page"));
    }

    @Test
    public void testPostCreating() throws Exception {
        when(cookieService.getLoginStatusCookie(any(HttpServletRequest.class))).thenReturn("user");
        mockMvc.perform(get("/create-post"))
                .andExpect(status().isOk())
                .andExpect(view().name("post_creating"));
    }
}