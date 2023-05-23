package com.example.social_network.event;

import com.example.social_network.domain.entity.User;
import com.example.social_network.email.EmailDetails;
import com.example.social_network.email.EmailServiceImpl;
import com.example.social_network.service.ConfirmRegServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationEvent> {

    @Autowired
    private final EmailServiceImpl emailService;
    @Autowired
    private final ConfirmRegServiceImpl confirmRegServiceImpl;

    public RegistrationListener(EmailServiceImpl emailService, ConfirmRegServiceImpl confirmRegServiceImpl) {
        this.emailService = emailService;

        this.confirmRegServiceImpl = confirmRegServiceImpl;
    }

    @Override
    public void onApplicationEvent(OnRegistrationEvent event) {

    }

    @EventListener
    public void handleOnRegistrationEvent(OnRegistrationEvent event) {

        User eventUser = event.getUser();
        String token = UUID.randomUUID().toString();
        confirmRegServiceImpl.createVerificationToken(eventUser, token);

        String recipientAddress = eventUser.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/regitrationConfirm?token=" + token;
        EmailDetails email = new EmailDetails();
        email.setRecipient(recipientAddress);
        email.setSubject(subject);
        email.setMsgBody("message" + "\r\n" + "http://localhost:8080" + confirmationUrl);
        emailService.sendSimpleMail(email);
    }
}
