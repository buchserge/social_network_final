package com.example.social_network.service;

import com.example.social_network.domain.MessageNotification;
import com.example.social_network.repo.MessageNotificationRepo;
import com.example.social_network.service.interfaces.MessageNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageNotificationServiceImpl implements MessageNotificationService {
    @Autowired
    private final MessageNotificationRepo repo;

    public MessageNotificationServiceImpl(MessageNotificationRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<MessageNotification> findAll() {
        return repo.findAll();
    }

    @Override
    public void saveNotific(MessageNotification messageNotification) {
        repo.save(messageNotification);
    }

    @Override
    public List<MessageNotification> findBySenderNameAndRecipientName(String senderName, String recipientName) {
        return repo.findBySenderNameAndRecipientName(senderName, recipientName);
    }


}
