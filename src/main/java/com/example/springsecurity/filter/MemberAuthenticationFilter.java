package com.example.springsecurity.filter;

import com.example.springsecurity.authentication.JwtToken;
import com.example.springsecurity.authentication.MemberToken;
import com.example.springsecurity.service.JwtService;
import com.example.springsecurity.web.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class MemberAuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws  IOException {
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        MemberToken memberToken = new MemberToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(memberToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        JwtToken jwtToken = jwtService.generateToken(authenticate);
        response.setHeader(AUTHORIZATION_HEADER,jwtToken.getGrantType()+" "+jwtToken.getAccessToken());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)  {
        return !request.getServletPath().equals("/login");
    }
}
