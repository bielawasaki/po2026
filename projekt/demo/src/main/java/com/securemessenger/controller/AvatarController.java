package com.securemessenger.controller;

import com.securemessenger.dto.UserResponse;
import com.securemessenger.service.AvatarService;
import com.securemessenger.service.AvatarService.AvatarData;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    // Upload lub zmiana avatara
    @PostMapping(
            value = "/api/users/me/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public UserResponse uploadAvatar(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authorization,

            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String token = extractToken(authorization);

        return avatarService.uploadAvatar(token, file);
    }

    // Usuniecie wlasnego avatara
    @DeleteMapping("/api/users/me/avatar")
    public UserResponse deleteAvatar(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authorization
    ) {

        String token = extractToken(authorization);

        return avatarService.deleteAvatar(token);
    }

    // Publiczne pobieranie avatara
    @GetMapping("/api/avatars/{avatarId}")
    public ResponseEntity<byte[]> getAvatar(
            @PathVariable String avatarId
    ) throws IOException {

        AvatarData avatar = avatarService.getAvatar(avatarId);

        return ResponseEntity.ok()
                .contentType(avatar.mediaType())
                .header(
                        HttpHeaders.CACHE_CONTROL,
                        "public, max-age=86400"
                )
                .body(avatar.bytes());
    }

    // Odczyt tokena z naglowka Authorization
    private String extractToken(String authorization) {

        if (authorization == null ||
                !authorization.startsWith("Bearer ") ||
                authorization.substring(7).isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Missing bearer token"
            );
        }

        return authorization.substring(7);
    }
}