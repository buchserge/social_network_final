package com.example.social_network.repo;

import com.example.social_network.domain.MessageChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageChatRepo extends JpaRepository<MessageChat, Long> {

    @Query("SELECT COUNT (m)  FROM MessageChat m WHERE m.senderName = :senderName AND m.recipientName = :recipientName AND m.isRead = '1'")
    Long countByRecipientNameAndSenderName(@Param("senderName") String senderName, @Param("recipientName") String recipientName);

    long countAllByRecipientNameAndIsRead(String recipientName,boolean isRead);

    List<MessageChat> findAllByRecipientNameAndSenderName(String recipientName, String senderName);

    List<MessageChat> findAllByRecipientName(String recipientName);
}
