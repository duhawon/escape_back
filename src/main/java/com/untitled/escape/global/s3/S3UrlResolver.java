package com.untitled.escape.global.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3UrlResolver {
    private final String publicBaseUrl;

    public S3UrlResolver(@Value("${aws.s3.public-base-url}") String publicBaseUrl) {
        this.publicBaseUrl = publicBaseUrl;
    }

    public String toPublicUrl(String key) {
        if (key == null || key.isBlank()) return null;
        return publicBaseUrl + "/" + key;
    }
}
