package com.example.springsecurity.exception;

import org.springframework.security.core.AuthenticationException;

public class BadJwtException extends AuthenticationException {
    public BadJwtException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadJwtException(String msg) {
        super(msg);
    }
}
