package com.untitled.escape.domain.user.controller;

import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.*;
import com.untitled.escape.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PutMapping("/me/profile-img")
    public ResponseEntity<Void> updateProfileImage(@RequestBody UpdateProfileImageRequest request) {
        userService.updateMyProfileImage(request.getProfileImgKey());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile/me")
    public ResponseEntity<MyProfileResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }
}
