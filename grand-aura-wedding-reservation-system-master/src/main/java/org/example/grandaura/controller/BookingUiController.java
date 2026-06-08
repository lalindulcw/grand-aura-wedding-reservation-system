package org.example.grandaura.controller;

import jakarta.servlet.http.HttpSession;
import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.Menu;
import org.example.grandaura.repository.MenuRepository;
import org.example.grandaura.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingUiController {

    private final BookingService bookingService;
    private final MenuRepository menuRepository;

    @Autowired
    public BookingUiController(BookingService bookingService, MenuRepository menuRepository) {
        this.bookingService = bookingService;
        this.menuRepository = menuRepository;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, Authentication auth) {
        Booking booking = new Booking();
        if (auth != null && auth.isAuthenticated()) {
            booking.setCustomerEmail(auth.getName());
        }
        
        // Get available menu items grouped by category
        List<Menu> availableMenus = menuRepository.findByIsAvailableTrue();
        
        model.addAttribute("booking", booking);
        model.addAttribute("availableMenus", availableMenus);
        
        return "createBooking";
    }

    @GetMapping("/my")
    public String myBookings(Model model, Authentication auth) {
        String customerEmail = auth != null ? auth.getName() : null;
        List<Booking> bookings = (customerEmail != null)
            ? bookingService.getBookingsForCustomer(customerEmail)
            : List.of();
        model.addAttribute("bookings", bookings);
        model.addAttribute("customerEmail", customerEmail);
        return "myBookings";
    }

    @GetMapping("/success")
    public String success(@RequestParam(value = "id", required = false) Long id, Model model) {
        // Check for booking ID from query parameter first, then flash attributes
        Long bookingId = id != null ? id : (Long) model.asMap().get("bookingId");
        if (bookingId != null) {
            Booking booking = bookingService.getBookingById(bookingId).orElse(null);
            if (booking != null) {
                model.addAttribute("booking", booking);
            }
        }
        return "bookingSuccess";
    }

    // Allow switching the session's active customer email
    @GetMapping("/switch")
    public String switchCustomer(@RequestParam("email") String email, HttpSession session) {
        session.setAttribute("customerEmail", email);
        return "redirect:/bookings/my";
    }
}


