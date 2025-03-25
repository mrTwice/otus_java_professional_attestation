package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDTO {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;

    private String firstName;
    private String lastName;
    private String pictureUrl;
    private String locale;
}