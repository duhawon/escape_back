package com.untitled.escape.domain.room.controller;
import com.untitled.escape.domain.room.dto.RoomDetailResponse;
import com.untitled.escape.domain.room.dto.RoomSummaryResponse;
import com.untitled.escape.domain.room.service.RoomService;
import com.untitled.escape.global.dto.response.SliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public SliceResponse<RoomSummaryResponse> getRooms(@RequestParam(required = false) String query,
                                                       @PageableDefault(size=10) Pageable pageable) {
        return SliceResponse.from(roomService.getRooms(query, pageable));
    }

    @GetMapping("/{roomId}")
    public RoomDetailResponse getRoom(@PathVariable Long roomId) {
        return roomService.getRoom(roomId);
    }
}
