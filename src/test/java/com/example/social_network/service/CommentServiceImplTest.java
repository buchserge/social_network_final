package com.example.social_network.service;

import com.example.social_network.domain.entity.Comment;
import com.example.social_network.domain.entity.Message;
import com.example.social_network.domain.entity.User;
import com.example.social_network.repo.CommentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommentServiceImplTest {
    private User user;
    @Autowired
    private CommentServiceImpl commentService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private MessageServiceImpl messageService;
    @MockBean
    private CommentRepo commentRepo;

    @BeforeEach
    public void setup() {
        User createUser = new User();
        createUser.setName("Katty");
        user = createUser;
    }

    @Test
    void createComment() {
        Message message = new Message();
        message.setId(1L);
        message.setText("some text");
        Mockito.doReturn(user).when(userService).getUserByName("Katty");
        Mockito.doReturn(message).when(messageService).getMessageID(1L);
        commentService.createComment("katty", "some", 1L);
        Mockito.verify(commentRepo,Mockito.times(1)).save(ArgumentMatchers.any(Comment.class));
    }
}