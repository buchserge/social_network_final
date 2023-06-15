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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/user_before.sql", "/image_info_before.sql", "/message_before.sql", "/comment_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/image_info_after.sql", "/comment_after.sql", "/message_after.sql", "/user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommentController commentController;

    @Test
    @WithUserDetails(value = "Katty")
    public void createCommentFormTest() throws Exception {

        mockMvc.perform(get("/api/comment/createCommentForm/{messageId}", "1"))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(view().name("listComments"))
                .andExpect(xpath("//*[@id='testComments']").nodeCount(2))
                .andExpect(xpath("//*[@id='testComments']/div/div[2]/li").string("second"));

    }


}