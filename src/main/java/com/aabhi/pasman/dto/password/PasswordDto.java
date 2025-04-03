package com.aabhi.pasman.dto.password;

public class PasswordDto {
    private String password;
    private String username;
    private String url;
    private String passwordId;
    private String userId;

    public PasswordDto(String password, String username, String url, String passwordId, String userId) {
        this.password = password;
        this.username = username;
        this.url = url;
        this.passwordId = passwordId;
        this.userId = userId;
    }

    public PasswordDto() {
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPasswordId() {
        return this.passwordId;
    }

    public void setPasswordId(String passwordId) {
        this.passwordId = passwordId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PasswordDto password(String password) {
        this.password = password;
        return this;
    }

    public PasswordDto username(String username) {
        this.username = username;
        return this;
    }

    public PasswordDto url(String url) {
        this.url = url;
        return this;
    }

    public PasswordDto passwordId(String passwordId) {
        this.passwordId = passwordId;
        return this;
    }

    public PasswordDto userId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " password='" + getPassword() + "'" +
            ", username='" + getUsername() + "'" +
            ", url='" + getUrl() + "'" +
            ", passwordId='" + getPasswordId() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
