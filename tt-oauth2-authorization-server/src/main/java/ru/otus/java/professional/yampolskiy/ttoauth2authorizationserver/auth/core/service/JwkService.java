package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.service;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
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

    private final JwkKeyRepository jwkKeyRepository;

    public JWKSet loadOrCreateJwkSet() {
        return jwkKeyRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .map(jwkKey -> {
                    try {
                        return JWKSet.parse(jwkKey.getKeyData());
                    } catch (Exception e) {
                        throw new RuntimeException("Ошибка парсинга JWK из базы", e);
                    }
                })
                .orElseGet(() -> {
                    RSAKey rsaKey = generateRsaKey();
                    JWKSet jwkSet = new JWKSet(rsaKey);

                    // Сохраняем в базу
                    JwkKey keyEntity = new JwkKey();
                    keyEntity.setKid(UUID.fromString(rsaKey.getKeyID()));
                    keyEntity.setKeyData(jwkSet.toString());
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
                jwks.addAll(JWKSet.parse(key.getKeyData()).getKeys());
            } catch (Exception e) {
                throw new RuntimeException("Ошибка парсинга JWK", e);
            }
        }

        return new JWKSet(jwks);
    }

    @Transactional
    public JwkKey rotateKey() {
        // Деактивируем все предыдущие
        jwkKeyRepository.updateAllSetInactive();

        // Генерируем новый
        RSAKey rsaKey = generateRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);

        JwkKey newKey = new JwkKey();
        UUID kid = UUID.randomUUID();
        newKey.setKid(kid);
        newKey.setKeyData(jwkSet.toString());
        newKey.setActive(true);
        newKey.setCreatedAt(Instant.now());
        return jwkKeyRepository.save(newKey);
    }

}
