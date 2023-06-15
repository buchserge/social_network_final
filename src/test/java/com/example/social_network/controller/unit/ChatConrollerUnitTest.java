package com.example.social_network.controller.unit;

import com.example.social_network.SocialNetworkApplication;
import com.example.social_network.config.UsersOnline;
import com.example.social_network.controller.ChatController;
import com.example.social_network.domain.MessageChat;
import com.example.social_network.domain.entity.User;
import com.example.social_network.event.*;
import com.example.social_network.security.EncryptionConfig;
import com.example.social_network.security.SecurityConfig;
import com.example.social_network.service.*;
import com.example.social_network.utils.MessageNotificationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



@WebMvcTest(ChatController.class)
@Import(SpringSecurityTestConfig.class)
public class ChatConrollerUnitTest {
    @Autowired
    private ChatController chatController;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl userServiceImpl;
    @MockBean
    private MessageChatServiceImpl messageChatServiceImpl;
    @MockBean
    private UsersOnline usersOnline;
    @MockBean
    private MessageNotificationUtils messageNotificationUtils;
    @MockBean
    private MessageNotificationServiceImpl messageNotificationService;
    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;
    @MockBean
    private StompEventPublisher stompEventPublisher;
    @MockBean
    private StompEventListener stompEventListener;
    @MockBean
    private RegistrationEventPublisher registrationEventPublisher;
    @MockBean
    private RegistrationListener listener;
    @MockBean
    private DeleteReadMessagesEventPublisher deleteReadMessagesEventPublisher;
    @MockBean
    private DeleteReadMessagesEventListener deleteReadMessagesEventListener;



    @Test
    @WithUserDetails(value = "Katty")
    public void getUnreadMessages() throws Exception {
        User user = new User();
        user.setName("Katty");
        user.setId(1L);
        user.setPassword("Katty");
        User user2 = new User();
        user2.setName("Paola");
        user2.setId(2L);
        user2.setPassword("Paola");

        MessageChat messageChat = new MessageChat();
        messageChat.setText("someText");
        messageChat.setId(1L);
        messageChat.setRecipientName("Paola");
        ArrayList<MessageChat> list = new ArrayList<>();
        list.add(messageChat);



        Mockito.doReturn(user2).when(userServiceImpl).findById(2L);
        Mockito.doReturn(user).when(userServiceImpl).getUserByName("Katty");
        Mockito.doReturn(list).when(messageChatServiceImpl).getChatMessagesByReceiptNameAndBySenderName("Katty","Paola");

        mockMvc.perform(get("/api/getUnreadMessages").param("id", "2"));
        Mockito.verify(messageChatServiceImpl,Mockito.times(1)).getChatMessagesByReceiptNameAndBySenderName("Katty","Paola");
        Mockito.verify(deleteReadMessagesEventPublisher,Mockito.times(1)).publishEvent(user,user2);
    }
}
