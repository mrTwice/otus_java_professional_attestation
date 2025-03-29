package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.service;

public interface KeyEncryptionService {
    String encrypt(String plainText);
    String decrypt(String encryptedText);
}
