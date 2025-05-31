package com.on_bapsang.backend.security;

import com.on_bapsang.backend.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 권한 미사용 시 null 또는 빈 리스트 반환
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // User 엔티티의 비밀번호
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // User 엔티티의 로그인 식별자
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
