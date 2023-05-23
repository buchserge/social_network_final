package com.example.social_network.event;

import com.example.social_network.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteReadMessagesEvent {
    private User currentUser;
    private User recipient;

    public DeleteReadMessagesEvent(String name) {
        this.currentUser = currentUser;
        this.recipient = recipient;
    }


}
