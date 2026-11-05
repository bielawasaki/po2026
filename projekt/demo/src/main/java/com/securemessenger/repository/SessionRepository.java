package com.securemessenger.repository;

import com.securemessenger.model.UserSession;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.NitriteCollection;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.dizitart.no2.filters.FluentFilter.where;
import static org.dizitart.no2.collection.Document.createDocument;

@Repository
public class SessionRepository {

    private final NitriteCollection sessions;

    public SessionRepository(Nitrite database) {

        sessions = database.getCollection("sessions");

        if (!sessions.hasIndex("tokenHash")) {
            sessions.createIndex("tokenHash");
        }
    }

    public void save(UserSession session) {

        Document document = createDocument("id", session.id())
                .put("userId", session.userId())
                .put("tokenHash", session.tokenHash())
                .put("createdAt", session.createdAt())
                .put("expiresAt", session.expiresAt());

        sessions.insert(document);
    }

    public Optional<UserSession> findByTokenHash(String tokenHash) {

        Document document = sessions.find(
                where("tokenHash").eq(tokenHash)
        ).firstOrNull();

        if (document == null) {
            return Optional.empty();
        }

        UserSession session = new UserSession(
                document.get("id", String.class),
                document.get("userId", String.class),
                document.get("tokenHash", String.class),
                document.get("createdAt", Long.class),
                document.get("expiresAt", Long.class)
        );

        return Optional.of(session);
    }

    public void deleteByTokenHash(String tokenHash) {

        sessions.remove(
                where("tokenHash").eq(tokenHash)
        );
    }
}