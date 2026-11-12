package com.securemessenger.service;

import com.securemessenger.dto.UserResponse;
import com.securemessenger.model.User;
import com.securemessenger.repository.UserRepository;
import com.securemessenger.security.TokenService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

@Service
public class AvatarService {

    private static final long MAX_FILE_SIZE = 2L * 1024 * 1024;
    private static final int MAX_DIMENSION = 4096;
    private static final long MAX_PIXELS = 4_000_000L;

    private static final int AVATAR_SIZE = 1024;

    private static final int MAX_GIF_FRAMES = 100;
    private static final long MAX_GIF_PIXELS = 25_000_000L;

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final Path avatarDirectory;

    public AvatarService(
            UserRepository userRepository,
            TokenService tokenService,
            @Value("${app.avatars.directory:data/avatars}")
            String directory
    ) throws IOException {

        this.userRepository = userRepository;
        this.tokenService = tokenService;

        this.avatarDirectory = Path.of(directory)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(avatarDirectory);
    }

    // ==========================================
    // UPLOAD AVATARA
    // ==========================================

    public synchronized UserResponse uploadAvatar(
            String token,
            MultipartFile file
    ) throws IOException {

        User user = getUser(token);

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Avatar file is required"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.CONTENT_TOO_LARGE,
                    "Avatar cannot exceed 2 MB"
            );
        }

        byte[] bytes = file.getBytes();

        byte[] outputBytes;
        String extension;

        // GIF zachowujemy w oryginalnej postaci.
        if (isGif(bytes)) {

            validateGif(bytes);

            outputBytes = bytes;
            extension = ".gif";

        } else {

            // PNG/JPEG konwertujemy na PNG.
            outputBytes = convertToPng(bytes);
            extension = ".png";
        }

        String avatarId = UUID.randomUUID().toString();

        Path newPath = avatarDirectory.resolve(
                avatarId + extension
        );

        String oldAvatarId = user.getProfile().getAvatarId();

        // 1. Zapis nowego pliku
        Files.write(
                newPath,
                outputBytes,
                StandardOpenOption.CREATE_NEW
        );

        // 2. Aktualizacja bazy
        try {
            user.getProfile().setAvatarId(avatarId);
            userRepository.updateProfile(user);

        } catch (RuntimeException e) {

            try {
                Files.deleteIfExists(newPath);
            } catch (IOException cleanupError) {
                e.addSuppressed(cleanupError);
            }

            throw e;
        }

        // 3. Usunięcie starego avatara
        deleteOldFile(oldAvatarId);

        return UserResponse.from(user);
    }

    // ==========================================
    // USUWANIE AVATARA
    // ==========================================

    public synchronized UserResponse deleteAvatar(String token) {

        User user = getUser(token);

        String oldAvatarId = user.getProfile().getAvatarId();

        if (oldAvatarId == null) {
            return UserResponse.from(user);
        }

        user.getProfile().setAvatarId(null);

        // Najpierw aktualizujemy bazę.
        userRepository.updateProfile(user);

        // Dopiero potem usuwamy plik.
        deleteOldFile(oldAvatarId);

        return UserResponse.from(user);
    }

    // ==========================================
    // POBIERANIE AVATARA
    // ==========================================

    public AvatarData getAvatar(String avatarId)
            throws IOException {

        Path path = findAvatarPath(avatarId);

        if (path == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Avatar not found"
            );
        }

        MediaType type = path.toString().endsWith(".gif")
                ? MediaType.IMAGE_GIF
                : MediaType.IMAGE_PNG;

        return new AvatarData(
                Files.readAllBytes(path),
                type
        );
    }

    public record AvatarData(
            byte[] bytes,
            MediaType mediaType
    ) {
    }

    // ==========================================
    // AUTORYZACJA
    // ==========================================

    private User getUser(String token) {

        String userId = tokenService.validateToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid or expired session"
                ));

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"
                ));
    }

    // ==========================================
    // OBSLUGA PLIKOW
    // ==========================================

    private Path findAvatarPath(String avatarId) {

        if (avatarId == null ||
                !avatarId.matches(
                        "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-"
                        + "[0-9a-f]{4}-[0-9a-f]{12}"
                )) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid avatar ID"
            );
        }

        Path png = avatarDirectory.resolve(avatarId + ".png");
        Path gif = avatarDirectory.resolve(avatarId + ".gif");

        if (Files.isRegularFile(png)) {
            return png;
        }

        if (Files.isRegularFile(gif)) {
            return gif;
        }

        return null;
    }

    private void deleteOldFile(String avatarId) {

        if (avatarId == null) {
            return;
        }

        try {
            Path path = findAvatarPath(avatarId);

            if (path != null) {
                Files.deleteIfExists(path);
            }

        } catch (IOException e) {
            System.err.println(
                    "Failed to delete old avatar: "
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // ROZPOZNAWANIE GIF
    // ==========================================

    private boolean isGif(byte[] bytes) {

        if (bytes.length < 6) {
            return false;
        }

        String header = new String(
                bytes,
                0,
                6,
                StandardCharsets.US_ASCII
        );

        return header.equals("GIF87a")
                || header.equals("GIF89a");
    }

    // ==========================================
    // WALIDACJA GIF
    // ==========================================

    private void validateGif(byte[] bytes) throws IOException {

        try (ByteArrayInputStream input =
                     new ByteArrayInputStream(bytes);

             ImageInputStream imageInput =
                     ImageIO.createImageInputStream(input)) {

            if (imageInput == null) {
                throw invalidImage();
            }

            Iterator<ImageReader> readers =
                    ImageIO.getImageReaders(imageInput);

            if (!readers.hasNext()) {
                throw invalidImage();
            }

            ImageReader reader = readers.next();

            try {
                reader.setInput(imageInput);

                if (!reader.getFormatName()
                        .equalsIgnoreCase("gif")) {
                    throw invalidImage();
                }

                // Wymiary logicznego obszaru GIF
                // zapisane w naglowku pliku.
                if (bytes.length < 13) {
                    throw invalidImage();
                }

                int canvasWidth =
                        (bytes[6] & 0xff)
                        | ((bytes[7] & 0xff) << 8);

                int canvasHeight =
                        (bytes[8] & 0xff)
                        | ((bytes[9] & 0xff) << 8);

                if (canvasWidth <= 0 ||
                        canvasHeight <= 0 ||
                        canvasWidth > AVATAR_SIZE ||
                        canvasHeight > AVATAR_SIZE) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "GIF canvas cannot exceed 512x512"
                    );
                }

                int frames = reader.getNumImages(true);

                if (frames < 1 ||
                        frames > MAX_GIF_FRAMES) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "GIF must contain 1-100 frames"
                    );
                }

                long totalPixels = 0;

                for (int i = 0; i < frames; i++) {

                    int width = reader.getWidth(i);
                    int height = reader.getHeight(i);

                    if (width <= 0 || height <= 0 ||
                            width > AVATAR_SIZE ||
                            height > AVATAR_SIZE) {

                        throw invalidImage();
                    }

                    totalPixels += (long) width * height;

                    if (totalPixels > MAX_GIF_PIXELS) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "GIF contains too many pixels"
                        );
                    }

                    // Sprawdzamy dekodowanie kazdej klatki.
                    if (reader.read(i) == null) {
                        throw invalidImage();
                    }
                }

            } finally {
                reader.dispose();
            }

        } catch (IOException | IndexOutOfBoundsException e) {
            throw invalidImage();
        }
    }

    // ==========================================
    // KONWERSJA PNG/JPEG
    // ==========================================

    private byte[] convertToPng(byte[] bytes)
            throws IOException {

        BufferedImage original;

        try (ByteArrayInputStream input =
                     new ByteArrayInputStream(bytes);

             ImageInputStream imageInput =
                     ImageIO.createImageInputStream(input)) {

            if (imageInput == null) {
                throw invalidImage();
            }

            Iterator<ImageReader> readers =
                    ImageIO.getImageReaders(imageInput);

            if (!readers.hasNext()) {
                throw invalidImage();
            }

            ImageReader reader = readers.next();

            try {
                reader.setInput(imageInput);

                String format = reader.getFormatName()
                        .toLowerCase(Locale.ROOT);

                if (!format.equals("png") &&
                        !format.equals("jpeg") &&
                        !format.equals("jpg")) {
                    throw invalidImage();
                }

                int width = reader.getWidth(0);
                int height = reader.getHeight(0);

                if (width <= 0 || height <= 0 ||
                        width > MAX_DIMENSION ||
                        height > MAX_DIMENSION ||
                        (long) width * height > MAX_PIXELS) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Image dimensions are too large"
                    );
                }

                original = reader.read(0);

                if (original == null) {
                    throw invalidImage();
                }

            } finally {
                reader.dispose();
            }

        } catch (IOException | IndexOutOfBoundsException e) {
            throw invalidImage();
        }

        // Skalowanie z zachowaniem proporcji
        double scale = Math.min(
                1.0,
                Math.min(
                        (double) AVATAR_SIZE / original.getWidth(),
                        (double) AVATAR_SIZE / original.getHeight()
                )
        );

        int width = Math.max(
                1,
                (int) Math.round(original.getWidth() * scale)
        );

        int height = Math.max(
                1,
                (int) Math.round(original.getHeight() * scale)
        );

        BufferedImage result = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D graphics = result.createGraphics();

        try {
            graphics.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC
            );

            graphics.drawImage(
                    original,
                    0, 0,
                    width, height,
                    null
            );

        } finally {
            graphics.dispose();
        }

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        if (!ImageIO.write(result, "png", output)) {
            throw new IOException("PNG encoder unavailable");
        }

        return output.toByteArray();
    }

    // ==========================================
    // POMOCNICZY WYJATEK
    // ==========================================

    private ResponseStatusException invalidImage() {

        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "File must be a valid PNG, JPEG or GIF image"
        );
    }
}