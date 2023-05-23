package com.example.social_network.event;

import com.example.social_network.domain.MessageChat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StompEvent{

    private MessageChat messageChat;


}

