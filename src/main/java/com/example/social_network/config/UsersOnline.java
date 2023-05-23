package com.example.social_network.config;

import com.example.social_network.domain.entity.User;
import com.example.social_network.service.UserServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Setter
@Getter
public class UsersOnline {
    private List<String> usersOnline = new ArrayList<>();
    @Autowired
    private final UserServiceImpl userServiceImpl;

    public UsersOnline(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    public void setUsersOffline(String currentUserName) {
        if (usersOnline.contains(currentUserName)) {
            User currentUser = userServiceImpl.getUserByName(currentUserName);
            currentUser.setOnline(false);
            userServiceImpl.saveNew(currentUser);
            usersOnline.remove(currentUserName);
        }
    }

    public void setUsersOnline(String currentUserName) {

        if (!usersOnline.contains(currentUserName)) {
            User currentUser = userServiceImpl.getUserByName(currentUserName);
            currentUser.setOnline(true);
            userServiceImpl.saveNew(currentUser);
            usersOnline.add(currentUserName);
        }
    }
}
