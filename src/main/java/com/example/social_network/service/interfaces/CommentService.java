package com.example.social_network.service.interfaces;

import com.example.social_network.domain.entity.Comment;
import com.example.social_network.domain.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    void createComment(String userName, String comment, Long messageId);

    Page<Comment> showComments(Message msg, Pageable pageable);
}
