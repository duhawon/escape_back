package com.untitled.escape.domain.follow.controller;

import com.untitled.escape.domain.follow.service.FollowService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{targetUserId}/follows")
public class FollowCommandController {
    private final FollowService followService;

    public FollowCommandController(FollowService followService) {
        this.followService = followService;
    }
    @PostMapping
    public void follow(@PathVariable UUID targetUserId) {
        followService.follow(targetUserId);
    }

    @DeleteMapping
    public void unfollow(@PathVariable UUID targetUserId) {
        followService.unfollow(targetUserId);
    }
}
