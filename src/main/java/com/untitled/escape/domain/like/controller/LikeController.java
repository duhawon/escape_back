package com.untitled.escape.domain.like.controller;

import com.untitled.escape.domain.like.TargetType;
import com.untitled.escape.domain.like.dto.LikeRequest;
import com.untitled.escape.domain.like.dto.LikeResponse;
import com.untitled.escape.domain.like.service.LikeService;
import com.untitled.escape.domain.user.dto.UserSummary;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public List<UserSummary> getLikeUsers(@RequestParam TargetType targetType,
                                          @RequestParam Long targetId) {
        return likeService.getLikeUsers(targetType, targetId);
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
