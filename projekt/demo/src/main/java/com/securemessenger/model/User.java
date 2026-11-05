package com.securemessenger.model;

public class User {

    private String id;
    private String username;
    private String passwordHash;
    private long createdAt;
    private UserProfile profile;

    public User() {
    }

    public User(String id, String username,
                String passwordHash, long createdAt) {

        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.profile = new UserProfile(username);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}