package com.num.digital_ticket.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户详情
 */
@Data
public class RedefinedUserDetails implements UserDetails {
    private User user;
    private List<String> permissions;
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;

    public RedefinedUserDetails(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public RedefinedUserDetails() {
    }

    /**
     * 返回用户的权限集合
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities.stream()
//                .filter(grantedAuthority -> grantedAuthority.getAuthority() != null)
//                .map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority()))
//                .collect(Collectors.toList());
        if (authorities != null) {
            return authorities;
        }
        authorities = this.permissions
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        System.out.println(user);
        return user.getUsername();
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
        return Boolean.TRUE;
    }
}
