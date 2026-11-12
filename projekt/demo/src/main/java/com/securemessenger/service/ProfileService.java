package com.securemessenger.service;

import com.securemessenger.dto.UpdateProfileRequest;
import com.securemessenger.dto.UserResponse;
import com.securemessenger.model.User;
import com.securemessenger.repository.UserRepository;
import com.securemessenger.security.TokenService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public ProfileService(
            UserRepository userRepository,
            TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public UserResponse updateProfile(
            String token,
            UpdateProfileRequest request
    ) {

        // 1. Sprawdzamy token
        String userId = tokenService.validateToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid or expired session"
                ));

        // 2. Pobieramy użytkownika
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"
                ));

        // 3. Sprawdzamy, czy jest co aktualizować
        if (request == null ||
                (request.username() == null &&
                 request.displayName() == null &&
                 request.bio() == null)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No profile fields provided"
            );
        }

        // 4. Aktualizacja username
        if (request.username() != null) {

            String username = request.username()
                    .trim()
                    .toLowerCase(Locale.ROOT);

            if (!username.matches("[a-z0-9_]{3,32}")) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Username must contain 3-32 characters: a-z, 0-9, _"
                );
            }

            if (!username.equals(user.getUsername())) {

                if (userRepository.findByUsername(username).isPresent()) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Username already exists"
                    );
                }

                user.setUsername(username);
            }
        }

        // 5. Aktualizacja displayName
        if (request.displayName() != null) {

            String displayName = request.displayName().trim();

            if (displayName.isEmpty() ||
                    displayName.length() > 64) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Display name must contain 1-64 characters"
                );
            }

            user.getProfile().setDisplayName(displayName);
        }

        // 6. Aktualizacja bio
        if (request.bio() != null) {

            String bio = request.bio();

            if (bio.length() > 500) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Bio cannot exceed 500 characters"
                );
            }

            user.getProfile().setBio(bio);
        }

        // 7. Zapis zmian
        try {
            userRepository.updateProfile(user);
        } catch (RuntimeException e) {

            // Możliwy konflikt przy równoczesnej zmianie username
            var existing = userRepository
                    .findByUsername(user.getUsername());

            if (existing.isPresent() &&
                    !existing.get().getId().equals(user.getId())) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Username already exists"
                );
            }

            throw e;
        }

        // 8. Zwracamy zaktualizowany profil
        return UserResponse.from(user);
    }
}