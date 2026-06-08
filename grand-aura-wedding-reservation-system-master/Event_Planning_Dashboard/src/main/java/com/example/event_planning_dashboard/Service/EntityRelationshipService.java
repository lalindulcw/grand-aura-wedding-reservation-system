package com.example.event_planning_dashboard.Service;

import com.example.event_planning_dashboard.model.Reminder;
import com.example.event_planning_dashboard.model.EventSchedule;
import com.example.event_planning_dashboard.model.Wedding;
import com.example.event_planning_dashboard.Repository.WeddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityRelationshipService {

    @Autowired
    private WeddingRepository weddingRepository;

    /**
     * Ensures that a reminder's wedding relationship is properly loaded from the database
     * to avoid Hibernate transient property exceptions
     */
    public Reminder prepareReminderForSave(Reminder reminder) {
        if (reminder.getWedding() != null && reminder.getWedding().getId() != null) {
            Wedding existingWedding = weddingRepository.findById(reminder.getWedding().getId()).orElse(null);
            if (existingWedding != null) {
                reminder.setWedding(existingWedding);
            } else {
                // If wedding doesn't exist, set to null to avoid cascade issues
                reminder.setWedding(null);
            }
        }
        return reminder;
    }

    /**
     * Ensures that an event schedule's wedding relationship is properly loaded from the database
     * to avoid Hibernate transient property exceptions
     */
    public EventSchedule prepareScheduleForSave(EventSchedule schedule) {
        if (schedule.getWedding() != null && schedule.getWedding().getId() != null) {
            Wedding existingWedding = weddingRepository.findById(schedule.getWedding().getId()).orElse(null);
            if (existingWedding != null) {
                schedule.setWedding(existingWedding);
            } else {
                // If wedding doesn't exist, set to null to avoid cascade issues
                schedule.setWedding(null);
            }
        }
        return schedule;
    }
}



