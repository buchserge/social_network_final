package com.example.social_network.utils;

import com.example.social_network.domain.MessageChat;
import com.example.social_network.domain.MessageNotification;
import com.example.social_network.domain.entity.User;
import com.example.social_network.service.MessageChatServiceImpl;
import com.example.social_network.service.MessageNotificationServiceImpl;
import com.example.social_network.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Component
public class MessageNotificationUtils {
    @Autowired
    private final MessageChatServiceImpl messageChatServiceImpl;
    @Autowired
    private final MessageNotificationServiceImpl messageNotificationServiceImpl;
    @Autowired
    private final UserServiceImpl userServiceImpl;

    public MessageNotificationUtils(MessageChatServiceImpl messageChatServiceImpl, MessageNotificationServiceImpl messageNotificationServiceImpl,
                                    UserServiceImpl userServiceImpl) {
        this.messageChatServiceImpl = messageChatServiceImpl;
        this.messageNotificationServiceImpl = messageNotificationServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    public void createNotification(User notificationUser, MessageChat message) {

        User recipientName = userServiceImpl.getUserByName(message.getRecipientName());

        message.setChatroomId(String.format("%s_%s", message.getSenderName(), message.getRecipientName()));
        message.setIsRead(false);
        message.setChatUser(recipientName);
        messageChatServiceImpl.saveChatMessage(message);

        userServiceImpl.saveNew(notificationUser);


        List<MessageNotification> messageNotifications = messageNotificationServiceImpl
                .findBySenderNameAndRecipientName(notificationUser.getUsername(), recipientName.getUsername());

        if (!ObjectUtils.isEmpty(messageNotifications)) {
            List<MessageNotification> notificationsBySenderAndRecipient = messageNotifications.stream().filter(m -> m.getSenderName().
                    equals(notificationUser.getUsername()) && m.getRecipientName().equals(recipientName.getUsername())).toList();
            Optional<MessageNotification> first = notificationsBySenderAndRecipient.stream().findFirst();
            MessageNotification notification = first.get();

            if (notification.getTotal() == null) {
                notification.setTotal(1L);
            } else {
                notification.setTotal(notification.getTotal() + 1L);
                messageNotificationServiceImpl.saveNotific(notification);
            }

        } else {
            MessageNotification messageNotification = new MessageNotification();
            messageNotification.setSenderName(notificationUser.getUsername());
            messageNotification.setRecipientName(recipientName.getUsername());
            messageNotification.setTotal(1L);
            messageNotificationServiceImpl.saveNotific(messageNotification);
        }
    }

    public void deleteNotification(List<MessageNotification> messageNotifications) {
        try {
            MessageNotification notification = messageNotifications.stream().findFirst().orElseThrow();
            notification.setTotal(0L);
            messageNotificationServiceImpl.saveNotific(notification);
        } catch (Exception exception) {
            System.out.println(exception.getMessage() + " NO MESSAGE NOTIFICATIONS FOUND FOR THE USER");

        }

    }

}
