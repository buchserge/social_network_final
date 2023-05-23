package com.example.social_network.controller;

import com.example.social_network.domain.entity.User;
import com.example.social_network.exception.UsersNotFoundException;
import com.example.social_network.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api")
public class SearchController {
    @Autowired
    private final UserServiceImpl userServiceImpl;

    public SearchController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/search")
    public String findUsers(@RequestParam("tagName") String tagName, Model model) {

        List<User> users = userServiceImpl.findAllUsersByName(tagName);
        if (users.isEmpty()) {
            throw new UsersNotFoundException("NO USERS FOUND");
        }
        model.addAttribute("searchUsers", users);
        return "usersSearch";
    }

}
