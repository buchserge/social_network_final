package com.example.social_network.service.interfaces;

import com.example.social_network.domain.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    void createMessage(String text, String userName);

    Page<Message> getAllMessages(Pageable pageable);

    Page<Message> getAllMessagesByUserId(Long id, Pageable pageable);

    Message getMessageID(Long id);

    void saveMessage(Message message);

    void deleteById(Long messageId);
}
