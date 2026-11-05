package com.securemessenger.repository;

import com.securemessenger.model.User;
import com.securemessenger.model.UserProfile;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.NitriteCollection;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.dizitart.no2.filters.FluentFilter.where;
import static org.dizitart.no2.collection.Document.createDocument;

@Repository
public class UserRepository {

    private final NitriteCollection users;

    public UserRepository(Nitrite database) {

        users = database.getCollection("users");

        if (!users.hasIndex("username")) {
            users.createIndex("username");
        }

        if (!users.hasIndex("id")) {
            users.createIndex("id");
        }
    }

    public void save(User user) {

        Document profile = createDocument(
                "displayName",
                user.getProfile().getDisplayName()
        )
        .put("avatarId", user.getProfile().getAvatarId())
        .put("bio", user.getProfile().getBio());

        Document document = createDocument("id", user.getId())
                .put("username", user.getUsername())
                .put("passwordHash", user.getPasswordHash())
                .put("createdAt", user.getCreatedAt())
                .put("profile", profile);

        users.insert(document);
    }

    public Optional<User> findByUsername(String username) {

        Document document = users.find(
                where("username").eq(username)
        ).firstOrNull();

        return Optional.ofNullable(document)
                .map(this::toUser);
    }

    public Optional<User> findById(String id) {

        Document document = users.find(
                where("id").eq(id)
        ).firstOrNull();

        return Optional.ofNullable(document)
                .map(this::toUser);
    }

    private User toUser(Document document) {

        User user = new User(
                document.get("id", String.class),
                document.get("username", String.class),
                document.get("passwordHash", String.class),
                document.get("createdAt", Long.class)
        );

        Document profileDocument =
                document.get("profile", Document.class);

        if (profileDocument != null) {

            UserProfile profile = new UserProfile();

            profile.setDisplayName(
                    profileDocument.get("displayName", String.class)
            );

            profile.setAvatarId(
                    profileDocument.get("avatarId", String.class)
            );

            profile.setBio(
                    profileDocument.get("bio", String.class)
            );

            user.setProfile(profile);
        }

        return user;
    }
}