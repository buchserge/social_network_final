package com.example.social_network.repo;

import com.example.social_network.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface UserRepo extends JpaRepository<User, Long> {
    User findByName(String name);

    List<User> findAllByNameStartsWith(String name);

    User findByEmail(String email);

}
