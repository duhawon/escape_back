package com.untitled.escape.domain.like.dto;

import com.untitled.escape.domain.user.dto.UserSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikerResponse {
    private UserSummary userSummary;
    private boolean followed ;

    public static LikerResponse of(UserSummary userSummary, boolean contains) {
        return new LikerResponse(userSummary, contains);
    }
}
