package com.kert.controller;

import com.kert.dto.PasswordDTO;
import com.kert.service.PasswordService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passwords")
public class PasswordController {
    private final PasswordService passwordService;

    @PutMapping("/{user_id}")
    public ResponseEntity<Void> updatePassword(@PathVariable("user_id") Long userId, @RequestBody PasswordDTO passwordDTO) {
        passwordService.updatePassword(userId, passwordDTO);
        return ResponseEntity.noContent().build();
    }
}
