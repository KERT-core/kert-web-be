package com.kert.service;

import com.kert.dto.PasswordDTO;
import com.kert.model.User;
import com.kert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User updatePassword(Long userId, PasswordDTO passwordDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 변경을 진행할 수 없습니다"));
        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 변경을 진행할 수 없습니다");
        }
        user.setHash(passwordEncoder.encode(passwordDTO.getPassword()));
        return userRepository.save(user);
    }
}
