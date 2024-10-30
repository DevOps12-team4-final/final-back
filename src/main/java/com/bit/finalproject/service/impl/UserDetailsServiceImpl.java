package com.bit.finalproject.service.impl;

import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
// Spring Security에서 제공하는 UserDetailsService 인터페이스를 구현하여,
// 사용자 인증시 DB에서 사용자 정보를 가져오는 역할을 한다.
// UserRepository를 이용해 Email으로 사용자를 조회하고, CustomUserDetails객체를 반환한다.
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    // Spring Security가 사용자 인증을 수행할 때 호출된다. (로그아웃)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException  {

        // Email을 기반으로 사용자를 조회하고, 없으면 예외를 던진다.
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("user not exist")
        );

        return CustomUserDetails.builder()
                .user(user)
                .build();
    }
}
