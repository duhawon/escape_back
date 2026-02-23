package com.untitled.escape.domain.upload.controller;

import com.untitled.escape.domain.upload.dto.PresignRequest;
import com.untitled.escape.domain.upload.dto.PresignResponse;
import com.untitled.escape.domain.upload.service.S3PresignService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
public class UploadController {
    private final S3PresignService s3PresignService;

    public UploadController(S3PresignService s3PresignService) {
        this.s3PresignService = s3PresignService;
    }

    @PostMapping("/rooms/poster/presign")
    public PresignResponse presignRoomPoster(@RequestBody PresignRequest request) {
        return s3PresignService.presignRoomPoster(request);
    }

    @PostMapping("/users/profile/presign")
    public PresignResponse presignUserProfile(@RequestBody PresignRequest request) {
        return s3PresignService.presignUserProfile(request);
    }
}
