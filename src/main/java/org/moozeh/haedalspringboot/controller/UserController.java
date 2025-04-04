package org.moozeh.haedalspringboot.controller;

import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.dto.UserRegistrationRequestDto;
import org.moozeh.haedalspringboot.dto.UserSimpleResponseDto;
import org.moozeh.haedalspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
}
