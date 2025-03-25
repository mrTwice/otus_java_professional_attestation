package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangePasswordDTO {
    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;
}