package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.entity.JwkKey;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.repository.JwkKeyRepository;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwkService {

    private final KeyEncryptionService keyEncryptionService;

    //TODO Шифровать приватные ключи перед сохранением - OK
    //TODO отдельный пользователя БД, у которого доступ только к таблице JWK.
    //TODO разделить доступ: один сервис может только читать (jwtDecoder), другой — писать (rotateKey).
    //TODO Зашифровать БД на уровне хоста или через disk-level encryption (LUKS, ecryptfs и т.п.).
    //TODO Хранить secret в HashiCorp Vault, AWS KMS, GCP KMS
    // || Использовать Jasypt (Spring Boot поддерживает автоматическую расшифровку в @Value)
    // || Сделать KeyStore на диске и читать ключ оттуда

    private static final Logger LOGGER = LoggerFactory.getLogger(JwkService.class);

    private final JwkKeyRepository jwkKeyRepository;

    public JWKSet loadOrCreateJwkSet() {
        return jwkKeyRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .map(jwkKey -> {
                    try {
                        return JWKSet.parse(keyEncryptionService.decrypt(jwkKey.getKeyData()));
                    } catch (Exception e) {
                        throw new RuntimeException("Ошибка парсинга JWK из базы", e);
                    }
                })
                .orElseGet(() -> {
                    RSAKey rsaKey = generateRsaKey();
                    JWKSet jwkSet = new JWKSet(rsaKey);

                    JwkKey keyEntity = new JwkKey();
                    UUID kid = UUID.fromString(rsaKey.getKeyID());
                    keyEntity.setKid(kid);
                    String encrypted = keyEncryptionService.encrypt(jwkSet.toString(false));
                    keyEntity.setKeyData(encrypted);
                    keyEntity.setCreatedAt(Instant.now());
                    keyEntity.setActive(true);
                    jwkKeyRepository.save(keyEntity);

                    return jwkSet;
                });
    }

    private RSAKey generateRsaKey() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();
            return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                    .privateKey((RSAPrivateKey) keyPair.getPrivate())
                    .keyID(UUID.randomUUID().toString())
                    .algorithm(JWSAlgorithm.RS256)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации RSA ключа", e);
        }
    }


    public JWKSet loadActiveJwkSet() {
        List<JwkKey> activeKeys = jwkKeyRepository.findAllByIsActiveTrueOrderByCreatedAtDesc();
        List<JWK> jwks = new ArrayList<>();

        for (JwkKey key : activeKeys) {
            try {
                jwks.addAll(JWKSet.parse(keyEncryptionService.decrypt(key.getKeyData())).getKeys());
            } catch (Exception e) {
                throw new RuntimeException("Ошибка парсинга JWK", e);
            }
        }

        return new JWKSet(jwks);
    }

    public JWKSet loadValidationJwkSet() {
        List<JwkKey> activeKeys = jwkKeyRepository.findAllByIsActiveTrueOrderByCreatedAtDesc();
        List<JWK> publicKeys = new ArrayList<>();

        for (JwkKey key : activeKeys) {
            try {
                List<JWK> keys = JWKSet.parse(keyEncryptionService.decrypt(key.getKeyData())).getKeys();
                for (JWK jwk : keys) {
                    publicKeys.add(jwk.toPublicJWK());
                }
            } catch (Exception e) {
                throw new RuntimeException("Ошибка парсинга JWK", e);
            }
        }

        return new JWKSet(publicKeys);
    }

    public JWKSet loadSigningJwkSet() {
        return jwkKeyRepository.findFirstByIsPrimaryTrueAndIsActiveTrueOrderByCreatedAtDesc()
                .map(jwkKey -> {
                    try {
                        return JWKSet.parse(keyEncryptionService.decrypt(jwkKey.getKeyData()));
                    } catch (Exception e) {
                        throw new RuntimeException("Ошибка парсинга JWK для подписи", e);
                    }
                })
                .orElseThrow(() -> new IllegalStateException("❌ Нет активного primary ключа"));
    }

    @Transactional
    public JwkKey rotateKey() {
        // Снять флаг primary со всех активных ключей
        jwkKeyRepository.clearPrimaryFromAll();

        // Генерация нового RSA ключа
        RSAKey rsaKey = generateRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);

        JwkKey newKey = new JwkKey();
        newKey.setKid(UUID.fromString(rsaKey.getKeyID()));
        newKey.setKeyData(keyEncryptionService.encrypt(jwkSet.toString(false)));
        newKey.setActive(true);   // 🔑 остаётся активным
        newKey.setPrimary(true);  // 🌟 основной для подписи
        newKey.setCreatedAt(Instant.now());

        return jwkKeyRepository.save(newKey);
    }

    public boolean noPrimaryExists() {
        return jwkKeyRepository.findFirstByIsPrimaryTrueAndIsActiveTrueOrderByCreatedAtDesc().isEmpty();
    }

    @Transactional
    public JwkKey generatePrimaryKey() {
        // Генерация нового RSA ключа
        RSAKey rsaKey = generateRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);

        JwkKey newKey = new JwkKey();
        UUID kid = UUID.fromString(rsaKey.getKeyID());
        newKey.setKid(kid);
        newKey.setKeyData(keyEncryptionService.encrypt(jwkSet.toString(false)));
        newKey.setActive(true);
        newKey.setPrimary(true);
        newKey.setCreatedAt(Instant.now());

        return jwkKeyRepository.save(newKey);
    }

}
