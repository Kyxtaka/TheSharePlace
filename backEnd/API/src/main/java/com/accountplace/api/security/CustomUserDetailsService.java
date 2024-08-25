package com.accountplace.api.security;

import com.accountplace.api.entity.Role;
import com.accountplace.api.entity.UserEntity;
import com.accountplace.api.repositories.UserRepository;
import com.accountplace.api.tools.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Email tmpEmail = new Email(username);
        boolean isEmail = tmpEmail.isValid();
        UserEntity user = null;
        if (isEmail) {
             user = userRepository.findByEmail(username).orElseThrow( () -> new UsernameNotFoundException("username or email not found"));
        } else {
             user = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("username or email not found"));
        }
        return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName()))
                .collect(Collectors.toList());
    }
}
