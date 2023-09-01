package com.example.springsecurity.authentication;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MemberToken extends UsernamePasswordAuthenticationToken {
    public MemberToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MemberToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
