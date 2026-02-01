package com.untitled.escape.domain.user.controller;

import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.SignUpRequest;
import com.untitled.escape.domain.user.dto.SignUpResponse;
import com.untitled.escape.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody final SignUpRequest signUpRequest) {
        User user = userService.signUp(signUpRequest);
        SignUpResponse signUpResponse = SignUpResponse.builder().userId(user.getId()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponse);
    }
}
