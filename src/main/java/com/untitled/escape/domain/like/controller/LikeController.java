package com.untitled.escape.domain.like.controller;

import com.untitled.escape.domain.like.TargetType;
import com.untitled.escape.domain.like.dto.LikeRequest;
import com.untitled.escape.domain.like.dto.LikeResponse;
import com.untitled.escape.domain.like.service.LikeService;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.global.dto.response.SliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public SliceResponse<UserSummary> getLikeUsers(@RequestParam TargetType targetType,
                                                   @RequestParam Long targetId,
                                                   @PageableDefault(size=10) Pageable pageable) {
        return SliceResponse.from(likeService.getLikeUsers(targetType, targetId, pageable));
    }

    @PostMapping
    public LikeResponse like(@RequestBody LikeRequest likeRequest) {
        return likeService.like(likeRequest.getTargetType(), likeRequest.getTargetId());

    }

    @DeleteMapping
    public LikeResponse unlike(@RequestBody LikeRequest likeRequest) {
        return likeService.unlike(likeRequest.getTargetType(), likeRequest.getTargetId());
    }
}
