package org.example.grandaura.controller;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.EventCoordinator;
import org.example.grandaura.service.EventCoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for Event Coordinator operations
 * Handles event coordination, customer management, and planning functionality
 */
@Controller
@RequestMapping("/event-coordinator")
public class EventCoordinatorController {

    private final EventCoordinatorService eventCoordinatorService;

    @Autowired
    public EventCoordinatorController(EventCoordinatorService eventCoordinatorService) {
        this.eventCoordinatorService = eventCoordinatorService;
    }

    /**
     * Event Coordinator Dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> statistics = eventCoordinatorService.getEventCoordinationStatistics();
        List<Booking> upcomingBookings = eventCoordinatorService.getUpcomingBookings();
        List<Booking> specialRequests = eventCoordinatorService.getBookingsWithSpecialRequests();
        
        model.addAttribute("statistics", statistics);
        model.addAttribute("upcomingBookings", upcomingBookings);
        model.addAttribute("specialRequests", specialRequests);
        
        return "event-coordinator/dashboard";
    }

    /**
     * Event Calendar View
     */
    @GetMapping("/calendar")
    public String calendar(Model model) {
        List<Booking> allBookings = eventCoordinatorService.getAllBookings();
        Map<String, Long> monthlyStats = eventCoordinatorService.getMonthlyBookingStats();
        
        model.addAttribute("bookings", allBookings);
        model.addAttribute("monthlyStats", monthlyStats);
        
        return "event-coordinator/calendar";
    }

    /**
     * Customer Management
     */
    @GetMapping("/customers")
    public String customers(Model model) {
        List<Booking> allBookings = eventCoordinatorService.getAllBookings();
        model.addAttribute("bookings", allBookings);
        return "event-coordinator/customers";
    }

    /**
     * Event Planning
     */
    @GetMapping("/planning")
    public String planning(Model model) {
        List<Booking> upcomingBookings = eventCoordinatorService.getUpcomingBookings();
        List<Booking> specialRequests = eventCoordinatorService.getBookingsWithSpecialRequests();
        
        model.addAttribute("upcomingBookings", upcomingBookings);
        model.addAttribute("specialRequests", specialRequests);
        
        return "event-coordinator/planning";
    }

    /**
     * View specific booking details
     */
    @GetMapping("/bookings/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Optional<Booking> booking = eventCoordinatorService.getBookingById(id);
        
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "event-coordinator/bookingDetails";
        } else {
            return "redirect:/event-coordinator/dashboard?error=Booking not found";
        }
    }

    /**
     * Edit Booking Form
     */
    @GetMapping("/bookings/{id}/edit")
    public String editBookingForm(@PathVariable Long id, Model model) {
        Optional<Booking> booking = eventCoordinatorService.getBookingById(id);
        
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "event-coordinator/editBooking";
        } else {
            return "redirect:/event-coordinator/calendar?error=Booking not found";
        }
    }

    /**
     * Update Booking
     */
    @PostMapping("/bookings/{id}/edit")
    public String updateBooking(@PathVariable Long id, 
                               @RequestParam String specialRequests,
                               @RequestParam(required = false) String coordinatorNotes,
                               @RequestParam(required = false) String eventStatus,
                               @RequestParam(required = false) String contactLog,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        
        Optional<Booking> existingBooking = eventCoordinatorService.getBookingById(id);
        if (existingBooking.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Booking not found");
            return "redirect:/event-coordinator/calendar";
        }
        
        Booking booking = existingBooking.get();
        booking.setSpecialRequests(specialRequests);
        // Note: coordinatorNotes, eventStatus, and contactLog would need additional fields in Booking entity
        // For now, we'll append them to special requests
        if (coordinatorNotes != null && !coordinatorNotes.isEmpty()) {
            String updatedRequests = booking.getSpecialRequests() + "\n\n[Coordinator Notes]: " + coordinatorNotes;
            booking.setSpecialRequests(updatedRequests);
        }
        
        eventCoordinatorService.updateBooking(booking);
        
        redirectAttributes.addFlashAttribute("success", "Event details updated successfully");
        return "redirect:/event-coordinator/bookings/" + id;
    }

    /**
     * Show Create Event Plan Form
     */
    @GetMapping("/planning/create")
    public String showCreateEventPlanForm(Model model) {
        List<Booking> upcomingBookings = eventCoordinatorService.getUpcomingBookings();
        model.addAttribute("bookings", upcomingBookings);
        return "event-coordinator/createEventPlan";
    }

    /**
     * Create Event Plan
     */
    @PostMapping("/planning/create")
    public String createEventPlan(@RequestParam Long bookingId,
                                  @RequestParam String planTitle,
                                  @RequestParam String planDescription,
                                  @RequestParam String checklist,
                                  @RequestParam String timeline,
                                  @RequestParam(required = false) String vendorNotes,
                                  RedirectAttributes redirectAttributes) {
        try {
            Optional<Booking> booking = eventCoordinatorService.getBookingById(bookingId);
            
            if (booking.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Booking not found");
                return "redirect:/event-coordinator/planning";
            }
            
            // Create event plan by appending to special requests
            Booking existingBooking = booking.get();
            StringBuilder eventPlan = new StringBuilder();
            
            eventPlan.append("\n\n═══════════════════════════════════════\n");
            eventPlan.append("EVENT PLAN: ").append(planTitle).append("\n");
            eventPlan.append("═══════════════════════════════════════\n\n");
            
            eventPlan.append("📋 DESCRIPTION:\n");
            eventPlan.append(planDescription).append("\n\n");
            
            eventPlan.append("✅ CHECKLIST:\n");
            eventPlan.append(checklist).append("\n\n");
            
            eventPlan.append("📅 TIMELINE:\n");
            eventPlan.append(timeline).append("\n\n");
            
            if (vendorNotes != null && !vendorNotes.isEmpty()) {
                eventPlan.append("👥 VENDOR NOTES:\n");
                eventPlan.append(vendorNotes).append("\n\n");
            }
            
            eventPlan.append("Created by Event Coordinator on: ").append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))).append("\n");
            
            // Append to existing special requests
            String currentRequests = existingBooking.getSpecialRequests() != null ? existingBooking.getSpecialRequests() : "";
            existingBooking.setSpecialRequests(currentRequests + eventPlan.toString());
            
            eventCoordinatorService.saveBooking(existingBooking);
            
            redirectAttributes.addFlashAttribute("success", "Event plan created successfully!");
            return "redirect:/event-coordinator/bookings/" + bookingId;
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create event plan: " + e.getMessage());
            return "redirect:/event-coordinator/planning";
        }
    }

    /**
     * Event Coordinator Profile
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        // For now, return a placeholder profile page
        EventCoordinator eventCoordinator = new EventCoordinator();
        eventCoordinator.setCoordinatorName("Event Coordinator");
        eventCoordinator.setEmail("coordinator@grandaura.com");
        eventCoordinator.setDepartment("Wedding Planning");
        eventCoordinator.setSpecialization("Luxury Wedding Coordination");
        eventCoordinator.setPhoneNumber("+1-555-0123");
        
        model.addAttribute("eventCoordinator", eventCoordinator);
        return "event-coordinator/profile";
    }

    /**
     * Update event coordinator profile
     */
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String coordinatorName, 
                               @RequestParam String email,
                               @RequestParam String department,
                               @RequestParam String specialization,
                               @RequestParam String phoneNumber,
                               Model model) {
        try {
            // Get all event coordinators and find the one we want to update
            List<EventCoordinator> allCoordinators = eventCoordinatorService.getAllEventCoordinators();
            EventCoordinator existingCoordinator = null;
            
            // Find the existing coordinator (there should be only one in this demo)
            if (!allCoordinators.isEmpty()) {
                existingCoordinator = allCoordinators.get(0); // Get the first (and likely only) coordinator
            }
            
            EventCoordinator updatedCoordinator;
            if (existingCoordinator != null) {
                // Update existing coordinator
                updatedCoordinator = existingCoordinator;
                updatedCoordinator.setCoordinatorName(coordinatorName);
                updatedCoordinator.setEmail(email);
                updatedCoordinator.setDepartment(department);
                updatedCoordinator.setSpecialization(specialization);
                updatedCoordinator.setPhoneNumber(phoneNumber);
                // Keep existing password hash and other fields
            } else {
                // Create new coordinator if none exists
                updatedCoordinator = new EventCoordinator();
                updatedCoordinator.setCoordinatorName(coordinatorName);
                updatedCoordinator.setEmail(email);
                updatedCoordinator.setDepartment(department);
                updatedCoordinator.setSpecialization(specialization);
                updatedCoordinator.setPhoneNumber(phoneNumber);
                updatedCoordinator.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye"); // BCrypt hash for "password"
                updatedCoordinator.setEnabled(true);
            }
            
            eventCoordinatorService.saveEventCoordinator(updatedCoordinator);
            model.addAttribute("success", "Profile updated successfully! You can now login with your new email address.");
            model.addAttribute("eventCoordinator", updatedCoordinator);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            // Keep the original data on error
            EventCoordinator fallbackCoordinator = new EventCoordinator();
            fallbackCoordinator.setCoordinatorName("Event Coordinator");
            fallbackCoordinator.setEmail("coordinator@grandaura.com");
            fallbackCoordinator.setDepartment("Wedding Planning");
            fallbackCoordinator.setSpecialization("Luxury Wedding Coordination");
            fallbackCoordinator.setPhoneNumber("+1-555-0123");
            model.addAttribute("eventCoordinator", fallbackCoordinator);
        }
        return "event-coordinator/profile";
    }

    /**
     * Analytics and Reports
     */
    @GetMapping("/analytics")
    public String analytics(Model model) {
        Map<String, Object> statistics = eventCoordinatorService.getEventCoordinationStatistics();
        Map<String, Long> venueUtilization = eventCoordinatorService.getVenueUtilization();
        Map<String, Long> monthlyStats = eventCoordinatorService.getMonthlyBookingStats();
        
        model.addAttribute("statistics", statistics);
        model.addAttribute("venueUtilization", venueUtilization);
        model.addAttribute("monthlyStats", monthlyStats);
        
        return "event-coordinator/analytics";
    }

    /**
     * Generate Monthly Report as PDF
     */
    @GetMapping("/analytics/generate-monthly-report")
    public void generateMonthlyReport(jakarta.servlet.http.HttpServletResponse response) {
        try {
            // Get data for the report
            Map<String, Object> statistics = eventCoordinatorService.getEventCoordinationStatistics();
            Map<String, Long> venueUtilization = eventCoordinatorService.getVenueUtilization();
            Map<String, Long> monthlyStats = eventCoordinatorService.getMonthlyBookingStats();
            
            // Set response headers for PDF download
            response.setContentType("application/pdf");
            String filename = "Grand_Aura_Monthly_Report_" + 
                java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy_MM_dd")) + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            
            // Create PDF using iText
            com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(response.getOutputStream());
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf, com.itextpdf.kernel.geom.PageSize.A4);
            document.setMargins(40, 40, 40, 40);
            
            // Define colors
            com.itextpdf.kernel.colors.Color goldColor = new com.itextpdf.kernel.colors.DeviceRgb(200, 169, 106);
            com.itextpdf.kernel.colors.Color darkColor = new com.itextpdf.kernel.colors.DeviceRgb(45, 45, 45);
            com.itextpdf.kernel.colors.Color lightGray = new com.itextpdf.kernel.colors.DeviceRgb(248, 249, 250);
            
            // Title
            com.itextpdf.layout.element.Paragraph title = new com.itextpdf.layout.element.Paragraph("GRAND AURA")
                .setFontSize(28)
                .setBold()
                .setFontColor(goldColor)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginBottom(5);
            document.add(title);
            
            com.itextpdf.layout.element.Paragraph subtitle = new com.itextpdf.layout.element.Paragraph("Event Coordination - Monthly Report")
                .setFontSize(16)
                .setFontColor(darkColor)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginBottom(20);
            document.add(subtitle);
            
            // Report Date
            com.itextpdf.layout.element.Paragraph reportDate = new com.itextpdf.layout.element.Paragraph(
                    "Report Generated: " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy")))
                .setFontSize(10)
                .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginBottom(30);
            document.add(reportDate);
            
            // Horizontal line
            com.itextpdf.layout.element.LineSeparator line = new com.itextpdf.layout.element.LineSeparator(
                new com.itextpdf.kernel.pdf.canvas.draw.SolidLine(1f));
            line.setMarginBottom(20);
            document.add(line);
            
            // Key Statistics Section
            com.itextpdf.layout.element.Paragraph statsHeader = new com.itextpdf.layout.element.Paragraph("📊 KEY STATISTICS")
                .setFontSize(14)
                .setBold()
                .setFontColor(goldColor)
                .setMarginBottom(10);
            document.add(statsHeader);
            
            // Statistics Table
            com.itextpdf.layout.element.Table statsTable = new com.itextpdf.layout.element.Table(new float[]{2, 1})
                .useAllAvailableWidth()
                .setMarginBottom(20);
            
            statsTable.addHeaderCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Metric").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(10));
            statsTable.addHeaderCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Value").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(10));
            
            statsTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph("Total Events")).setPadding(8));
            statsTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(statistics.get("totalBookings").toString())).setPadding(8));
            
            statsTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph("Total Guests Served")).setPadding(8));
            statsTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(statistics.get("totalGuests").toString())).setPadding(8));
            
            statsTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph("Average Event Size")).setPadding(8));
            statsTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(String.format("%.0f guests", statistics.get("averageGuestCount")))).setPadding(8));
            
            statsTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph("Upcoming Events")).setPadding(8));
            statsTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(statistics.get("upcomingBookings").toString())).setPadding(8));
            
            document.add(statsTable);
            
            // Venue Utilization Section
            com.itextpdf.layout.element.Paragraph venueHeader = new com.itextpdf.layout.element.Paragraph("🏛️ VENUE UTILIZATION")
                .setFontSize(14)
                .setBold()
                .setFontColor(goldColor)
                .setMarginBottom(10);
            document.add(venueHeader);
            
            com.itextpdf.layout.element.Table venueTable = new com.itextpdf.layout.element.Table(new float[]{2, 1})
                .useAllAvailableWidth()
                .setMarginBottom(20);
            
            venueTable.addHeaderCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Venue").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(10));
            venueTable.addHeaderCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Bookings").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(10));
            
            for (Map.Entry<String, Long> entry : venueUtilization.entrySet()) {
                venueTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(entry.getKey())).setPadding(8));
                venueTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(entry.getValue().toString())).setPadding(8));
            }
            
            document.add(venueTable);
            
            // Monthly Trends Section
            com.itextpdf.layout.element.Paragraph monthlyHeader = new com.itextpdf.layout.element.Paragraph("📈 MONTHLY BOOKING TRENDS")
                .setFontSize(14)
                .setBold()
                .setFontColor(goldColor)
                .setMarginBottom(10);
            document.add(monthlyHeader);
            
            com.itextpdf.layout.element.Table monthlyTable = new com.itextpdf.layout.element.Table(new float[]{2, 1})
                .useAllAvailableWidth()
                .setMarginBottom(20);
            
            monthlyTable.addHeaderCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Month").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(10));
            monthlyTable.addHeaderCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Bookings").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(10));
            
            for (Map.Entry<String, Long> entry : monthlyStats.entrySet()) {
                monthlyTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(entry.getKey())).setPadding(8));
                monthlyTable.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(entry.getValue().toString())).setPadding(8));
            }
            
            document.add(monthlyTable);
            
            // Footer
            document.add(new com.itextpdf.layout.element.LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine(1f)).setMarginTop(20));
            
            com.itextpdf.layout.element.Paragraph footer = new com.itextpdf.layout.element.Paragraph(
                    "© 2025 Grand Aura Event Coordination System • Confidential Report")
                .setFontSize(9)
                .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginTop(10);
            document.add(footer);
            
            // Close document
            document.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF report: " + e.getMessage());
        }
    }
}

