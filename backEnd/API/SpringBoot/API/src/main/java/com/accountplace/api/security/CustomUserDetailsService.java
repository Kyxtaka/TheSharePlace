package com.accountplace.api.security;

import com.accountplace.api.entity.RoleEntity;
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
 * This class is responsible for loading user details by identifier or email and mapping roles to authorities.
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
     * Loads user details by identifier or email.
     * It checks if the provided identifier is an email or a identifier and fetches the user accordingly.
     *
     * @param identifier The identifier or email of the user.
     * @return A UserDetails object containing the user's details.
     * @throws UsernameNotFoundException If no user is found with the provided identifier or email.
     */
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Email tmpEmail = new Email(identifier);
        boolean isEmail = tmpEmail.isValid();
        UserEntity user = null;

        // Fetch user by email if valid, otherwise by identifier
        if (isEmail) {
            user = userRepository.findByEmail(identifier).orElseThrow(
                    () -> new UsernameNotFoundException("identifier or email not found"));
        } else {
            user = userRepository.findByUsername(identifier).orElseThrow(
                    () -> new UsernameNotFoundException("identifier or email not found"));
        }

        // Return a UserDetails object with the user’s credentials and authorities
        return new User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoleEntities()));
    }

    /**
     * Maps the list of roles to a collection of GrantedAuthority.
     * Each role is prefixed with "ROLE_" as per Spring Security conventions.
     *
     * @param roleEntities The list of roles assigned to the user.
     * @return A collection of GrantedAuthority representing the user's roles.
     */
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<RoleEntity> roleEntities) {
        return roleEntities.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }
}
