package com.example.event_planning_dashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;

@Entity
@Table(name = "event_schedules")
public class EventSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "wedding_id", nullable = true)
    @JsonIgnoreProperties("eventSchedules")
    private Wedding wedding;

    @Column(name = "activity_time", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime activityTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "activity_title", nullable = false)
    private String activityTitle;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "location")
    private String location;

    // Constructors
    public EventSchedule() {}

    public EventSchedule(Wedding wedding, LocalTime activityTime, Integer durationMinutes,
                         String activityTitle, String description, String location) {
        this.wedding = wedding;
        this.activityTime = activityTime;
        this.durationMinutes = durationMinutes;
        this.activityTitle = activityTitle;
        this.description = description;
        this.location = location;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Wedding getWedding() { return wedding; }
    public void setWedding(Wedding wedding) { this.wedding = wedding; }

    public LocalTime getActivityTime() { return activityTime; }
    public void setActivityTime(LocalTime activityTime) {
        this.activityTime = activityTime;
    }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getActivityTitle() { return activityTitle; }
    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
