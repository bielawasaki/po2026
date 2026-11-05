package com.securemessenger.model;

public class UserProfile {

    private String displayName;
    private String avatarId;
    private String bio;

    public UserProfile() {
    }

    public UserProfile(String displayName) {
        this.displayName = displayName;
        this.avatarId = null;
        this.bio = "";
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public String getBio() {
        return bio;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}