package com.example.social_network.service;

import com.example.social_network.domain.MessageChat;
import com.example.social_network.repo.MessageChatRepo;
import com.example.social_network.service.interfaces.MessageChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageChatServiceImpl implements MessageChatService {
    @Autowired
    private final MessageChatRepo messageChatRepo;

    public MessageChatServiceImpl(MessageChatRepo messageChatRepo) {
        this.messageChatRepo = messageChatRepo;
    }

    @Override
    public void saveChatMessage(MessageChat messageChat) {
        messageChatRepo.save(messageChat);
    }

    @Override
    public long count(String senderName, String recipientName) {
        return messageChatRepo.countByRecipientNameAndSenderName(senderName, recipientName);
    }

    @Override
    public List<MessageChat> getChatMessagesByReceiptNameAndBySenderName(String recipientName, String senderName) {
        List<MessageChat> allByRecipientNameAndSenderName = messageChatRepo.findAllByRecipientNameAndSenderName(recipientName, senderName);
        allByRecipientNameAndSenderName.addAll(messageChatRepo.findAllByRecipientNameAndSenderName(senderName, recipientName));
        return allByRecipientNameAndSenderName;
    }


    @Override
    public void deleteCollection(List<MessageChat> collection) throws InterruptedException {
        messageChatRepo.deleteAllInBatch(collection);
    }

    public void saveCollection(Iterable collection) {
        messageChatRepo.saveAll(collection);
    }

    @Override
    public List<MessageChat> findAllByRecipientName(String recipientName) {
        return messageChatRepo.findAllByRecipientName(recipientName);
    }

    @Override
    public long countAllByRecipientNameAndIsRead(String recipientName,boolean isRead) {
        return messageChatRepo.countAllByRecipientNameAndIsRead(recipientName,isRead);
    }
}
