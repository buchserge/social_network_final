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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@ExtendWith(SpringExtension.class)
@WithUserDetails("Katty")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/user_before.sql", "/image_info_before.sql", "/message_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/image_info_after.sql", "/message_after.sql", "/user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MessageControllerTest {

    @Autowired
    private MessageController messageController;
    @Autowired
    private MockMvc mockMvc;

    @Test

    void displayMessages() throws Exception {
        mockMvc.perform(get("/api/message/showMessages"))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(view().name("userMessages")).andExpect(model().attributeExists("messages"))
                .andExpect(xpath("//*[@id='messageTest']").nodeCount(2));
    }

    @Test
    void showMessagesById() throws Exception {
        mockMvc.perform(get("/api/message/showMessages/{userId}",1))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(view().name("userMessages")).andExpect(model().attributeExists("messages"))
                .andExpect(xpath("//*[@id='messageTest']").nodeCount(2));
    }

    @Test
    void writeMessage() throws Exception {
        mockMvc.perform(post("/api/message/writeMessage").param("abc","some")
                        .header("Referer","http://localhost:8080/api/message/showMessages"))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());
    }
}