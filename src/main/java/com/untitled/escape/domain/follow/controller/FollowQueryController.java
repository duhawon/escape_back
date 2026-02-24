package com.untitled.escape.domain.follow.controller;

import com.untitled.escape.domain.follow.dto.FollowCountResponse;
import com.untitled.escape.domain.follow.service.FollowService;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.global.dto.response.SliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/follows")
public class FollowQueryController {
    private final FollowService followService;

    public FollowQueryController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping("/followings")
    // DESC : 내가 팔로우 한 사람들
    public SliceResponse<UserSummary> getFollowings(@PathVariable UUID userId,
                                                    @PageableDefault(size = 10) Pageable pageable) {
        return SliceResponse.from(followService.getFollowings(userId, pageable));
    }

    @GetMapping("/followers")
    // DESC : 나를 팔로우 하는 사람들
    public SliceResponse<UserSummary> getFollowers(@PathVariable UUID userId,
                                          @PageableDefault(size = 10) Pageable pageable) {
        return SliceResponse.from(followService.getFollowers(userId, pageable));
    }

    @GetMapping("/count")
    public ResponseEntity<FollowCountResponse> getFollowCounts(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(followService.getFollowCounts(userId));
    }
}
