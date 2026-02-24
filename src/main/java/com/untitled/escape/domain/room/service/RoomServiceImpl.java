package com.untitled.escape.domain.room.service;

import com.untitled.escape.domain.review.repository.ReviewRepository;
import com.untitled.escape.domain.room.Room;
import com.untitled.escape.domain.room.dto.RoomDetailResponse;
import com.untitled.escape.domain.room.dto.RoomSummaryResponse;
import com.untitled.escape.domain.room.repository.RoomRepository;
import com.untitled.escape.global.exception.CustomException;
import com.untitled.escape.global.exception.code.RoomErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ReviewRepository reviewRepository;

    public RoomServiceImpl(RoomRepository roomRepository, ReviewRepository reviewRepository) {
        this.roomRepository = roomRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<RoomSummaryResponse> getRooms(String query, Pageable pageable) {
        String normalizedQuery =
                StringUtils.hasText(query) ? query : null;

        return roomRepository.findRoomSummaries(normalizedQuery, pageable)
                .map(RoomSummaryResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDetailResponse getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
        Double avg = reviewRepository.findAverageRatingByRoomId(roomId);
        BigDecimal rating = (avg == null)
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP);

        return RoomDetailResponse.from(room, rating);
    }

}
