package com.example.social_network.service;

import com.example.social_network.domain.dto.UserDTO;
import com.example.social_network.domain.entity.User;
import com.example.social_network.repo.UserRepo;
import com.example.social_network.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByName(username);
    }

    @Override
    public User getUserByName(String name) {
        return userRepo.findByName(name);
    }

    @Override
    public void updateUserCredentials(Long id, UserDTO userDTO) {
        User user = userRepo.findById(id).orElseThrow();
        if (user.getUsername().equals(userDTO.getName()) || user.getPassword().equals(userDTO.getPassword())) {
            throw new DataIntegrityViolationException("same credentials");
        }
        user.setName(userDTO.getName());
        user.setPassword((passwordEncoder.encode(userDTO.getPassword())));
        userRepo.save(user);
    }

    @Override
    public void saveNew(User user) {
        userRepo.save(user);
    }

    @Transactional
    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow();
    }

    @Override
    public List<User> findAllUsersByName(String tagName) {
        return userRepo.findAllByNameStartsWith(tagName);
    }
}
