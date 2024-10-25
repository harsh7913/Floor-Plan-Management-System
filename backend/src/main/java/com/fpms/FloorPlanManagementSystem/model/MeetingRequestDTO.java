package com.fpms.FloorPlanManagementSystem.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingRequestDTO {
    private String roomId;
    private int participants;
    private String version;

    // Constructor
    public MeetingRequestDTO() {
    }

        // Add getter and setter for the new field
        public String getVersion() {
            return version;
        }
    
        public void setVersion(String version) {
            this.version = version;
        }

    public MeetingRequestDTO(String roomId, int participants) {
        this.roomId = roomId;
        this.participants = participants;
    }

    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
}
