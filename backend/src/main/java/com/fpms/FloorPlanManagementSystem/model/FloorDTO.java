package com.fpms.FloorPlanManagementSystem.model;

import java.util.List;

public class FloorDTO {
    private int floorNumber;
    private List<RoomDTO> rooms;
    private String username;
    private String version;

    // Constructor
    public FloorDTO() {}

    public FloorDTO(int floorNumber, List<RoomDTO> rooms, String username, String version) {
        this.floorNumber = floorNumber;
        this.rooms = rooms;
        this.username = username;
        this.version = version;
    }

    // Getters and Setters
    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
