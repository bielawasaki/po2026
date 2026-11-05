package com.securemessenger.service;

import com.securemessenger.dto.*;
import com.securemessenger.model.User;
import com.securemessenger.repository.UserRepository;
import com.securemessenger.security.TokenService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            TokenService tokenService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {

        if (request == null ||
                request.username() == null ||
                request.password() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username and password are required"
            );
        }

        String username = request.username()
                .trim()
                .toLowerCase(Locale.ROOT);

        String password = request.password();

        if (!username.matches("[a-z0-9_]{3,32}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username must contain 3-32 characters: a-z, 0-9, _"
            );
        }

        if (password.length() < 12 ||
                password.length() > 256) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must contain 12-256 characters"
            );
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }

        User user = new User(
                UUID.randomUUID().toString(),
                username,
                passwordEncoder.encode(password),
                System.currentTimeMillis()
        );

        try {
            userRepository.save(user);
        } catch (org.dizitart.no2.exceptions.UniqueConstraintException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }

        String token = tokenService.createToken(user.getId());

        return new AuthResponse(
                token,
                UserResponse.from(user)
        );
    }

    public AuthResponse login(LoginRequest request) {

        if (request == null ||
                request.username() == null ||
                request.password() == null) {
            throw invalidCredentials();
        }

        String username = request.username()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw invalidCredentials();
        }

        String token = tokenService.createToken(user.getId());

        return new AuthResponse(
                token,
                UserResponse.from(user)
        );
    }

    public UserResponse getCurrentUser(String token) {

        String userId = tokenService.validateToken(token)
                .orElseThrow(this::unauthorized);

        User user = userRepository.findById(userId)
                .orElseThrow(this::unauthorized);

        return UserResponse.from(user);
    }

    public void logout(String token) {

        if (tokenService.validateToken(token).isEmpty()) {
            throw unauthorized();
        }

        tokenService.revokeToken(token);
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password"
        );
    }

    private ResponseStatusException unauthorized() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid or expired session"
        );
    }
}