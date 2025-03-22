package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.io.*;
import java.util.Base64;

public class OAuth2AuthorizationRequestUtils {

    public static String serialize(OAuth2AuthorizationRequest request) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(request);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException ex) {
            throw new IllegalStateException("Не удалось сериализовать OAuth2AuthorizationRequest", ex);
        }
    }

    public static OAuth2AuthorizationRequest deserialize(String serialized) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(serialized));
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (OAuth2AuthorizationRequest) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw new IllegalStateException("Не удалось десериализовать OAuth2AuthorizationRequest", ex);
        }
    }
}
