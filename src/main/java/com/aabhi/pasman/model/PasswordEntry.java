package com.aabhi.pasman.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PasswordEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String username;
    private String password;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
