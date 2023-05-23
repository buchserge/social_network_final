package com.example.social_network.repo;

import com.example.social_network.domain.entity.Comment;
import com.example.social_network.domain.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByMsg(Message msg, Pageable pageable);


}
