package com.example.social_network.repo;

import com.example.social_network.domain.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    Page<Message> findAllByUsr_Id(Long id, Pageable pageable);
}