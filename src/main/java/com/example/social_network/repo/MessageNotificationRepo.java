package com.example.social_network.repo;

import com.example.social_network.domain.MessageNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageNotificationRepo extends JpaRepository<MessageNotification, Long> {

    List<MessageNotification> findBySenderNameAndRecipientName(String senderName, String recipientName);



}
