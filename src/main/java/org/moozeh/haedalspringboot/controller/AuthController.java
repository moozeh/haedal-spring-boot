package org.moozeh.haedalspringboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.dto.request.LoginRequestDto;
import org.moozeh.haedalspringboot.dto.request.UserRegistrationRequestDto;
import org.moozeh.haedalspringboot.dto.response.UserSimpleResponseDto;
import org.moozeh.haedalspringboot.service.AuthService;
import org.moozeh.haedalspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserSimpleResponseDto> registerUser(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
        User user = new User(
            userRegistrationRequestDto.getUsername(),
            userRegistrationRequestDto.getPassword(),
            userRegistrationRequestDto.getName()
        );

        UserSimpleResponseDto savedUser = userService.saveUser(user);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserSimpleResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        UserSimpleResponseDto userSimpleResponseDto = authService.login(loginRequestDto, request);

        return ResponseEntity.ok(userSimpleResponseDto);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logoutUser(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserSimpleResponseDto> me(HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        UserSimpleResponseDto userSimpleResponseDto = userService.convertUserToSimpleDto(currentUser, currentUser);

        return ResponseEntity.ok(userSimpleResponseDto);
    }
}
