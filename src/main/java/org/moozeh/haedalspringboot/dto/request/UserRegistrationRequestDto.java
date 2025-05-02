package org.moozeh.haedalspringboot.dto.request;

import lombok.Getter;

@Getter
public class UserRegistrationRequestDto {
    private String username;
    private String password;
    private String name;
}
