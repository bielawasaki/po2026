package com.securemessenger.security;

import com.securemessenger.model.UserSession;
import com.securemessenger.repository.SessionRepository;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private static final long SESSION_DURATION =
            30L * 24 * 60 * 60 * 1000;

    private final SecureRandom random = new SecureRandom();
    private final SessionRepository sessionRepository;

    public TokenService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public String createToken(String userId) {

        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        long now = System.currentTimeMillis();

        UserSession session = new UserSession(
                UUID.randomUUID().toString(),
                userId,
                hashToken(token),
                now,
                now + SESSION_DURATION
        );

        sessionRepository.save(session);

        return token;
    }

    public Optional<String> validateToken(String token) {

        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        String tokenHash = hashToken(token);

        Optional<UserSession> found =
                sessionRepository.findByTokenHash(tokenHash);

        if (found.isEmpty()) {
            return Optional.empty();
        }

        UserSession session = found.get();

        if (System.currentTimeMillis() >= session.expiresAt()) {
            sessionRepository.deleteByTokenHash(tokenHash);
            return Optional.empty();
        }

        return Optional.of(session.userId());
    }

    public void revokeToken(String token) {

        if (token == null || token.isBlank()) {
            return;
        }

        sessionRepository.deleteByTokenHash(
                hashToken(token)
        );
    }

    private String hashToken(String token) {
      
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 is not available", e
            );
        }
    }
}