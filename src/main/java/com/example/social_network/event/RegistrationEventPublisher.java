package com.example.social_network.event;


import com.example.social_network.domain.entity.User;
import lombok.Data;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Data
public class RegistrationEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishEvent(String appUrl, User user) {

        publisher.publishEvent(new OnRegistrationEvent(user, appUrl, user));
    }
}
