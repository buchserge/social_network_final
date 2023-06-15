package com.example.social_network.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/user_before.sql", "/message_before.sql", "/image_info_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/user_after.sql", "/message_after.sql", "/image_info_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LikeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "Katty")
   public void liked() throws Exception {
        mockMvc.perform(get("/api/like/1").header("Referer","http://localhost:8080/api/friendsMessages"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/api/friendsMessages?page=0&size=5"));

    }
    @Test
    @WithUserDetails(value = "Katty")
    public  void likeExists() throws Exception {
        mockMvc.perform(get("/api/friendsMessages?page=0&size=5"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='testMessages']/div/div/div/div[2]/div[2]/div[1]/div/span")
                        .string("1"));
    }

}