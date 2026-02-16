package com.untitled.escape.domain.room.service;

import com.untitled.escape.domain.room.dto.RoomDetailResponse;
import com.untitled.escape.domain.room.dto.RoomSummaryResponse;

import java.util.List;

public interface RoomService {
    List<RoomSummaryResponse> getRooms(String query);
    RoomDetailResponse getRoom(Long roomId);
}
