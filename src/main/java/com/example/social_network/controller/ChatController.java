package com.example.social_network.controller;

import com.example.social_network.config.UsersOnline;
import com.example.social_network.domain.MessageChat;
import com.example.social_network.domain.MessageNotification;
import com.example.social_network.domain.entity.User;
import com.example.social_network.event.DeleteReadMessagesEventPublisher;
import com.example.social_network.event.StompEventPublisher;
import com.example.social_network.service.MessageChatServiceImpl;
import com.example.social_network.service.MessageNotificationServiceImpl;
import com.example.social_network.service.UserServiceImpl;
import com.example.social_network.utils.MessageNotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class ChatController {
    @Autowired
    private final MessageChatServiceImpl messageChatServiceImpl;

    @Autowired
    private final UsersOnline usersOnline;
    @Autowired
    private final MessageNotificationUtils messageNotificationUtils;
    @Autowired
    private final MessageNotificationServiceImpl messageNotificationServiceImpl;
    @Autowired
    private final UserServiceImpl userServiceImpl;
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private final StompEventPublisher stompEventPublisher;
    @Autowired
    private final DeleteReadMessagesEventPublisher deleteReadMessagesEventPublisher;

    public ChatController(MessageChatServiceImpl messageChatServiceImpl, UsersOnline usersOnline, MessageNotificationUtils messageNotificationUtils,
                          MessageNotificationServiceImpl messageNotificationServiceImpl, UserServiceImpl userServiceImpl,
                          SimpMessagingTemplate messagingTemplate, StompEventPublisher stompEventPublisher,
                          DeleteReadMessagesEventPublisher deleteReadMessagesEventPublisher) {
        this.messageChatServiceImpl = messageChatServiceImpl;

        this.usersOnline = usersOnline;
        this.messageNotificationUtils = messageNotificationUtils;
        this.messageNotificationServiceImpl = messageNotificationServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.messagingTemplate = messagingTemplate;
        this.stompEventPublisher = stompEventPublisher;
        this.deleteReadMessagesEventPublisher = deleteReadMessagesEventPublisher;
    }


    @GetMapping("/showChat/{id}")
    public String showChatUser(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) throws CloneNotSupportedException {

        User userByName = userServiceImpl.getUserByName(user.getName());
        User recipient = userServiceImpl.findById(id);

        Set<User> userFriends = userByName.getFriends();
        sortSetByOnline(userFriends);

        List<MessageChat> chatMessagesByRecipientName = messageChatServiceImpl.findAllByRecipientName(user.getUsername());
        List<User> notFriends = chatMessagesByRecipientName.stream().map(m -> userServiceImpl.getUserByName(m.getSenderName())).filter(Objects::nonNull).distinct().toList();

        List<MessageNotification> messageNotificationList = messageNotificationServiceImpl.findAll();

        model.addAttribute("truee", true);
        model.addAttribute("notFriends", notFriends);
        model.addAttribute("notificationCountList", messageNotificationList);
        model.addAttribute("usrName", userByName.getName());
        model.addAttribute("usr", userByName);
        model.addAttribute("recipient", recipient);
        model.addAttribute("users", userFriends);
        model.addAttribute("recipientName", recipient.getName());
        model.addAttribute("recipientId", id);
        model.addAttribute("senderId", userByName.getId());
        model.addAttribute("showSend", true);
        model.addAttribute("friends", userByName.getFriends());
        return "chat";
    }

    private void sortSetByOnline(Set<User> collection) {
        collection.stream().sorted((u1, u2) -> {
            if (u1.getOnline()) {
                if (u2.getOnline()) {
                    return 0;
                }
                return -1;
            } else if (u2.getOnline())
                return 1;
            return u1.getOnline().compareTo(u2.getOnline());
        });
    }


    @GetMapping("/showChat")
    public String showChat(@AuthenticationPrincipal User user, Model model) {

        User userByName = userServiceImpl.getUserByName(user.getName());

        Set<User> userFriends = userByName.getFriends();
        sortSetByOnline(userFriends);

        List<MessageChat> chatMessagesByRecipientName = messageChatServiceImpl.findAllByRecipientName(user.getUsername());
        List<User> notFriends = chatMessagesByRecipientName.stream().map(m -> userServiceImpl.getUserByName(m.getSenderName())).filter(Objects::nonNull).distinct().toList();

        List<MessageNotification> messageNotificationList = messageNotificationServiceImpl.findAll();

        model.addAttribute("truee", false);
        model.addAttribute("notFriends", notFriends);
        model.addAttribute("notificationCountList", messageNotificationList);
        model.addAttribute("users", userFriends);
        model.addAttribute("usr", userByName);
        model.addAttribute("friends", userByName.getFriends());
        model.addAttribute("chatId", userByName.getId());
        model.addAttribute("showSend", false);
        return "chat";
    }


    @MessageMapping("/chat")
    public void sendMessage(Principal principal, @Payload MessageChat message) throws Exception {
        message.setTimeStamp();
        User notificationUser = userServiceImpl.getUserByName(principal.getName());
        messageNotificationUtils.createNotification(notificationUser, message);
        stompEventPublisher.publishEvent(message);
    }


    @GetMapping("/getUnreadMessages")
    @ResponseBody
    String getUnreadMessages(Principal principal, @RequestParam Long id) throws InterruptedException {

        User recipient = userServiceImpl.findById(id);
        User currentUser = userServiceImpl.getUserByName(principal.getName());
        List<MessageChat> all = messageChatServiceImpl.getChatMessagesByReceiptNameAndBySenderName(principal.getName(), recipient.getName());

        Comparator<MessageChat> compareByCreatedAt = Comparator.comparing(MessageChat::getCreatedAt);
        all.sort(compareByCreatedAt);

        if (!all.isEmpty()) {
            for (MessageChat mes : all
            ) {
                Thread.sleep(10);
                messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/hello", mes);
            }
        }
        List<MessageChat> chatMessages = all.stream().filter(m->m.getRecipientName().equals(currentUser.getName())).map(m -> {
            m.setIsRead(true);
            return m;
        }).collect(Collectors.toList());
        messageChatServiceImpl.saveCollection(chatMessages);
        deleteReadMessagesEventPublisher.publishEvent(currentUser, recipient);
        return "trigger";

    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        String currentUserName = Objects.requireNonNull(event.getUser()).getName();
        User currentUser = userServiceImpl.getUserByName(currentUserName);
        usersOnline.setUsersOffline(currentUserName);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        String currentUserName = Objects.requireNonNull(event.getUser()).getName();
        usersOnline.setUsersOnline(currentUserName);

    }


    @MessageMapping("/name")
    @SendTo("/topic/hello")
    public MessageChat sendMessageName(@Payload MessageChat message) throws Exception {
        return message;
    }


}
