package com.example.social_network.service;

import com.example.social_network.domain.entity.Comment;
import com.example.social_network.domain.entity.Message;
import com.example.social_network.domain.entity.User;
import com.example.social_network.repo.CommentRepo;
import com.example.social_network.service.interfaces.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private final CommentRepo commentRepo;
    @Autowired
    private final UserServiceImpl userServiceImpl;
    @Autowired
    private final MessageServiceImpl messageServiceImpl;

    public CommentServiceImpl(CommentRepo commentRepo, UserServiceImpl userServiceImpl, MessageServiceImpl messageServiceImpl) {
        this.commentRepo = commentRepo;
        this.userServiceImpl = userServiceImpl;
        this.messageServiceImpl = messageServiceImpl;
    }

    @Override
    public void createComment(String userName, String comment, Long messageId) {
        User currentUser = userServiceImpl.getUserByName(userName);

        Message messageByID = messageServiceImpl.getMessageID(messageId);

        Comment newComment = new Comment();
        newComment.setText(comment);
        newComment.setAuthor(currentUser);
        newComment.setMsg(messageByID);
        commentRepo.save(newComment);
    }

    @Override
    public Page<Comment> showComments(Message msg, Pageable pageable) {
        return  commentRepo.findAllByMsg(msg, pageable);
    }
}
