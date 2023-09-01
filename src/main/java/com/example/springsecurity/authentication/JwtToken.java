package com.example.springsecurity.authentication;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtToken {
    private String accessToken;
    private String refreshToken;
    private String grantType;
}
