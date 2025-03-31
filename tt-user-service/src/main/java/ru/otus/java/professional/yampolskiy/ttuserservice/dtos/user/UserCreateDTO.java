package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDTO {

    @NotNull
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String oidcProvider;

    @NotNull
    private String oidcSubject;

    private boolean emailVerified;
    private boolean active;
    private boolean locked;
    private boolean phoneNumberVerified;

    // optional user profile
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