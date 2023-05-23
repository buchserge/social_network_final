package com.example.social_network.domain;

import com.example.social_network.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB", length = Integer.MAX_VALUE)
    byte[] imageData;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String toString() {
        return String.format("ImageInfo [id=%d, name=%s]", this.id, this.name);
    }
}
