package com.fpms.FloorPlanManagementSystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.fpms.FloorPlanManagementSystem.service.FloorPlanService;
import com.fpms.FloorPlanManagementSystem.model.FloorDTO;
import com.fpms.FloorPlanManagementSystem.model.UpdateRequestDTO;
import com.fpms.FloorPlanManagementSystem.model.RoomDTO;
import com.fpms.FloorPlanManagementSystem.model.MeetingRequestDTO;

@RestController
@RequestMapping("/api/floor-plan")
public class FloorPlanController {

    private final FloorPlanService floorPlanService;

    public FloorPlanController(FloorPlanService floorPlanService) {
        this.floorPlanService = floorPlanService;
    }

    // Existing methods
    @PostMapping("/update")
    public ResponseEntity<String> updateFloorPlan(@RequestBody UpdateRequestDTO updateRequestDTO) {
        if (updateRequestDTO.getUsername().equals("admin")) {
            floorPlanService.updateFloorPlan(updateRequestDTO.getUsername(), updateRequestDTO.getVersion(), updateRequestDTO.getFloorDTOs());
            return ResponseEntity.ok("Floor plan updated successfully");
        } else {
            return ResponseEntity.ok("Only admin user can update the floor plan");
        }
    }

    @GetMapping
    public ResponseEntity<List<FloorDTO>> getFloorPlans(
            @RequestParam String username,
            @RequestParam String version) {
        List<FloorDTO> floorDTOs = floorPlanService.getFloorPlans(username, version);
        return ResponseEntity.ok(floorDTOs);
    }

    // New method to recommend rooms based on participants and last booked room
    @GetMapping("/recommend-rooms")
    public ResponseEntity<List<RoomDTO>> recommendRooms(
            @RequestParam int participants,
            @RequestParam(required = false) String lastRoomId) {
        List<RoomDTO> recommendedRooms = floorPlanService.getRecommendedRooms(participants, lastRoomId);
        return ResponseEntity.ok(recommendedRooms);
    }

    // New method to book a room
    @PostMapping("/book-room")
    public ResponseEntity<String> bookRoom(@RequestBody MeetingRequestDTO meetingRequestDTO) {
        System.out.print(meetingRequestDTO);
        String result = floorPlanService.bookRoom(meetingRequestDTO.getRoomId(), meetingRequestDTO.getParticipants());
        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
