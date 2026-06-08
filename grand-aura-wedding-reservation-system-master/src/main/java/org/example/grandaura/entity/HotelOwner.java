package org.example.grandaura.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Hotel Owner entity for managing hotel operations and analytics
 * This entity represents hotel owners who have access to all booking data
 * and management capabilities across the hotel.
 */
@Entity
@Table(name = "hotel_owners")
public class HotelOwner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Owner name is required")
    @Size(max = 100, message = "Owner name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String ownerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 160, message = "Email cannot exceed 160 characters")
    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @NotBlank(message = "Password hash is required")
    @Size(min = 8)
    @Column(nullable = false, length = 200)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled = true;

    @Size(max = 200, message = "Hotel name cannot exceed 200 characters")
    @Column(length = 200)
    private String hotelName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    // Default constructor (required by JPA)
    public HotelOwner() {}

    // Constructor for creating new hotel owner
    public HotelOwner(String ownerName, String email, String passwordHash, String hotelName) {
        this.ownerName = ownerName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.hotelName = hotelName;
        this.enabled = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "HotelOwner{" +
                "id=" + id +
                ", ownerName='" + ownerName + '\'' +
                ", email='" + email + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
