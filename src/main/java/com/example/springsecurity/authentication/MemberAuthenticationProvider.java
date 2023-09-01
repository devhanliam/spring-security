package com.example.springsecurity.authentication;

import com.example.springsecurity.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = authService.loadUserByUsername(authentication.getName());

        if (passwordEncoder.matches((String) authentication.getCredentials(), userDetails.getPassword())) {
            return new MemberToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        }

        throw new BadCredentialsException("인증정보 미일치");
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return MemberToken.class.isAssignableFrom(authentication);
    }
}
