package com.example.social_network.service.interfaces;

import com.example.social_network.domain.dto.UserDTO;
import com.example.social_network.domain.entity.User;

import java.util.List;

public interface UserService {
    User getUserByName(String name);

    void updateUserCredentials(Long id, UserDTO userDTO);

    void saveNew(User user);

    User findById(Long id);

    List<User> findAllUsersByName(String tagName);

}
