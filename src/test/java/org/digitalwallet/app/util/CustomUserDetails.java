package org.digitalwallet.app.util;

import org.digitalwallet.app.repository.model.UserAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final UserAuth userAuth;

    public CustomUserDetails(UserAuth userAuth) {
        this.userAuth = userAuth;
    }

    public Long getId() {
        return userAuth.getId();
    }

    @Override
    public String getUsername() {
        return userAuth.getUsername();
    }

    @Override
    public String getPassword() {
        return userAuth.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userAuth.getRole()));
    }
}