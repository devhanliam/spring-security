package com.example.springsecurity.domain.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER(ROLES.USER, "유저권한"),
    ADMIN(ROLES.ADMIN, "어드민권한");

    public static class ROLES{
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

    private String authority;
    private String description;

    private Role(String authority, String description){
        this.authority = authority;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    @Override
    public String getAuthority() {
        return authority;
    }

}
