package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String pictureUrl;
    private String locale;
}
