package com.example.springsecurity.service;

import com.example.springsecurity.domain.entity.Member;
import com.example.springsecurity.domain.entity.SecurityMember;
import com.example.springsecurity.infra.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("인증정보 미일치"));

        return SecurityMember.withUsername(member.getEmail())
                .password(member.getPassword())
                .authorities(member.getRoles().getAuthority())
                .build();
    }
}
