package com.example.social_network.service;

import com.example.social_network.domain.VerificationToken;
import com.example.social_network.domain.entity.Role;
import com.example.social_network.domain.entity.User;
import com.example.social_network.repo.UserRepo;
import com.example.social_network.repo.VerificationTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ConfirmRegServiceImplTest {
    private User user;
    @Autowired
    ConfirmRegServiceImpl regService;
    @MockBean
    private VerificationTokenRepository verifRepo;
    @MockBean
    private UserRepo userRepo;

    @BeforeEach
    void setup() {
        User userTest = new User();
        userTest.setName("TestName");
        userTest.setId(1L);
        user = userTest;
    }

    @Test
    void createVerificationToken() {
        regService.createVerificationToken(user, Mockito.anyString());
        Mockito.verify(verifRepo).save(any(VerificationToken.class));
    }

    @Test
    void registerNewUserAccount() {
        regService.registerNewUserAccount(user);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(false, user.getOnline());
        Assertions.assertEquals(Role.USER, user.getRoles().stream().findAny().get());
    }
}