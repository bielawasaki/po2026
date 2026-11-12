package com.securemessenger.controller;

import com.securemessenger.dto.UpdateProfileRequest;
import com.securemessenger.dto.UserResponse;
import com.securemessenger.service.AuthService;
import com.securemessenger.service.ProfileService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;
    private final ProfileService profileService;

    public UserController(
            AuthService authService,
            ProfileService profileService
    ) {
        this.authService = authService;
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public UserResponse me(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authorization
    ) {
        String token = extractToken(authorization);

        return authService.getCurrentUser(token);
    }

    @PatchMapping("/me")
    public UserResponse updateProfile(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authorization,

            @RequestBody UpdateProfileRequest request
    ) {
        String token = extractToken(authorization);

        return profileService.updateProfile(token, request);
    }

    private String extractToken(String authorization) {

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Missing bearer token"
            );
        }

        return authorization.substring(7);
    }
}