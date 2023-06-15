package com.example.social_network.event;


import com.example.social_network.domain.MessageChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class StompEventListener {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public StompEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener(StompEvent.class)
    public void handleStompEvent(StompEvent stompEvent) {
        MessageChat messageChat = stompEvent.getMessageChat();
        System.out.println("triggered"+messageChat.getRecipientName());
        messagingTemplate.convertAndSendToUser(messageChat.getRecipientName(), "/queue/hello", messageChat);
        messagingTemplate.convertAndSendToUser(messageChat.getSenderName(), "/queue/hello", messageChat);
    }


}

