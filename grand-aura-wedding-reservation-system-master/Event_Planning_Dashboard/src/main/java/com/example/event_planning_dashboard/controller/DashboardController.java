package com.example.event_planning_dashboard.controller;

import com.example.event_planning_dashboard.Repository.WeddingRepository;
import com.example.event_planning_dashboard.Repository.DecorationInventoryRepository;
import com.example.event_planning_dashboard.Repository.EventScheduleRepository;
import com.example.event_planning_dashboard.Repository.ReminderRepository;
import com.example.event_planning_dashboard.Service.EntityRelationshipService;
import com.example.event_planning_dashboard.model.EventSchedule;
import com.example.event_planning_dashboard.model.Reminder;
import com.example.event_planning_dashboard.model.Wedding;
import com.example.event_planning_dashboard.model.DecorationInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class DashboardController {

    @Autowired
    private WeddingRepository weddingRepository;

    @Autowired
    private DecorationInventoryRepository decorationInventoryRepository;

    @Autowired
    private EventScheduleRepository eventScheduleRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityRelationshipService entityRelationshipService;

    // In DashboardController.java (add import if needed)
    @GetMapping("/coordinator/dashboard")
    public String showDashboard(Model model, Authentication authentication) {  // Inject Authentication
        if (authentication == null || !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EVENT_COORDINATOR"))) {
            return "redirect:/login?access-denied=true";
        }
        // Load all data for the dashboard
        model.addAttribute("weddings", weddingRepository.findAll());
        model.addAttribute("inventory", decorationInventoryRepository.findAll());
        model.addAttribute("schedules", eventScheduleRepository.findAll());
        model.addAttribute("reminders", reminderRepository.findAll());
        return "event-dashboard";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login"; // Redirect to login page
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Load all data for the dashboard
        model.addAttribute("weddings", weddingRepository.findAll());
        model.addAttribute("inventory", decorationInventoryRepository.findAll());
        model.addAttribute("schedules", eventScheduleRepository.findAll());
        model.addAttribute("reminders", reminderRepository.findAll());
        
        // Calculate real statistics
        long totalWeddings = weddingRepository.count();
        long activeWeddings = weddingRepository.findAll().stream()
            .filter(w -> w.getStatus() != null && !w.getStatus().equals("Completed"))
            .count();
        long inProgressWeddings = weddingRepository.findAll().stream()
            .filter(w -> w.getStatus() != null && w.getStatus().equals("In Progress"))
            .count();
        long plannedWeddings = weddingRepository.findAll().stream()
            .filter(w -> w.getStatus() != null && w.getStatus().equals("Planned"))
            .count();
        
        // Add statistics to model
        model.addAttribute("totalWeddings", totalWeddings);
        model.addAttribute("activeWeddings", activeWeddings);
        model.addAttribute("inProgressWeddings", inProgressWeddings);
        model.addAttribute("plannedWeddings", plannedWeddings);
        
        return "event-dashboard";
    }

    // Backward compatibility: legacy static path
    @GetMapping("/event-dashboard.html")
    public String legacyDashboard(Model model) {
        // Load all data for the dashboard
        model.addAttribute("weddings", weddingRepository.findAll());
        model.addAttribute("inventory", decorationInventoryRepository.findAll());
        model.addAttribute("schedules", eventScheduleRepository.findAll());
        model.addAttribute("reminders", reminderRepository.findAll());
        
        // Calculate real statistics
        long totalWeddings = weddingRepository.count();
        long activeWeddings = weddingRepository.findAll().stream()
            .filter(w -> w.getStatus() != null && !w.getStatus().equals("Completed"))
            .count();
        long inProgressWeddings = weddingRepository.findAll().stream()
            .filter(w -> w.getStatus() != null && w.getStatus().equals("In Progress"))
            .count();
        long plannedWeddings = weddingRepository.findAll().stream()
            .filter(w -> w.getStatus() != null && w.getStatus().equals("Planned"))
            .count();
        
        // Add statistics to model
        model.addAttribute("totalWeddings", totalWeddings);
        model.addAttribute("activeWeddings", activeWeddings);
        model.addAttribute("inProgressWeddings", inProgressWeddings);
        model.addAttribute("plannedWeddings", plannedWeddings);
        
        return "event-dashboard";
    }

    @RequestMapping("/event-planning")
    public String eventPlanning(Model model) {
        model.addAttribute("weddings", weddingRepository.findAll());
        model.addAttribute("inventory", decorationInventoryRepository.findAll());
        model.addAttribute("schedules", eventScheduleRepository.findAll());
        model.addAttribute("reminders", reminderRepository.findAll());
        return "event-dashboard";
    }

    // Create/Update wedding basic info
    @PostMapping("/weddings")
    public String saveWedding(@ModelAttribute Wedding wedding) {
        if (wedding.getId() != null) {
            Wedding existing = weddingRepository.findById(wedding.getId()).orElse(null);
            if (existing != null) {
                if (wedding.getCoupleNames() != null && !wedding.getCoupleNames().isEmpty()) {
                    existing.setCoupleNames(wedding.getCoupleNames());
                }
                if (wedding.getWeddingDate() != null) {
                    existing.setWeddingDate(wedding.getWeddingDate());
                }
                if (wedding.getVenue() != null) {
                    existing.setVenue(wedding.getVenue());
                }
                if (wedding.getGuestCount() != null) {
                    existing.setGuestCount(wedding.getGuestCount());
                }
                if (wedding.getBudget() != null) {
                    existing.setBudget(wedding.getBudget());
                }
                if (wedding.getThemePreference() != null) {
                    existing.setThemePreference(wedding.getThemePreference());
                }
                if (wedding.getMusicStyle() != null) {
                    existing.setMusicStyle(wedding.getMusicStyle());
                }
                if (wedding.getStatus() != null) {
                    existing.setStatus(wedding.getStatus());
                }
                if (wedding.getSpecialRequirements() != null) {
                    existing.setSpecialRequirements(wedding.getSpecialRequirements());
                }
                weddingRepository.save(existing);
                return "redirect:/dashboard";
            }
        }
        // Creating a new wedding: require mandatory fields
        if (wedding.getCoupleNames() == null || wedding.getCoupleNames().isEmpty() || wedding.getWeddingDate() == null) {
            return "redirect:/dashboard?error=missing_required_fields";
        }
        weddingRepository.save(wedding);
        return "redirect:/dashboard";
    }

    // Add schedule item
    @PostMapping("/schedules")
    public String addSchedule(@ModelAttribute EventSchedule schedule) {
        // Use service to properly handle wedding relationship
        schedule = entityRelationshipService.prepareScheduleForSave(schedule);
        eventScheduleRepository.save(schedule);
        return "redirect:/dashboard";
    }

    // Create a sample schedule structure for a given wedding if not present
    @PostMapping("/schedules/sample")
    public String addSampleSchedule(@ModelAttribute("wedding.id") Long weddingId) {
        if (weddingId == null) {
            return "redirect:/dashboard?error=select_wedding";
        }
        Wedding w = weddingRepository.findById(weddingId).orElse(null);
        if (w == null) {
            return "redirect:/dashboard?error=wedding_not_found";
        }

        // Only add if no schedules yet for this wedding
        boolean hasAny = eventScheduleRepository.findAll().stream().anyMatch(s -> s.getWedding().getId().equals(weddingId));
        if (!hasAny) {
            EventSchedule ceremony = new EventSchedule(w, java.time.LocalTime.of(14, 0), 60, "Ceremony", "Vows and ring exchange", "Garden");
            EventSchedule photos = new EventSchedule(w, java.time.LocalTime.of(15, 30), 30, "Photo Session", "Family photos", "Terrace");
            EventSchedule reception = new EventSchedule(w, java.time.LocalTime.of(18, 0), 180, "Reception", "Dinner and dancing", "Ballroom");
            eventScheduleRepository.save(ceremony);
            eventScheduleRepository.save(photos);
            eventScheduleRepository.save(reception);
        }
        return "redirect:/dashboard";
    }

    // Save reminder
    @PostMapping("/reminders")
    public String saveReminder(@ModelAttribute Reminder reminder) {
        // Use service to properly handle wedding relationship
        reminder = entityRelationshipService.prepareReminderForSave(reminder);
        reminderRepository.save(reminder);
        return "redirect:/dashboard#reminders";
    }

    // Save inventory item
    @PostMapping("/inventory")
    public String saveInventory(@ModelAttribute DecorationInventory item) {
        decorationInventoryRepository.save(item);
        return "redirect:/dashboard";
    }

    // ==============================
    // UPDATE ENDPOINTS
    // ==============================

    @PutMapping("/weddings/{id}")
    public String updateWedding(@PathVariable Long id, @ModelAttribute Wedding wedding) {
        Wedding existing = weddingRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setCoupleNames(wedding.getCoupleNames());
            existing.setWeddingDate(wedding.getWeddingDate());
            existing.setVenue(wedding.getVenue());
            existing.setGuestCount(wedding.getGuestCount());
            existing.setBudget(wedding.getBudget());
            existing.setThemePreference(wedding.getThemePreference());
            existing.setMusicStyle(wedding.getMusicStyle());
            existing.setStatus(wedding.getStatus());
            existing.setSpecialRequirements(wedding.getSpecialRequirements());
            weddingRepository.save(existing);
        }
        return "redirect:/dashboard";
    }

    @PutMapping("/schedules/{id}")
    public String updateSchedule(@PathVariable Long id, @ModelAttribute EventSchedule schedule) {
        EventSchedule existing = eventScheduleRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setActivityTime(schedule.getActivityTime());
            existing.setDurationMinutes(schedule.getDurationMinutes());
            existing.setActivityTitle(schedule.getActivityTitle());
            existing.setDescription(schedule.getDescription());
            existing.setLocation(schedule.getLocation());
            eventScheduleRepository.save(existing);
        }
        return "redirect:/dashboard";
    }

    @PutMapping("/reminders/{id}")
    public String updateReminder(@PathVariable Long id, @ModelAttribute Reminder reminder) {
        Reminder existing = reminderRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTitle(reminder.getTitle());
            existing.setReminderDate(reminder.getReminderDate());
            existing.setReminderTime(reminder.getReminderTime());
            existing.setType(reminder.getType());
            existing.setMessage(reminder.getMessage());
            existing.setPriority(reminder.getPriority());
            reminderRepository.save(existing);
        }
        return "redirect:/dashboard";
    }

    @PutMapping("/inventory/{id}")
    public String updateInventory(@PathVariable Long id, @ModelAttribute DecorationInventory item) {
        DecorationInventory existing = decorationInventoryRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setItemName(item.getItemName());
            existing.setCategory(item.getCategory());
            existing.setQuantityAvailable(item.getQuantityAvailable());
            existing.setQuantityReserved(item.getQuantityReserved());
            existing.setConditionStatus(item.getConditionStatus());
            existing.setDescription(item.getDescription());
            decorationInventoryRepository.save(existing);
        }
        return "redirect:/dashboard";
    }

    // ==============================
    // DELETE ENDPOINTS
    // ==============================

    @DeleteMapping("/weddings/{id}")
    public String deleteWedding(@PathVariable Long id) {
        weddingRepository.deleteById(id);
        return "redirect:/dashboard";
    }

    @DeleteMapping("/schedules/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        eventScheduleRepository.deleteById(id);
        return "redirect:/dashboard";
    }

    @DeleteMapping("/reminders/{id}")
    public String deleteReminder(@PathVariable Long id) {
        reminderRepository.deleteById(id);
        return "redirect:/dashboard";
    }

    @DeleteMapping("/inventory/{id}")
    public String deleteInventory(@PathVariable Long id) {
        decorationInventoryRepository.deleteById(id);
        return "redirect:/dashboard";
    }

    // ==============================
    // AJAX ENDPOINTS FOR UI UPDATES
    // ==============================

    @PostMapping("/weddings/{id}/update")
    public String updateWeddingAjax(@PathVariable Long id, @ModelAttribute Wedding wedding) {
        return updateWedding(id, wedding);
    }

    @PostMapping("/weddings/{id}/delete")
    public String deleteWeddingAjax(@PathVariable Long id) {
        return deleteWedding(id);
    }

    @PostMapping("/schedules/{id}/update")
    public String updateScheduleAjax(@PathVariable Long id, @ModelAttribute EventSchedule schedule) {
        return updateSchedule(id, schedule);
    }

    @PostMapping("/schedules/{id}/delete")
    public String deleteScheduleAjax(@PathVariable Long id) {
        return deleteSchedule(id);
    }

    @PostMapping("/reminders/{id}/update")
    public String updateReminderAjax(@PathVariable Long id, @ModelAttribute Reminder reminder) {
        return updateReminder(id, reminder);
    }

    @PostMapping("/reminders/{id}/delete")
    public String deleteReminderAjax(@PathVariable Long id) {
        return deleteReminder(id);
    }

    @PostMapping("/inventory/{id}/update")
    public String updateInventoryAjax(@PathVariable Long id, @ModelAttribute DecorationInventory item) {
        return updateInventory(id, item);
    }

    @PostMapping("/inventory/{id}/delete")
    public String deleteInventoryAjax(@PathVariable Long id) {
        return deleteInventory(id);
    }

    // ==============================
    // TEST ENDPOINTS (NO AUTH REQUIRED)
    // ==============================

    @GetMapping("/test-crud")
    @org.springframework.web.bind.annotation.ResponseBody
    public String testCrud() {
        try {
            // Test creating a wedding
            Wedding testWedding = new Wedding();
            testWedding.setCoupleNames("Test Couple");
            testWedding.setWeddingDate(java.time.LocalDate.now().plusDays(30));
            testWedding.setVenue("Test Venue");
            testWedding.setGuestCount(100);
            testWedding.setBudget(15000.0);
            testWedding.setStatus("Planned");
            testWedding = weddingRepository.save(testWedding);

            // Test updating the wedding
            testWedding.setStatus("In Progress");
            weddingRepository.save(testWedding);

            // Test creating a schedule
            EventSchedule testSchedule = new EventSchedule();
            testSchedule.setActivityTitle("Test Activity");
            testSchedule.setActivityTime(java.time.LocalTime.of(14, 0));
            testSchedule.setDescription("Test Description");
            testSchedule.setWedding(testWedding);
            eventScheduleRepository.save(testSchedule);

            // Test creating inventory
            DecorationInventory testInventory = new DecorationInventory();
            testInventory.setItemName("Test Item");
            testInventory.setCategory("Test Category");
            testInventory.setQuantityAvailable(10);
            testInventory.setConditionStatus("Excellent");
            decorationInventoryRepository.save(testInventory);

            // Test creating reminder
            Reminder testReminder = new Reminder();
            testReminder.setTitle("Test Reminder");
            testReminder.setReminderDate(java.time.LocalDate.now().plusDays(7));
            testReminder.setReminderTime(java.time.LocalTime.of(10, 0));
            testReminder.setType("General");
            testReminder.setPriority("High");
            testReminder.setMessage("Test reminder message");
            testReminder.setWedding(testWedding);
            reminderRepository.save(testReminder);

            // Test updating the wedding
            testWedding.setStatus("In Progress");
            testWedding.setCoupleNames("Updated Test Couple");
            weddingRepository.save(testWedding);

            // Test updating the schedule
            testSchedule.setActivityTitle("Updated Test Activity");
            eventScheduleRepository.save(testSchedule);

            // Test updating inventory
            testInventory.setItemName("Updated Test Item");
            testInventory.setQuantityAvailable(20);
            decorationInventoryRepository.save(testInventory);

            // Test updating reminder
            testReminder.setTitle("Updated Test Reminder");
            testReminder.setPriority("Medium");
            reminderRepository.save(testReminder);

            // Test deleting the reminder
            reminderRepository.deleteById(testReminder.getId());

            return "CRUD operations test successful! Created and updated: Wedding ID " + testWedding.getId() + 
                   ", Schedule ID " + testSchedule.getId() + 
                   ", Inventory ID " + testInventory.getId() + 
                   ". Deleted: Reminder ID " + testReminder.getId();
        } catch (Exception e) {
            return "CRUD test failed: " + e.getMessage();
        }
    }

    @GetMapping("/test-persistence")
    @org.springframework.web.bind.annotation.ResponseBody
    public String testPersistence() {
        try {
            // Count existing data
            long weddingCount = weddingRepository.count();
            long scheduleCount = eventScheduleRepository.count();
            long inventoryCount = decorationInventoryRepository.count();
            long reminderCount = reminderRepository.count();
            
            return "Database persistence test - Current counts: " +
                   "Weddings: " + weddingCount + 
                   ", Schedules: " + scheduleCount + 
                   ", Inventory: " + inventoryCount + 
                   ", Reminders: " + reminderCount;
        } catch (Exception e) {
            return "Persistence test failed: " + e.getMessage();
        }
    }

    @PostMapping("/test-wedding-update")
    @org.springframework.web.bind.annotation.ResponseBody
    public String testWeddingUpdate() {
        try {
            // Find the first wedding and update it
            Wedding wedding = weddingRepository.findAll().stream().findFirst().orElse(null);
            if (wedding != null) {
                String originalStatus = wedding.getStatus();
                wedding.setStatus("Updated via Test");
                wedding.setCoupleNames("Updated Test Couple");
                weddingRepository.save(wedding);
                
                return "Wedding update test successful! ID: " + wedding.getId() + 
                       ", Original Status: " + originalStatus + 
                       ", New Status: " + wedding.getStatus();
            } else {
                return "No weddings found to update";
            }
        } catch (Exception e) {
            return "Wedding update test failed: " + e.getMessage();
        }
    }

    @PostMapping("/test-form-submission")
    @org.springframework.web.bind.annotation.ResponseBody
    public String testFormSubmission(@ModelAttribute Wedding wedding) {
        try {
            // Simulate form submission
            wedding.setCoupleNames("Form Test Couple");
            wedding.setWeddingDate(java.time.LocalDate.now().plusDays(60));
            wedding.setVenue("Form Test Venue");
            wedding.setGuestCount(200);
            wedding.setBudget(30000.0);
            wedding.setStatus("Form Test Status");
            wedding.setThemePreference("Form Test Theme");
            wedding.setMusicStyle("Form Test Music");
            wedding.setSpecialRequirements("Form Test Requirements");
            
            Wedding savedWedding = weddingRepository.save(wedding);
            
            return "Form submission test successful! Created Wedding ID: " + savedWedding.getId() + 
                   " with couple names: " + savedWedding.getCoupleNames();
        } catch (Exception e) {
            return "Form submission test failed: " + e.getMessage();
        }
    }

    @GetMapping("/debug-weddings")
    @org.springframework.web.bind.annotation.ResponseBody
    public String debugWeddings() {
        try {
            java.util.List<Wedding> weddings = weddingRepository.findAll();
            StringBuilder result = new StringBuilder("Total weddings in database: " + weddings.size() + "\n\n");
            for (int i = 0; i < weddings.size(); i++) {
                Wedding wedding = weddings.get(i);
                result.append("Wedding ").append(i + 1).append(":\n");
                result.append("  ID: ").append(wedding.getId()).append("\n");
                result.append("  Couple: ").append(wedding.getCoupleNames()).append("\n");
                result.append("  Date: ").append(wedding.getWeddingDate()).append("\n");
                result.append("  Venue: ").append(wedding.getVenue()).append("\n");
                result.append("  Status: ").append(wedding.getStatus()).append("\n\n");
            }
            return result.toString();
        } catch (Exception e) {
            return "Debug failed: " + e.getMessage();
        }
    }

    @GetMapping("/cleanup-orphaned-records")
    @org.springframework.web.bind.annotation.ResponseBody
    public String cleanupOrphanedRecords() {
        try {
            // Use SQL to clean up orphaned records directly
            int orphanedSchedules = jdbcTemplate.update("DELETE FROM event_schedules WHERE wedding_id NOT IN (SELECT id FROM weddings)");
            int orphanedReminders = jdbcTemplate.update("DELETE FROM reminders WHERE wedding_id NOT IN (SELECT id FROM weddings)");
            
            return "Cleanup completed! Removed " + orphanedSchedules + " orphaned schedules and " + orphanedReminders + " orphaned reminders.";
        } catch (Exception e) {
            return "Cleanup failed: " + e.getMessage();
        }
    }

}
