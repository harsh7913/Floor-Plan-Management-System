package com.fpms.FloorPlanManagementSystem.service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.fpms.FloorPlanManagementSystem.repository.FloorPlanRepository;
import com.fpms.FloorPlanManagementSystem.model.FloorPlanEntity;
import com.fpms.FloorPlanManagementSystem.model.FloorDTO;
import com.fpms.FloorPlanManagementSystem.model.RoomDTO;

@Service
public class FloorPlanService {

    private final FloorPlanRepository floorPlanRepository;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper for JSON processing

    public FloorPlanService(FloorPlanRepository floorPlanRepository, ObjectMapper objectMapper) {
        this.floorPlanRepository = floorPlanRepository;
        this.objectMapper = objectMapper;
    }

    // Updated to assign room IDs if missing
        // Updated to assign room IDs if missing and set isAvailable to true
        public void updateFloorPlan(String username, String version, List<FloorDTO> updatedFloorDTOs) {
            if (version == null || version.isEmpty()) {
                throw new IllegalArgumentException("Version must be provided for the floor plan.");
            }
    
            // Create or update the FloorPlanEntity
            FloorPlanEntity floorPlanEntity = new FloorPlanEntity();
            floorPlanEntity.setUsername(username);
            floorPlanEntity.setVersion(version);  // Ensure the version is assigned
    
            // Iterate over floors and assign room IDs, set isAvailable to true
            for (FloorDTO floor : updatedFloorDTOs) {
                for (RoomDTO room : floor.getRooms()) {
                    if (room.getRoomId() == null || room.getRoomId().isEmpty()) {
                        String newRoomId = generateRoomId(floor, room);
                        room.setRoomId(newRoomId);
                    }
                    room.setAvailable(true);  // Set isAvailable to true when updating
                }
            }
    
            // Convert to JSON and save the updated floor plan
            floorPlanEntity.setFloorPlanData(mapToJSON(updatedFloorDTOs));
            floorPlanRepository.save(floorPlanEntity);  // Save with assigned version
        }
    
        // Helper method to generate a unique room ID
        private String generateRoomId(FloorDTO floor, RoomDTO room) {
            return "FL-" + floor.getFloorNumber() + "-RM-" + System.currentTimeMillis();
        }

    public List<FloorDTO> getFloorPlans(String username, String version) {
        Optional<FloorPlanEntity> floorPlanEntityOptional =
                floorPlanRepository.findByVersion(version);

        return floorPlanEntityOptional.map(entity -> mapToDTO(entity.getFloorPlanData()))
                .orElse(null);
    }

    // New method: Get recommended rooms based on participants and last booked room proximity
    public List<RoomDTO> getRecommendedRooms(int participants, String lastRoomId) {
        List<FloorDTO> floorPlans = getAllFloorPlans(); // Fetch all floor plans from the database
        List<RoomDTO> recommendedRooms = new ArrayList<>();

        for (FloorDTO floor : floorPlans) {
            for (RoomDTO room : floor.getRooms()) {
                if (room.isAvailable() && room.getCapacity() >= participants) {
                    recommendedRooms.add(room);
                }
            }
        }

        // Sort rooms by proximity to last booked room and booking weightage
        recommendedRooms = recommendedRooms.stream()
                .sorted(Comparator.comparingInt((RoomDTO r) -> r.getRoomId().equals(lastRoomId) ? 0 : 1)
                        .thenComparingInt(RoomDTO::getBookingWeightage).reversed())
                .collect(Collectors.toList());

        return recommendedRooms;
    }

    // New method: Book a room based on room ID and participants count
    public String bookRoom(String roomId, int participants) {
        try {
            List<FloorDTO> floorPlans = getAllFloorPlans(); // Fetch all floor plans from the database
            System.out.println("Booking request received for Room ID: " + roomId + " with participants: " + participants);
    
            for (FloorDTO floor : floorPlans) {
                // System.out.print(floor);
                for (RoomDTO room : floor.getRooms()) {
                    System.out.println("Checking Room ID: " + room.getRoomId());
    
                    if (room.getRoomId() != null && room.getRoomId().equals(roomId)) {
                        if (room.isAvailable() && room.getCapacity() >= participants) {
                            room.setAvailable(false); // Mark room as unavailable
                            room.setBookingWeightage(room.getBookingWeightage() + 1); // Increment booking weightage
    
                            // Ensure the correct version is passed while updating the floor plan
                            String version = floor.getVersion(); // Get the version from the floor
                            if (version == null || version.isEmpty()) {
                                return "Error: Floor plan version is missing.";
                            }
    
                            // Save the updated floor plan to the database
                            updateFloorPlan(floor.getUsername(), version, floorPlans);
                            return "Room " + room.getRoomName() + " booked successfully.";
                        } else if (!room.isAvailable()) {
                            return "Room " + room.getRoomName() + " is already booked.";
                        } else if (room.getCapacity() < participants) {
                            return "Room " + room.getRoomName() + " does not have enough capacity.";
                        }
                    }
                }
            }
    
            return "Room with ID " + roomId + " not found."; // If room not found
    
        } catch (Exception e) {
            // Log the error and return a meaningful error message
            System.out.println("Error while booking room: " + e.getMessage());
            e.printStackTrace();
            return "An error occurred while trying to book the room. Please try again.";
        }
    }

    // Helper method to fetch all floor plans (could be optimized based on requirements)
    private List<FloorDTO> getAllFloorPlans() {
        List<FloorPlanEntity> entities = floorPlanRepository.findAll();
        List<FloorDTO> floorPlans = new ArrayList<>();

        for (FloorPlanEntity entity : entities) {
            floorPlans.addAll(mapToDTO(entity.getFloorPlanData()));
        }

        return floorPlans;
    }

    private String mapToJSON(List<FloorDTO> floorDTOs) {
        try {
            return objectMapper.writeValueAsString(floorDTOs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize floor plan data to JSON", e);
        }
    }

    private List<FloorDTO> mapToDTO(String json) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, FloorDTO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize floor plan data from JSON", e);
        }
    }
}
