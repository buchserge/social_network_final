package com.example.social_network.service;

import com.example.social_network.domain.entity.Message;
import com.example.social_network.domain.entity.User;
import com.example.social_network.repo.MessageRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MessageServiceImplTest {
    private User user;
    @Autowired
    private MessageServiceImpl messageService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private  MessageRepo messageRepo;
    @BeforeEach
    public void setup() {
        User createUser = new User();
        createUser.setName("Katty");
        user=createUser;
    }



    @Test
    public void createMessageTest() {

        Mockito.doReturn(user)
                .when(userService)
                .getUserByName("Katty");
        Message message = new Message();
        message.setUsr(user);
        message.setText("someText");
        messageService.createMessage(anyString(), "Katty");

Mockito.verify(messageRepo).save(any(Message.class));
    }
}