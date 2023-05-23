package com.example.social_network.controller;

import com.example.social_network.domain.entity.User;
import com.example.social_network.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/api")
public class FollowController {
    @Autowired
    private final UserServiceImpl userServiceImpl;

    public FollowController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/follow/{id}")
    public String follow(@PathVariable Long id, @AuthenticationPrincipal User user, @RequestHeader(required = false) String referer, RedirectAttributes attributes) {

        User byId = userServiceImpl.findById(id);
        User userByName = userServiceImpl.getUserByName(user.getName());

        if (userByName.getFriends().contains(byId)) {

            userByName.getFriends().remove(byId);
            userServiceImpl.saveNew(userByName);


            return "redirect:/api/message/showMessages/" + id;
        } else {

            userByName.addFriend(byId);
            userServiceImpl.saveNew(userByName);

            UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
            return "redirect:" + components.getPath();
        }


    }
}
