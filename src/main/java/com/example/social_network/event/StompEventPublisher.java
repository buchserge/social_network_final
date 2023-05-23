package com.example.social_network.event;

import com.example.social_network.domain.MessageChat;
import com.example.social_network.service.UserServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

@Component
@Data
public class StompEventPublisher {

    private final ApplicationEventPublisher publisher;
    @Autowired
    private final UserServiceImpl userServiceImpl;
    @Autowired
    private final SimpUserRegistry simpUserRegistry;
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    public void publishEvent(MessageChat message) {
        StompEvent stompEvent = new StompEvent(message);
        publisher.publishEvent(stompEvent);


    }
}
