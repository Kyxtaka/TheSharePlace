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

/**
 * Custom implementation of the Spring Security UserDetailsService interface.
 * This class is responsible for loading user details by username or email and mapping roles to authorities.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor for CustomUserDetailsService.
     * @param userRepository The UserRepository used to retrieve user information from the database.
     */
    @Autowired
    private CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by username or email.
     * It checks if the provided username is an email or a username and fetches the user accordingly.
     *
     * @param username The username or email of the user.
     * @return A UserDetails object containing the user's details.
     * @throws UsernameNotFoundException If no user is found with the provided username or email.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Email tmpEmail = new Email(username);
        boolean isEmail = tmpEmail.isValid();
        UserEntity user = null;

        // Fetch user by email if valid, otherwise by username
        if (isEmail) {
            user = userRepository.findByEmail(username).orElseThrow(
                    () -> new UsernameNotFoundException("username or email not found"));
        } else {
            user = userRepository.findByUsername(username).orElseThrow(
                    () -> new UsernameNotFoundException("username or email not found"));
        }

        // Return a UserDetails object with the user’s credentials and authorities
        return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    /**
     * Maps the list of roles to a collection of GrantedAuthority.
     * Each role is prefixed with "ROLE_" as per Spring Security conventions.
     *
     * @param roles The list of roles assigned to the user.
     * @return A collection of GrantedAuthority representing the user's roles.
     */
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }
}
