package com.example.social_network.event;

import com.example.social_network.domain.entity.User;
import com.example.social_network.service.MessageChatServiceImpl;
import com.example.social_network.service.MessageNotificationServiceImpl;
import com.example.social_network.utils.MessageNotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DeleteReadMessagesEventPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private MessageChatServiceImpl messageChatServiceImpl;
    @Autowired
    private MessageNotificationServiceImpl messageNotificationService;
    @Autowired
    private MessageNotificationUtils messageNotificationUtils;

    public DeleteReadMessagesEventPublisher(ApplicationEventPublisher publisher, MessageChatServiceImpl messageChatServiceImpl, MessageNotificationServiceImpl messageNotificationService, MessageNotificationUtils messageNotificationUtils) {
        this.publisher = publisher;
        this.messageChatServiceImpl = messageChatServiceImpl;
        this.messageNotificationService = messageNotificationService;
        this.messageNotificationUtils = messageNotificationUtils;
    }

    public void publishEvent(User currentUser, User recipient) throws InterruptedException {
        publisher.publishEvent(new DeleteReadMessagesEvent(currentUser,recipient));

    }
}
