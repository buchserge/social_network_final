package com.example.social_network.controller;

import com.example.social_network.domain.entity.Message;
import com.example.social_network.domain.entity.User;
import com.example.social_network.service.MessageServiceImpl;
import com.example.social_network.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


@Controller
@RequestMapping("/api")
public class MainFeedController {

    @Autowired
    private final MessageServiceImpl messageServiceImpl;
    @Autowired
    private final UserServiceImpl userServiceImpl;


    public MainFeedController(MessageServiceImpl messageServiceImpl, UserServiceImpl userServiceImpl) {
        this.messageServiceImpl = messageServiceImpl;
        this.userServiceImpl = userServiceImpl;

    }

    @GetMapping("/friendsMessages")
    public String message(@AuthenticationPrincipal User user,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "5") int size,
                          Model model) throws IOException {

        Page<Message> pageMessages = messageServiceImpl.getAllMessages(PageRequest.of(page, size, Sort.by("id").descending()));
        User currentUser = userServiceImpl.getUserByName(user.getName());
        long count = currentUser.getFriends().size();

        model.addAttribute("friendsMessages", pageMessages.getContent());
        model.addAttribute("pageMessages", pageMessages);
        model.addAttribute("totalPages", pageMessages.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("followersCount", count);
        model.addAttribute("paginationCase", "messageController");
        model.addAttribute("currentUser", currentUser);
        return "allMessages";
    }




}