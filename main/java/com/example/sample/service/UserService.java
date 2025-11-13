package com.example.sample.service;

import com.example.sample.entity.User;
import com.example.sample.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
@SuppressWarnings("unused")
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    // Required methods for AuthController and PortfolioController
    @SuppressWarnings("unused")
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @SuppressWarnings("unused")
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @SuppressWarnings("unused")
    public User save(User user) {
        return userRepository.save(user);
    }

    // Additional useful methods
    @SuppressWarnings("unused")
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @SuppressWarnings("unused")
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}