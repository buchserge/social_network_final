package com.example.social_network.event;

import com.example.social_network.domain.MessageChat;
import com.example.social_network.domain.MessageNotification;
import com.example.social_network.domain.entity.User;
import com.example.social_network.service.MessageChatServiceImpl;
import com.example.social_network.service.MessageNotificationServiceImpl;
import com.example.social_network.utils.MessageNotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteReadMessagesEventListener {
    @Autowired
    private final MessageNotificationUtils messageNotificationUtils;
    @Autowired
    private final MessageChatServiceImpl messageChatServiceImpl;
    @Autowired
    private final MessageNotificationServiceImpl messageNotificationService;

    public DeleteReadMessagesEventListener(MessageNotificationUtils messageNotificationUtils, MessageChatServiceImpl messageChatServiceImpl, MessageNotificationServiceImpl messageNotificationService) {
        this.messageNotificationUtils = messageNotificationUtils;
        this.messageChatServiceImpl = messageChatServiceImpl;
        this.messageNotificationService = messageNotificationService;
    }

    @EventListener(DeleteReadMessagesEvent.class)
    public void handleDeleteReadMessagesEvent(DeleteReadMessagesEvent deleteReadMessagesEvent) throws InterruptedException {

        User currentUser = deleteReadMessagesEvent.getCurrentUser();
        User recipient = deleteReadMessagesEvent.getRecipient();

        List<MessageChat> chatMessagesByReceiptNameAndBySenderName = messageChatServiceImpl.getChatMessagesByReceiptNameAndBySenderName(currentUser.getName(), recipient.getName());
        if (chatMessagesByReceiptNameAndBySenderName.stream().allMatch(MessageChat::getIsRead) && chatMessagesByReceiptNameAndBySenderName.size() > 10L) {
            messageChatServiceImpl.deleteCollection(chatMessagesByReceiptNameAndBySenderName);
        }
        List<MessageNotification> messageNotifications = messageNotificationService.findBySenderNameAndRecipientName(recipient.getUsername(), currentUser.getUsername());
        messageNotificationUtils.deleteNotification(messageNotifications);

    }
}
