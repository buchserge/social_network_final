package com.example.social_network.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String senderName;
    private String recipientName;
    private Long total;
    public String toString() {
        return String.format("messageNotification [id=%d,total=%d,senderName=%s,recipientName=%s]",
                this.id, this.total, this.senderName, this.recipientName);
    }
}
