package com.untitled.escape.domain.user.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
}
