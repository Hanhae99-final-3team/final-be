package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(memberEmail);
        return member
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("아이디를 찾을 수 없습니다."));
    }
}
