package org.moozeh.haedalspringboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.dto.request.UserUpdateRequestDto;
import org.moozeh.haedalspringboot.dto.response.UserDetailResponseDto;
import org.moozeh.haedalspringboot.dto.response.UserSimpleResponseDto;
import org.moozeh.haedalspringboot.service.AuthService;
import org.moozeh.haedalspringboot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/users") // 유저 정보 간략 조회
    public ResponseEntity<List<UserSimpleResponseDto>> getUsers(@RequestParam(required = false)String username, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        List<UserSimpleResponseDto> users;
        if (username == null || username.isEmpty()) {
            users = userService.getAllUsers(currentUser);
        } else {
            users = userService.getUserByUsername(currentUser, username);
        }

        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/profile") // 유저 정보 수정 (이미지 제외)
    public ResponseEntity<UserDetailResponseDto> updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        UserDetailResponseDto updated = userService.updateUser(currentUser, userUpdateRequestDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/users/{userId}/profile") // 유저 정보 상세 조회
    public ResponseEntity<UserDetailResponseDto>
    getUserProfile(@PathVariable Long userId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        UserDetailResponseDto userDetailResponseDto = userService.getUserDetail(currentUser, userId);

        return ResponseEntity.ok(userDetailResponseDto);
    }
}
