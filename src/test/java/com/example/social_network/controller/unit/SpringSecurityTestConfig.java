package com.example.social_network.controller.unit;

import com.example.social_network.domain.entity.Role;
import com.example.social_network.domain.entity.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User basicUser = new User();
        basicUser.setName("Katty");
        basicUser.setPassword("$08$20Pj7ibX/EmHgFaNlMTlq.33i0p9XtK02KFOHk0YN29Cnw4pogMQ.");
        basicUser.getRoles().add(Role.USER);
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User("Katty",
                "Katty", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        return new InMemoryUserDetailsManager(Arrays.asList(user));
    }
}