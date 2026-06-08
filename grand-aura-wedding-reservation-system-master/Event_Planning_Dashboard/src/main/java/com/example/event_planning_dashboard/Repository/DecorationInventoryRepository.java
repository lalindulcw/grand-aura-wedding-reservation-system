package com.example.event_planning_dashboard.Repository;

import com.example.event_planning_dashboard.model.DecorationInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecorationInventoryRepository extends JpaRepository<DecorationInventory, Long> {
}

