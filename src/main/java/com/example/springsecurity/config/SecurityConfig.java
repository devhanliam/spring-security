package com.example.springsecurity.config;

import com.example.springsecurity.authentication.provider.JwtAuthenticationProvider;
import com.example.springsecurity.authentication.provider.MemberAuthenticationProvider;
import com.example.springsecurity.service.AuthService;
import com.example.springsecurity.service.JwtService;
import com.example.springsecurity.exceptionhanlder.CustomAuthenticationEntryPoint;
import com.example.springsecurity.exceptionhanlder.CustomAccessDeniedHandler;
import com.example.springsecurity.filter.JwtAuthenticationFilter;
import com.example.springsecurity.filter.ExceptionHandlingFilter;
import com.example.springsecurity.filter.MemberAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final AuthService authService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler deniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers().antMatchers("/favicon.ico","/error")
                .requestMatchers(toH2Console());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/api/v1/test")
                .hasRole("USER")
                .antMatchers("/api/v1/admin")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/main")
                .anonymous()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(exceptionHandlingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(memberAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(deniedHandler);
        return http.build();
    }

    @Bean
    public MemberAuthenticationFilter memberAuthenticationFilter() {
        return new MemberAuthenticationFilter(objectMapper, memberAuthenticationProvider(), jwtService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtAuthenticationProvider());
    }

    @Bean
    public ExceptionHandlingFilter exceptionHandlingFilter() {
        return new ExceptionHandlingFilter(authenticationEntryPoint);
    }

    @Bean
    public AuthenticationManager memberAuthenticationProvider() {
        MemberAuthenticationProvider memberAuthenticationProvider = new MemberAuthenticationProvider(passwordEncoder(), authService);
        return new ProviderManager(memberAuthenticationProvider);
    }

    @Bean
    public AuthenticationManager jwtAuthenticationProvider() {
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtService);
        return new ProviderManager(jwtAuthenticationProvider);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
