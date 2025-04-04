package com.aabhi.pasman.dto.password;

public class ReturnPasswordDto {
    private String username;
    private String password;
    private String url;
    private String description;

    public ReturnPasswordDto() {
    }

    public ReturnPasswordDto(String username, String password, String url, String description) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.description = description;
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
}
