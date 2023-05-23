package com.example.social_network.advice;


import com.example.social_network.controller.*;
import com.example.social_network.domain.entity.User;
import com.example.social_network.service.MessageChatServiceImpl;
import com.example.social_network.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {MessageController.class, CommentController.class, ChatController.class,
        MainFeedController.class, FriendListController.class, SettingsController.class, SearchController.class})
public class UserControllerAdvice {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private MessageChatServiceImpl messageChatService;

    public UserControllerAdvice(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @ModelAttribute
    public void addUserAttribute(Model model) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userServiceImpl.getUserByName(name);
        long count = messageChatService.countAllByRecipientNameAndIsRead(name,false);
        model.addAttribute("userNotify", count);
        model.addAttribute("friendsUser", currentUser);
    }
}
