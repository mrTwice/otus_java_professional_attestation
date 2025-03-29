package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.repository;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Repository
public class InMemoryUserInfoRepository implements UserInfoProvider {

    private final Map<String, Map<String, Object>> userInfo = new HashMap<>();

    public InMemoryUserInfoRepository() {
        userInfo.put("test-user", createUser("test-user"));
    }


    @Override
    public Map<String, Object> getClaimsByUsername(String username) {
        return userInfo.get(username);
    }

    public Map<String, Object> findByUsername(String username) {
        return getClaimsByUsername(username);
    }

    private static Map<String, Object> createUser(String username) {
        return OidcUserInfo.builder()
                .subject(username)
                .name("First Last")
                .givenName("First")
                .familyName("Last")
                .middleName("Middle")
                .nickname("User")
                .preferredUsername(username)
                .profile("https://example.com/" + username)
                .picture("https://example.com/" + username + ".jpg")
                .website("https://example.com")
                .email(username + "@example.com")
                .emailVerified(true)
                .gender("female")
                .birthdate("1970-01-01")
                .zoneinfo("Europe/Paris")
                .locale("en-US")
                .phoneNumber("+1 (604) 555-1234;ext=5678")
                .phoneNumberVerified(false)
                .claim("address", Collections.singletonMap("formatted",
                        "Champ de Mars\n5 Av. Anatole France\n75007 Paris\nFrance"))
                .updatedAt("1970-01-01T00:00:00Z")
                .build()
                .getClaims();
    }
}
