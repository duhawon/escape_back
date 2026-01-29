package com.untitled.escape.domain.user.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    private String name;
    private String email;
    private String password;
}
