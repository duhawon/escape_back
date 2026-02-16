package com.untitled.escape.domain.room.service;

import com.untitled.escape.domain.review.repository.ReviewRepository;
import com.untitled.escape.domain.room.Room;
import com.untitled.escape.domain.room.dto.RoomDetailResponse;
import com.untitled.escape.domain.room.dto.RoomSummaryResponse;
import com.untitled.escape.domain.room.repository.RoomRepository;
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
    public List<RoomSummaryResponse> getRooms(String query) {
        String normalizedQuery =
                StringUtils.hasText(query) ? query : null;

        return roomRepository.findRoomSummaries(normalizedQuery)
                .stream()
                .map(RoomSummaryResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDetailResponse getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 방입니다."));
        Double avg = reviewRepository.findAverageRatingByRoomId(roomId);
        BigDecimal rating = (avg == null)
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP);

        return RoomDetailResponse.from(room, rating);
    }

}
