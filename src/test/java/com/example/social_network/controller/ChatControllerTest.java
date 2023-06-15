package com.example.social_network.controller;

import com.example.social_network.config.UsersOnline;
import com.example.social_network.domain.MessageChat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/user_before.sql", "/image_info_before.sql", "/message_chat_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/image_info_after.sql", "/user_after.sql", "/message_chat_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ChatControllerTest {

    @Autowired
    private UsersOnline usersOnline;
    @Autowired
    private ObjectMapper objectMapper;

    private WebSocketStompClient webSocketStompClient;
    @Autowired
    private FriendListController friendListController;
    @Autowired
    private MockMvc mockMvc;
    private String URL;
    @LocalServerPort
    private Integer port;


    @BeforeEach
    void setup() throws ExecutionException, InterruptedException, TimeoutException {
        URL = "ws://localhost:" + port + "/ws";
        usersOnline.setUsersOnline("Katty");

    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    @Test
    @WithUserDetails("Katty")
    public void showChatTest() throws Exception {
        mockMvc.perform(get("/api/showChat"))
                .andExpect(status().isOk())
                .andExpect(authenticated());
    }


    @Test
    @WithUserDetails(value = "Katty")
    public void sendMessage() throws ExecutionException, InterruptedException, TimeoutException {
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        BlockingQueue<MessageChat> queue = new ArrayBlockingQueue<>(1);
        StompSession stompSession = webSocketStompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
        }).get(3, SECONDS);
        stompSession.subscribe("/topic/hello", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MessageChat.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                queue.add((MessageChat) payload);
            }
        });
        MessageChat msg = new MessageChat();
        msg.setText("test");
        msg.setSenderName("Katty");
        msg.setRecipientName("Paola");
        stompSession.send("/app/name", msg);
        await().
                atMost(3, SECONDS)
                .untilAsserted(() -> assertEquals(msg.getSenderName(), queue.poll().getSenderName()));


    }


}