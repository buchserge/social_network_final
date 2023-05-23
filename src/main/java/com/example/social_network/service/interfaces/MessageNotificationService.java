package com.example.social_network.service.interfaces;

import com.example.social_network.domain.MessageNotification;

import java.util.List;

public interface MessageNotificationService {
    List<MessageNotification> findAll();

    void saveNotific(MessageNotification messageNotification);

    List<MessageNotification> findBySenderNameAndRecipientName(String senderName, String recipientName);


}
