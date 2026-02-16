package com.untitled.escape.domain.follow.controller;

import com.untitled.escape.domain.follow.service.FollowService;
import com.untitled.escape.domain.user.dto.UserSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/follows")
public class FollowQueryController {
    private final FollowService followService;

    public FollowQueryController(FollowService followService) {
        this.followService = followService;
    }
    @GetMapping("/followings")
    public List<UserSummary> getFollowings(@PathVariable UUID userId) {
        return followService.getFollowings(userId);
    }

    @GetMapping("/followers")
    public List<UserSummary> getFollowers(@PathVariable UUID userId) {
        return followService.getFollowers(userId);
    }
}
