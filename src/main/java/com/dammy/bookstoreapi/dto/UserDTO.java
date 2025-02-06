package com.dammy.bookstoreapi.dto;

public class UserDTO {
    private final Long id;
    private final String username;
    private final String name;
    private final String role;

    public UserDTO(Long id, String username, String name, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getRole() { return role; }
}