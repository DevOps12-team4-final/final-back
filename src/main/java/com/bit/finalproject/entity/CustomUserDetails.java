package com.bit.finalproject.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// UserDetails 인터페이스를 구현하여, Spring Security가 사용자 인증을 처리할 수 있도록 사용자 정보를 제공한다.
// User 객체를 이용해 Email, password, authorities(권한)정보를 UserDetails로 변환해준다.
public class CustomUserDetails implements UserDetails {
    private User user;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();

        auths.add(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        return auths;
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 이메일을 username으로 반환
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
}
