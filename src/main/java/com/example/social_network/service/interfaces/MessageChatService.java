package com.example.social_network.service.interfaces;

import com.example.social_network.domain.MessageChat;

import java.util.List;

public interface MessageChatService {

    void saveChatMessage(MessageChat messageChat);

    long count(String senderName, String recipientName);

    List<MessageChat> getChatMessagesByReceiptNameAndBySenderName(String recipientName, String senderName);

    void deleteCollection(List<MessageChat> collection) throws InterruptedException;

    void saveCollection(Iterable collection);

    List<MessageChat> findAllByRecipientName(String recipientName);

    long countAllByRecipientNameAndIsRead(String recipientName,boolean isRead);
}
