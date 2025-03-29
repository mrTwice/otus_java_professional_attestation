package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoteUserDetailsService implements UserDetailsService {

    //private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserPrincipalDTO user = userClient.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername("test-user")
                .password("{bcrypt}$2a$10$XNQVSzziAeYSJNRQrx56oe9vrmU7BXf0D0xNCDOgY370Jip5JuMuO")
                .roles("USER")
                .build();
    }
}