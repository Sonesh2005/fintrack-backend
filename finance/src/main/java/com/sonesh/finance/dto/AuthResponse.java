package com.sonesh.finance.dto;

public class AuthResponse {
    public String token;
    public Long userId;
    public String name;
    public String email;

    public AuthResponse(String token, Long userId, String name, String email) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}