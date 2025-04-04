package com.aabhi.pasman.dto.password;

public class InsertPasswordDto {
    private String username;
    private String password;
    private String url;
    private String description;
    private Long userId;

    public InsertPasswordDto() {}

    public InsertPasswordDto(String username, String password, String url, String description, Long userId) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.description = description;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Long getUserId() {
        return userId;
    }
}
