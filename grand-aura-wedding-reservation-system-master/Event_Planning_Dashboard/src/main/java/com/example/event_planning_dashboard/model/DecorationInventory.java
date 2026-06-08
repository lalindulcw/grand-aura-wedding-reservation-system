package com.example.event_planning_dashboard.model;

import javax.persistence.*;

@Entity
@Table(name = "decoration_inventory")
public class DecorationInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "quantity_reserved")
    private Integer quantityReserved = 0;

    @Column(name = "condition_status")
    private String conditionStatus;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    // Constructors
    public DecorationInventory() {}

    public DecorationInventory(String itemName, String category, Integer quantityAvailable,
                               String conditionStatus, String description) {
        this.itemName = itemName;
        this.category = category;
        this.quantityAvailable = quantityAvailable;
        this.conditionStatus = conditionStatus;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(Integer quantityAvailable) { this.quantityAvailable = quantityAvailable; }

    public Integer getQuantityReserved() { return quantityReserved; }
    public void setQuantityReserved(Integer quantityReserved) { this.quantityReserved = quantityReserved; }

    public String getConditionStatus() { return conditionStatus; }
    public void setConditionStatus(String conditionStatus) { this.conditionStatus = conditionStatus; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
