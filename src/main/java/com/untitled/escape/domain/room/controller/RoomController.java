package com.untitled.escape.domain.room.controller;
import com.untitled.escape.domain.room.dto.RoomDetailResponse;
import com.untitled.escape.domain.room.dto.RoomSummaryResponse;
import com.untitled.escape.domain.room.service.RoomService;
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
    public List<RoomSummaryResponse> getRooms(@RequestParam(required = false) String query) {
        return roomService.getRooms(query);
    }

    @GetMapping("/{roomId}")
    public RoomDetailResponse getRoom(@PathVariable Long roomId) {
        return roomService.getRoom(roomId);
    }
}
