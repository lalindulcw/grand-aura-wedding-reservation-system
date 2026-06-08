package org.example.grandaura.controller;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.FrontDeskOfficer;
import org.example.grandaura.entity.UserAccount;
import org.example.grandaura.service.FrontDeskOfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for Front Desk Officer operations
 * Handles check-in/out, guest management, and booking coordination
 */
@Controller
@RequestMapping("/front-desk")
public class FrontDeskOfficerController {

    private final FrontDeskOfficerService frontDeskOfficerService;

    @Autowired
    public FrontDeskOfficerController(FrontDeskOfficerService frontDeskOfficerService) {
        this.frontDeskOfficerService = frontDeskOfficerService;
    }

    /**
     * Front Desk Dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = frontDeskOfficerService.getDashboardStatistics();
        model.addAttribute("stats", stats);
        return "front-desk/dashboard";
    }

    /**
     * Check-In Management
     */
    @GetMapping("/check-in")
    public String checkInManagement(Model model) {
        Map<String, Object> data = frontDeskOfficerService.getCheckInData();
        model.addAttribute("todaysBookings", data.get("todaysBookings"));
        model.addAttribute("upcomingBookings", data.get("upcomingBookings"));
        return "front-desk/checkIn";
    }

    /**
     * View booking details for check-in
     */
    @GetMapping("/check-in/{id}")
    public String viewBookingForCheckIn(@PathVariable Long id, Model model) {
        Optional<Booking> booking = frontDeskOfficerService.getBookingById(id);
        
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "front-desk/bookingDetails";
        } else {
            return "redirect:/front-desk/check-in?error=Booking not found";
        }
    }

    /**
     * Guest Management
     */
    @GetMapping("/guests")
    public String guestManagement(Model model) {
        List<UserAccount> customers = frontDeskOfficerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "front-desk/guests";
    }

    /**
     * View guest details
     */
    @GetMapping("/guests/{email}")
    public String viewGuestDetails(@PathVariable String email, Model model) {
        Optional<UserAccount> customer = frontDeskOfficerService.getCustomerByEmail(email);
        
        if (customer.isPresent()) {
            model.addAttribute("customer", customer.get());
            
            // Get customer's bookings
            List<Booking> bookings = frontDeskOfficerService.getAllBookings().stream()
                    .filter(b -> b.getCustomerEmail() != null && b.getCustomerEmail().equals(email))
                    .toList();
            model.addAttribute("bookings", bookings);
            
            return "front-desk/guestDetails";
        } else {
            return "redirect:/front-desk/guests?error=Guest not found";
        }
    }

    /**
     * Booking Management
     */
    @GetMapping("/bookings")
    public String bookingManagement(@RequestParam(required = false) String date, Model model) {
        List<Booking> bookings;
        
        if (date != null && !date.isEmpty()) {
            LocalDate searchDate = LocalDate.parse(date);
            Map<String, Object> data = frontDeskOfficerService.getBookingsByDate(searchDate);
            Object bookingsObj = data.get("bookings");
            bookings = bookingsObj instanceof List ? (List<Booking>) bookingsObj : List.of();
            model.addAttribute("selectedDate", searchDate);
        } else {
            bookings = frontDeskOfficerService.getAllBookings();
        }
        
        model.addAttribute("bookings", bookings);
        
        // Calculate statistics
        int totalGuests = bookings.stream().mapToInt(Booking::getGuestCount).sum();
        int avgGuestCount = bookings.isEmpty() ? 0 : totalGuests / bookings.size();
        long upcomingCount = bookings.stream()
                .filter(b -> b.getWeddingDate() != null && 
                       b.getWeddingDate().isAfter(LocalDate.now()) && 
                       b.getWeddingDate().isBefore(LocalDate.now().plusDays(8)))
                .count();
        
        model.addAttribute("totalGuests", totalGuests);
        model.addAttribute("avgGuestCount", avgGuestCount);
        model.addAttribute("upcomingCount", upcomingCount);
        
        return "front-desk/bookings";
    }

    /**
     * View booking details
     */
    @GetMapping("/bookings/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Optional<Booking> booking = frontDeskOfficerService.getBookingById(id);
        
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "front-desk/bookingDetails";
        } else {
            return "redirect:/front-desk/bookings?error=Booking not found";
        }
    }

    /**
     * Show Edit Booking Form
     */
    @GetMapping("/bookings/{id}/edit")
    public String showEditBookingForm(@PathVariable Long id, Model model) {
        Optional<Booking> booking = frontDeskOfficerService.getBookingById(id);
        
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "front-desk/editBooking";
        } else {
            return "redirect:/front-desk/bookings?error=Booking not found";
        }
    }

    /**
     * Update Booking
     */
    @PostMapping("/bookings/{id}/update")
    public String updateBooking(@PathVariable Long id,
                               @RequestParam int guestCount,
                               @RequestParam(required = false) String specialRequests,
                               Model model) {
        Optional<Booking> bookingOpt = frontDeskOfficerService.getBookingById(id);
        
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setGuestCount(guestCount);
            booking.setSpecialRequests(specialRequests);
            
            frontDeskOfficerService.updateBooking(booking);
            model.addAttribute("success", "Booking updated successfully!");
            model.addAttribute("booking", booking);
            return "redirect:/front-desk/bookings/" + id + "?success=updated";
        } else {
            return "redirect:/front-desk/bookings?error=Booking not found";
        }
    }

    /**
     * Process Check-In
     */
    @PostMapping("/bookings/{id}/check-in")
    public String processCheckIn(@PathVariable Long id) {
        Booking booking = frontDeskOfficerService.checkInBooking(id);
        
        if (booking != null) {
            return "redirect:/front-desk/bookings/" + id + "?success=checkedin";
        } else {
            return "redirect:/front-desk/bookings/" + id + "?error=Failed to check in";
        }
    }

    /**
     * Process Check-Out
     */
    @PostMapping("/bookings/{id}/check-out")
    public String processCheckOut(@PathVariable Long id) {
        Booking booking = frontDeskOfficerService.checkOutBooking(id);
        
        if (booking != null) {
            return "redirect:/front-desk/bookings/" + id + "?success=checkedout";
        } else {
            return "redirect:/front-desk/bookings/" + id + "?error=Failed to check out";
        }
    }

    /**
     * Update Booking Status
     */
    @PostMapping("/bookings/{id}/update-status")
    public String updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        Booking booking = frontDeskOfficerService.updateBookingStatus(id, status);
        
        if (booking != null) {
            return "redirect:/front-desk/bookings/" + id + "?success=statusupdated";
        } else {
            return "redirect:/front-desk/bookings/" + id + "?error=Failed to update status";
        }
    }

    /**
     * Add Front Desk Notes
     */
    @PostMapping("/bookings/{id}/add-notes")
    public String addFrontDeskNotes(@PathVariable Long id, @RequestParam String notes) {
        Booking booking = frontDeskOfficerService.addFrontDeskNotes(id, notes);
        
        if (booking != null) {
            return "redirect:/front-desk/bookings/" + id + "?success=notesadded";
        } else {
            return "redirect:/front-desk/bookings/" + id + "?error=Failed to add notes";
        }
    }

    /**
     * Analytics and Reports
     */
    @GetMapping("/analytics")
    public String analytics(Model model) {
        Map<String, Object> analytics = frontDeskOfficerService.getGuestAnalytics();
        model.addAttribute("analytics", analytics);
        return "front-desk/analytics";
    }

    /**
     * Front Desk Officer Profile
     */
    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        String email = auth.getName();
        Optional<FrontDeskOfficer> officer = frontDeskOfficerService.findByEmail(email);
        
        if (officer.isPresent()) {
            model.addAttribute("officer", officer.get());
        }
        return "front-desk/profile";
    }

    /**
     * Update Profile
     */
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String officerName,
                               @RequestParam String email,
                               @RequestParam(required = false) String shift,
                               @RequestParam(required = false) String department,
                               @RequestParam(required = false) String phoneNumber,
                               @RequestParam(required = false) String employeeId,
                               @RequestParam(required = false) String languages,
                               @RequestParam(required = false) String accessLevel,
                               Model model) {
        try {
            List<FrontDeskOfficer> allOfficers = frontDeskOfficerService.getAllFrontDeskOfficers();
            FrontDeskOfficer existingOfficer = null;
            
            if (!allOfficers.isEmpty()) {
                existingOfficer = allOfficers.get(0);
            }
            
            if (existingOfficer != null) {
                existingOfficer.setOfficerName(officerName);
                existingOfficer.setEmail(email);
                existingOfficer.setShift(shift);
                existingOfficer.setDepartment(department);
                existingOfficer.setPhoneNumber(phoneNumber);
                existingOfficer.setEmployeeId(employeeId);
                existingOfficer.setLanguages(languages);
                existingOfficer.setAccessLevel(accessLevel);
                
                frontDeskOfficerService.saveFrontDeskOfficer(existingOfficer);
                model.addAttribute("officer", existingOfficer);
                model.addAttribute("message", "Profile updated successfully! If you changed your email, please logout and login again with your new email address.");
            } else {
                model.addAttribute("error", "Officer not found");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            List<FrontDeskOfficer> allOfficers = frontDeskOfficerService.getAllFrontDeskOfficers();
            if (!allOfficers.isEmpty()) {
                model.addAttribute("officer", allOfficers.get(0));
            }
        }
        return "front-desk/profile";
    }
}

