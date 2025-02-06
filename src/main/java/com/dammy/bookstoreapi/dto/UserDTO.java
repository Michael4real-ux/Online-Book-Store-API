package com.dammy.bookstoreapi.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
    private final Long id;
    private final String username;
    private final String name;
    private final String role;

    @JsonCreator
    public UserDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("name") String name,
            @JsonProperty("role") String role
    ) {
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