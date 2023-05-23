package com.example.social_network.domain.dto;



import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotEmpty(message = "Field can't be empty")
    private String name;
    @NotEmpty(message = "Field can't be empty")
    private String password;
    private String email;
}
