package com.example.social_network.service;

import com.example.social_network.domain.VerificationToken;
import com.example.social_network.domain.entity.Role;
import com.example.social_network.domain.entity.User;
import com.example.social_network.repo.UserRepo;
import com.example.social_network.repo.VerificationTokenRepository;
import com.example.social_network.service.interfaces.ConfirmRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Service;

@Service
public class ConfirmRegServiceImpl implements ConfirmRegService {
    @Autowired
    private final VerificationTokenRepository verifRepo;
    @Autowired
    private final UserRepo userRepo;

    public ConfirmRegServiceImpl(VerificationTokenRepository verifRepo, UserRepo userRepo, DaoAuthenticationProvider provider) {
        this.verifRepo = verifRepo;
        this.userRepo = userRepo;

    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken();
        myToken.setToken(token);
        myToken.setUser(user);
        verifRepo.save(myToken);
    }

    @Override
    public User registerNewUserAccount(User user) {
        user.setOnline(false);
        user.getRoles().add(Role.USER);
        return userRepo.save(user);
    }



}
