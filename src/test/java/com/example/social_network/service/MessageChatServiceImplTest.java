package com.example.social_network.service;

import com.example.social_network.domain.MessageChat;
import com.example.social_network.repo.MessageChatRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MessageChatServiceImplTest {
    @Autowired
    private MessageChatServiceImpl messageChatService;
    @MockBean
    private  MessageChatRepo messageChatRepo;
    @Test
    void getChatMessagesByReceiptNameAndBySenderName() {
        List<MessageChat> list1=new ArrayList<>();
        List<MessageChat> list2=new ArrayList<>();

        MessageChat messageChat1 = new MessageChat();
        messageChat1.setId(1L);
        messageChat1.setText("first");
        messageChat1.setRecipientName("reciepient1");
        messageChat1.setSenderName("sender1");

        MessageChat messageChat2 = new MessageChat();
        messageChat2.setId(2L);
        messageChat2.setText("second");
        messageChat2.setRecipientName("reciepient1");
        messageChat2.setSenderName("sender1");

        MessageChat messageChat3 = new MessageChat();
        messageChat3.setId(3L);
        messageChat3.setText("third");
        messageChat3.setRecipientName("sender1");
        messageChat3.setSenderName("sender1");

        MessageChat messageChat4 = new MessageChat();
        messageChat4.setId(4L);
        messageChat4.setText("fourth");
        messageChat4.setRecipientName("sender1");
        messageChat4.setSenderName("reciepient1");
        list1.add(messageChat1);
        list1.add(messageChat2);
        list2.add(messageChat3);
        list2.add(messageChat4);

        ArrayList<MessageChat> resultTest = new ArrayList<>();
        resultTest.addAll(list1);
        resultTest.addAll(list2);

        Mockito.doReturn(list1).when(messageChatRepo).findAllByRecipientNameAndSenderName("reciepient1","sender1");
        Mockito.doReturn(list2).when(messageChatRepo).findAllByRecipientNameAndSenderName("sender1","reciepient1");
        List<MessageChat> result = messageChatService.getChatMessagesByReceiptNameAndBySenderName("reciepient1", "sender1");

        Assertions.assertTrue(result.equals(resultTest));
    }
}