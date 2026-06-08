package org.example.grandaura.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.*;
import jakarta.persistence.Column;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String customerName;

	@Size(max = 100)
	@Column(length = 100)
	private String brideName;

	@Size(max = 100)
	@Column(length = 100)
	private String groomName;

	@NotBlank
	@Email
	@Size(max = 160)
	@Column(nullable = false, length = 160)
	private String customerEmail;

	@Min(1)
	@Column(nullable = false)
	private int guestCount;

	@Size(max = 500)
	@Column(length = 500)
	private String specialRequests;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String venue;

	@Size(max = 100)
	@Column(length = 100)
	private String preferredVenue;

	@FutureOrPresent
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column(nullable = false)
	private LocalDate weddingDate;

	// Catering Service Fields
	@Size(max = 100)
	@Column(length = 100)
	private String cateringPackage;

	@Size(max = 500)
	@Column(length = 500)
	private String dietaryRequirements;

	@Size(max = 500)
	@Column(length = 500)
	private String specialCateringRequests;

	private Integer estimatedGuestCount;

	@Size(max = 1000)
	@Column(length = 1000)
	private String selectedMenuItems;

	// Menu Plan Reference
	@Column(name = "menu_plan_id")
	private Long menuPlanId;

	// Front Desk Management Fields
	@Size(max = 50)
	@Column(length = 50)
	private String bookingStatus; // "Pending", "Checked In", "In Progress", "Completed", "Cancelled"

	@Size(max = 1000)
	@Column(length = 1000)
	private String frontDeskNotes;

	@Column
	private java.time.LocalDateTime checkInTime;

	@Column
	private java.time.LocalDateTime checkOutTime;

    // Default constructor (required by JPA)
    public Booking() {
		this.bookingStatus = "Pending"; // Default status
	}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getBrideName() { return brideName; }
    public void setBrideName(String brideName) { this.brideName = brideName; }
    public String getGroomName() { return groomName; }
    public void setGroomName(String groomName) { this.groomName = groomName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public int getGuestCount() { return guestCount; }
    public void setGuestCount(int guestCount) { this.guestCount = guestCount; }
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getPreferredVenue() { return preferredVenue; }
    public void setPreferredVenue(String preferredVenue) { this.preferredVenue = preferredVenue; }
    public LocalDate getWeddingDate() { return weddingDate; }
    public void setWeddingDate(LocalDate weddingDate) { this.weddingDate = weddingDate; }
    
    // Catering Service Getters and Setters
    public String getCateringPackage() { return cateringPackage; }
    public void setCateringPackage(String cateringPackage) { this.cateringPackage = cateringPackage; }
    public String getDietaryRequirements() { return dietaryRequirements; }
    public void setDietaryRequirements(String dietaryRequirements) { this.dietaryRequirements = dietaryRequirements; }
    public String getSpecialCateringRequests() { return specialCateringRequests; }
    public void setSpecialCateringRequests(String specialCateringRequests) { this.specialCateringRequests = specialCateringRequests; }
    public Integer getEstimatedGuestCount() { return estimatedGuestCount; }
    public void setEstimatedGuestCount(Integer estimatedGuestCount) { this.estimatedGuestCount = estimatedGuestCount; }
    public String getSelectedMenuItems() { return selectedMenuItems; }
    public void setSelectedMenuItems(String selectedMenuItems) { this.selectedMenuItems = selectedMenuItems; }
    
    public Long getMenuPlanId() { return menuPlanId; }
    public void setMenuPlanId(Long menuPlanId) { this.menuPlanId = menuPlanId; }
    
    // Front Desk Management Getters and Setters
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public String getFrontDeskNotes() { return frontDeskNotes; }
    public void setFrontDeskNotes(String frontDeskNotes) { this.frontDeskNotes = frontDeskNotes; }
    public java.time.LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(java.time.LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    public java.time.LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(java.time.LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
}