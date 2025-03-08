package com.kert.service;

import com.kert.model.User;
import com.kert.repository.UserRepository;
import com.kert.dto.SignUpDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final long MAX_PROFILE_PICTURE_SIZE = 2 * 1024 * 1024;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long studentId) {
        return userRepository.findById(studentId).orElse(null);
    }

    private boolean isProfilePictureSizeValid(String profilePicture) {
        if (profilePicture == null) {
            return true;
        }
        byte[] profilePictureBytes = profilePicture.getBytes(StandardCharsets.UTF_8);
        return profilePictureBytes.length <= MAX_PROFILE_PICTURE_SIZE;
    }

    public User createUser(SignUpDTO signUpDTO) {
        if (!isProfilePictureSizeValid(signUpDTO.getProfilePicture())) {
            throw new IllegalArgumentException("프로필 사진 크기가 2MB를 초과합니다.");
        }

        User user = new User();
        user.setStudentId(signUpDTO.getStudentId());
        user.setEmail(signUpDTO.getEmail());
        user.setName(signUpDTO.getName());
        user.setProfilePicture(signUpDTO.getProfilePicture());
        user.setGeneration(signUpDTO.getGeneration());
        user.setMajor(signUpDTO.getMajor());
        user.setHash(passwordEncoder.encode(signUpDTO.getPassword()));

        if (userRepository.existsById(user.getStudentId())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        return userRepository.save(user);
    }

    public boolean login(Long studentId, String rawPassword) {
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자이거나 비밀번호가 틀렸습니다."));
        return passwordEncoder.matches(rawPassword, user.getHash());
    }

    public User updateUser(Long studentId, User userDetails) {
        User user = getUserById(studentId);

        if (user != null) {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setProfilePicture(userDetails.getProfilePicture());
            user.setGeneration(userDetails.getGeneration());
            user.setMajor(userDetails.getMajor());
            return userRepository.save(user);
        }

        return null;
    }

    public void deleteUser(Long studentId) {
        userRepository.deleteById(studentId);
    }
}