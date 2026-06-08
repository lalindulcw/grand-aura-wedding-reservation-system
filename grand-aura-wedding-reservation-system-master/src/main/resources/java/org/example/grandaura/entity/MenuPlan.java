package org.example.grandaura.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Entity representing a Menu Plan Template created by the Catering Manager
 * Reusable menu plan templates that customers can select when booking
 */
@Entity
@Table(name = "menu_plans")
public class MenuPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Plan name is required")
    @Size(max = 200)
    @Column(name = "plan_name", nullable = false, length = 200)
    private String planName;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull(message = "Price per guest is required")
    @Column(name = "price_per_guest", nullable = false)
    private Double pricePerGuest;

    // Course Details
    @Size(max = 500)
    @Column(name = "appetizers", length = 500)
    private String appetizers;

    @Size(max = 500)
    @Column(name = "main_courses", length = 500)
    private String mainCourses;

    @Size(max = 500)
    @Column(name = "desserts", length = 500)
    private String desserts;

    @Size(max = 500)
    @Column(name = "beverages", length = 500)
    private String beverages;

    // Additional Details
    @Size(max = 1000)
    @Column(name = "dietary_info", length = 1000)
    private String dietaryInfo;

    @Size(max = 1000)
    @Column(name = "special_notes", length = 1000)
    private String specialNotes;

    @NotNull(message = "Is active is required")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive; // true = Available for customers, false = Hidden

    @Size(max = 50)
    @Column(name = "category", length = 50)
    private String category; // "Classic", "Premium", "Luxury", "Custom"

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    // Constructors
    public MenuPlan() {
        this.createdDate = LocalDate.now();
        this.lastModifiedDate = LocalDate.now();
        this.isActive = true; // Default to active
    }

    public MenuPlan(String planName, String description, Double pricePerGuest, String category) {
        this();
        this.planName = planName;
        this.description = description;
        this.pricePerGuest = pricePerGuest;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPricePerGuest() {
        return pricePerGuest;
    }

    public void setPricePerGuest(Double pricePerGuest) {
        this.pricePerGuest = pricePerGuest;
    }

    public String getAppetizers() {
        return appetizers;
    }

    public void setAppetizers(String appetizers) {
        this.appetizers = appetizers;
    }

    public String getMainCourses() {
        return mainCourses;
    }

    public void setMainCourses(String mainCourses) {
        this.mainCourses = mainCourses;
    }

    public String getDesserts() {
        return desserts;
    }

    public void setDesserts(String desserts) {
        this.desserts = desserts;
    }

    public String getBeverages() {
        return beverages;
    }

    public void setBeverages(String beverages) {
        this.beverages = beverages;
    }

    public String getDietaryInfo() {
        return dietaryInfo;
    }

    public void setDietaryInfo(String dietaryInfo) {
        this.dietaryInfo = dietaryInfo;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "MenuPlan{" +
                "id=" + id +
                ", planName='" + planName + '\'' +
                ", category='" + category + '\'' +
                ", pricePerGuest=" + pricePerGuest +
                ", isActive=" + isActive +
                '}';
    }
}

