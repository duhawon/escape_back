package com.untitled.escape.auth.mapper;

import com.untitled.escape.auth.dto.ReissueResponseDto;
import com.untitled.escape.auth.dto.ReissueResultDto;
import com.untitled.escape.auth.dto.SignInResponseDto;
import com.untitled.escape.auth.dto.SignInResultDto;
import com.untitled.escape.domain.user.User;
import com.untitled.escape.global.s3.S3UrlResolver;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseMapper {
    private final S3UrlResolver s3UrlResolver;

    public AuthResponseMapper(S3UrlResolver s3UrlResolver) {
        this.s3UrlResolver = s3UrlResolver;
    }
    public SignInResponseDto toSignInResponse(SignInResultDto result) {
        User u = result.getUser();
        return new SignInResponseDto(
                u.getId(),
                u.getEmail(),
                u.getName(),
                s3UrlResolver.toPublicUrl(u.getProfileImgKey())
        );
    }

    public ReissueResponseDto toReissueResponse(ReissueResultDto result) {
        User u = result.getUser();
        return new ReissueResponseDto(
                u.getId(),
                u.getEmail(),
                u.getName(),
                s3UrlResolver.toPublicUrl(u.getProfileImgKey())
        );
    }
}
