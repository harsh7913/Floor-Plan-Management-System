package com.fpms.FloorPlanManagementSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "floor_plan")
public class FloorPlanEntity {

    @Id
    @Column(nullable = false)
    private String version;  // Version is the primary key and must be manually assigned

    @Column(nullable = false)
    private String username;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String floorPlanData;

    // Getters and setters
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFloorPlanData() {
        return floorPlanData;
    }

    public void setFloorPlanData(String floorPlanData) {
        this.floorPlanData = floorPlanData;
    }
}
