package com.example.event_planning_dashboard.model;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "weddings")
public class Wedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COUPLE_NAMES", nullable = false)
    private String coupleNames;

    @Column(name = "WEDDING_DATE", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate weddingDate;

    @Column(name = "venue")
    private String venue;

    @Column(name = "guest_count")
    private Integer guestCount;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "theme_preference")
    private String themePreference;

    @Column(name = "music_style")
    private String musicStyle;

    @Column(name = "status")
    private String status;

    @Column(name = "special_requirements", columnDefinition = "NVARCHAR(MAX)")
    private String specialRequirements;

    // Constructors
    public Wedding() {}

    public Wedding(String coupleNames, LocalDate weddingDate, String venue,
                   Integer guestCount, Double budget, String themePreference,
                   String musicStyle, String status) {
        this.coupleNames = coupleNames;
        this.weddingDate = weddingDate;
        this.venue = venue;
        this.guestCount = guestCount;
        this.budget = budget;
        this.themePreference = themePreference;
        this.musicStyle = musicStyle;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCoupleNames() { return coupleNames; }
    public void setCoupleNames(String coupleNames) {
        this.coupleNames = coupleNames;
    }

    public LocalDate getWeddingDate() { return weddingDate; }
    public void setWeddingDate(LocalDate weddingDate) {
        this.weddingDate = weddingDate;
    }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

    public String getThemePreference() { return themePreference; }
    public void setThemePreference(String themePreference) { this.themePreference = themePreference; }

    public String getMusicStyle() { return musicStyle; }
    public void setMusicStyle(String musicStyle) { this.musicStyle = musicStyle; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSpecialRequirements() { return specialRequirements; }
    public void setSpecialRequirements(String specialRequirements) { this.specialRequirements = specialRequirements; }
}
