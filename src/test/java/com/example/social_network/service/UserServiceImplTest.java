package com.example.social_network.service;

import com.example.social_network.domain.dto.UserDTO;
import com.example.social_network.domain.entity.User;
import com.example.social_network.repo.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {


    @Autowired
    private UserServiceImpl userService;
    @MockBean
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Test
    void updateUserCredentials() {
        User createdUser = new User();
        createdUser.setName("Katty");
        createdUser.setPassword("Katty");
        UserDTO createdUserDTO = new UserDTO();
        createdUserDTO.setName("Kay");
        createdUserDTO.setPassword("Kay");

        Mockito.doReturn(Optional.of(createdUser)).when(userRepo).findById(1L);
        userService.updateUserCredentials(1L, createdUserDTO);
        Assertions.assertEquals("Kay",createdUser.getName());
    }

    @Test
    public void updateUserCredentialsException() {
        User createdUser = new User();
        createdUser.setName("Katty");
        createdUser.setPassword("Katty");

        UserDTO createdUserDTO = new UserDTO();
        createdUserDTO.setName("Katty");
        createdUserDTO.setPassword("Katty");

        Mockito.doReturn(Optional.of(createdUser)).when(userRepo).findById(10L);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userService.updateUserCredentials(10L, createdUserDTO));


    }

}