package com.example.social_network.service;

import com.example.social_network.domain.entity.Message;
import com.example.social_network.domain.entity.User;
import com.example.social_network.repo.MessageRepo;
import com.example.social_network.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private final MessageRepo messageRepo;
    @Autowired
    private final UserServiceImpl userServiceImpl;

    public MessageServiceImpl(MessageRepo messageRepo, UserServiceImpl userServiceImpl) {
        this.messageRepo = messageRepo;
        this.userServiceImpl = userServiceImpl;
    }


    @Override
    public void createMessage(String text, String userName) {
        User currentUser = userServiceImpl.getUserByName(userName);
        Message message = new Message();
        message.setUsr(currentUser);
        message.setText(text);
        messageRepo.save(message);
    }

    @Override
    public Page<Message> getAllMessages(Pageable pageable) {
        return messageRepo.findAll(pageable);
    }


    @Override
    public Page<Message> getAllMessagesByUserId(Long id, Pageable pageable) {
        return messageRepo.findAllByUsr_Id(id, pageable);
    }

    @Override
    public Message getMessageID(Long id) {
        return messageRepo.findById(id).orElseThrow();
    }

    @Override
    public void saveMessage(Message message) {
        messageRepo.save(message);
    }

    @Override
    public void deleteById(Long messageId) {
        messageRepo.deleteById(messageId);
    }
}
