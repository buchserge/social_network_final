package com.example.social_network.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String text;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "msg",fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User usr;
    private Long likeCount= 0L;

    @ManyToMany
    @JoinTable(
            name = "message_user",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> usersLiked;

    public String toString() {
        return String.format("Message [id=%d, text=%s]", this.id, this.text);
    }


}
