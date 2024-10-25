package com.fpms.FloorPlanManagementSystem.model;

public class RoomDTO {
    private String roomId;
    private String name;
    private int capacity;
    private boolean available;
    private int bookingWeightage;
    private String roomName;
    private String floorVersion;  // Added the version field

    // Constructor
    public RoomDTO() {}

    public RoomDTO(String roomId, String name, int capacity, boolean available, int bookingWeightage, String roomName, String floorVersion) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.available = available;
        this.bookingWeightage = bookingWeightage;
        this.roomName = roomName;
        this.floorVersion = floorVersion;  // Initialize the new version field

        // Print statements for debugging purposes
        System.out.print(roomId);
        System.out.print(capacity);
        System.out.print(floorVersion);  // Print the version for debugging
    }

    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getBookingWeightage() {
        return bookingWeightage;
    }

    public void setBookingWeightage(int bookingWeightage) {
        this.bookingWeightage = bookingWeightage;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getFloorVersion() {
        return floorVersion;
    }

    public void setFloorVersion(String floorVersion) {
        this.floorVersion = floorVersion;
    }
}
