package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "Пароль должен быть не короче 8 символов")
    private String password;

    private String firstName;
    private String middleName;
    private String lastName;
    private String nickname;
    private String pictureUrl;
    private String profile;
    private String website;
    private String locale;
    private String gender;
    private LocalDate birthdate;
    private String zoneinfo;
    private String phoneNumber;
    private Map<String, Object> address;
}
