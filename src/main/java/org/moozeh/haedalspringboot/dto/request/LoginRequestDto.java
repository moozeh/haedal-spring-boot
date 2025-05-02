package org.moozeh.haedalspringboot.dto.request;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String username;
    private String password;
}
