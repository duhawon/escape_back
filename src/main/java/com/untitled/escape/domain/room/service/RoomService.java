package com.untitled.escape.domain.room.service;

import com.untitled.escape.domain.room.dto.RoomDetailResponse;
import com.untitled.escape.domain.room.dto.RoomSummaryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RoomService {
    Slice<RoomSummaryResponse> getRooms(String query, Pageable pageable);
    RoomDetailResponse getRoom(Long roomId);
}
