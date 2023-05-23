package com.example.social_network.domain;


import com.example.social_network.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class MessageChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    @NotEmpty(message = "Field can't be empty")
    private String text;
    private Long senderId;
    private Long receiptId;
    private Boolean isRead;
    private String chatroomId;
    private String senderName;
    private String recipientName;
    @CreationTimestamp
    @CreatedDate
    private Timestamp createdAt;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User chatUser;

    public void setRoomID(MessageChat chatMessage) {
        chatroomId = String.format("%s_%s", chatMessage.getSenderName(), chatMessage.getRecipientName());
    }

    public MessageChat(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return String.format("chatMessage [id=%d,senderId=%d,receiptId=%d,name=%s,senderName=%s,recipientName=%s]",
                this.id, this.senderId, this.receiptId, this.text, this.senderName, this.recipientName);
    }
public void setTimeStamp(){

       this.createdAt= Timestamp.from(Instant.now());
    System.out.println("INSTANT"+createdAt);

}

}