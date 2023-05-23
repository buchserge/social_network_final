package com.example.social_network.controller;

import com.example.social_network.domain.entity.Comment;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.NoSuchFileException;

@Controller
@RequestMapping("api/message")
public class MessageController {

    @Autowired
    private final MessageServiceImpl messageServiceImpl;
    @Autowired
    private final UserServiceImpl userServiceImpl;

    public MessageController(MessageServiceImpl messageServiceImpl, UserServiceImpl userServiceImpl) {
        this.messageServiceImpl = messageServiceImpl;
        this.userServiceImpl = userServiceImpl;

    }

    @GetMapping("/showMessages")
    public String displayMessages(@AuthenticationPrincipal User user, Model model,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size) throws NoSuchFileException {

        User currentUser = userServiceImpl.findById(user.getId());

        Page<Message> pageMessages = messageServiceImpl.getAllMessagesByUserId(currentUser.getId(), PageRequest.of(page, size, Sort.by("id").descending()));

        Comment comment = new Comment();
        comment.setAuthor(user);

        model.addAttribute("followersCount", (long) currentUser.getFriends().size());
        model.addAttribute("paginationCase", "myMessageController");
        model.addAttribute("comment", comment);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageMessages.getTotalPages());
        model.addAttribute("totalItems", pageMessages.getTotalElements());
        model.addAttribute("messages", pageMessages);
        model.addAttribute("size", size);
        model.addAttribute("userById", currentUser);
        model.addAttribute("currentUser", currentUser);

        return "userMessages";

    }


    @GetMapping("/showMessages/{userId}")
    public String showMessagesById(@AuthenticationPrincipal User user, @PathVariable Long userId, @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "5") int size, Model model) {

        Page<Message> allMessagesByUserId = messageServiceImpl.getAllMessagesByUserId(userId, PageRequest.of(page, size, Sort.by("id").descending()));
        User userById = userServiceImpl.findById(userId);
        User currentUser = userServiceImpl.getUserByName(user.getName());

        if (currentUser.getFriends().contains(userById)) {
            model.addAttribute("followed", true);
        } else {
            model.addAttribute("followed", false);
        }
        model.addAttribute("paginationCase", "myMessageController2");
        model.addAttribute("messages", allMessagesByUserId);
        model.addAttribute("totalPages", allMessagesByUserId.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("followersCount", (long) userById.getFriends().size());
        model.addAttribute("userById", userById);
        model.addAttribute("currentUser", currentUser);

        return "userMessages";
    }

    @PostMapping("/writeMessage")
    public String writeMessage(@RequestParam("abc") String text, @AuthenticationPrincipal User user, @RequestHeader(required = false) String referer) {
        messageServiceImpl.createMessage(text, user.getName());
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        return "redirect:" + components.getPath();
    }

    @GetMapping("/deleteMessage/{messageId}")
    public String liked(@PathVariable Long messageId, @RequestHeader(required = false) String referer) {
        messageServiceImpl.deleteById(messageId);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        return "redirect:" + components.getPath();
    }


}
