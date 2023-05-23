package com.example.social_network.service.interfaces;

import com.example.social_network.domain.entity.User;


public interface ConfirmRegService {
    void createVerificationToken(User user, String token);

    User registerNewUserAccount(User user);

}
