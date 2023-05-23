package com.example.social_network.event;

import com.example.social_network.domain.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnRegistrationEvent extends ApplicationEvent {

    private String appUrl;
    private User user;

    public OnRegistrationEvent(Object source, String appUrl, User user) {
        super(source);
        this.appUrl = appUrl;
        this.user = user;
    }


}
