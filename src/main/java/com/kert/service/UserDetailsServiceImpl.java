package com.kert.service;

import com.kert.model.User;
import com.kert.repository.UserRepository;
import com.kert.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isAdmin = adminRepository.findById(user.getStudentId()).isPresent();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getStudentId().toString())
                .password(user.getHash())
                .roles(isAdmin ? "ADMIN" : "USER")
                .build();
    }
}
