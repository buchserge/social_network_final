package com.example.social_network.controller;

import com.example.social_network.domain.entity.Message;
import com.example.social_network.domain.entity.User;
import com.example.social_network.service.MessageServiceImpl;
import com.example.social_network.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Controller
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private final MessageServiceImpl messageServiceImpl;
    @Autowired
    private final UserServiceImpl userServiceImpl;

    public LikeController(MessageServiceImpl messageServiceImpl, UserServiceImpl userServiceImpl) {
        this.messageServiceImpl = messageServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping("/like/{likedMessageId}")
    public String liked(@AuthenticationPrincipal User user, @PathVariable Long likedMessageId, @RequestHeader(required = false) String referer,
                        RedirectAttributes attributes, Model model) {

        Message messageID = messageServiceImpl.getMessageID(likedMessageId);
        User currentUser = userServiceImpl.getUserByName(user.getName());
        Set<User> usersLiked = messageID.getUsersLiked();

        if (usersLiked.contains(currentUser)) {
            usersLiked.remove(currentUser);
            messageID.setLikeCount(messageID.getLikeCount() - 1l);

        } else {
            usersLiked.add(currentUser);
            messageID.setLikeCount(messageID.getLikeCount() + 1l);
        }
        messageServiceImpl.saveMessage(messageID);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        if (ObjectUtils.isEmpty(components.getQueryParams().getFirst("size"))) {
            return "redirect:" + components.getPath() + "?page=0&size=5";
        } else {
            return "redirect:" + components.getPath() + "?size=" + components.getQueryParams().getFirst("size")
                    + "&page=" + components.getQueryParams().getFirst("page");
        }
    }
}
