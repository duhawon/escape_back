package com.untitled.escape.domain.upload.service;

import com.untitled.escape.domain.upload.dto.PresignRequest;
import com.untitled.escape.domain.upload.dto.PresignResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
public class S3PresignService {
    private final S3Presigner presigner;
    private final String bucket;
    private final String publicBaseUrl;

    public S3PresignService(
            S3Presigner presigner,
            @Value("${aws.s3.bucket}") String bucket,
            @Value("${aws.s3.public-base-url}") String publicBaseUrl) {
        this.presigner = presigner;
        this.bucket = bucket;
        this.publicBaseUrl = publicBaseUrl;
    }

    public PresignResponse presignRoomPoster(PresignRequest req) {
        String ext = guessExt(req.contentType(), req.originalFileName());
        String key = "rooms/posters/" + UUID.randomUUID() + ext;
        return presignPut(key, req.contentType());
    }

    public PresignResponse presignUserProfile(PresignRequest req) {
        String ext = guessExt(req.contentType(), req.originalFileName());
        String key = "users/profiles/" + UUID.randomUUID() + ext;
        return presignPut(key, req.contentType());
    }

    private PresignResponse presignPut(String key, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        String uploadUrl = presigner.presignPutObject(presignRequest).url().toString();
        String publicUrl = publicBaseUrl + "/" + key;

        return new PresignResponse(uploadUrl, key, publicUrl);
    }

    private String guessExt(String contentType, String originalFileName) {
        if (contentType == null) return "";
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> {
                // contentType이 애매하면 파일명 확장자로 fallback(있을 때만)
                if (originalFileName != null && originalFileName.contains(".")) {
                    yield originalFileName.substring(originalFileName.lastIndexOf('.'));
                }
                yield "";
            }
        };
    }
}
