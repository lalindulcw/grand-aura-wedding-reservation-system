package org.example.grandaura.controller;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.Menu;
import org.example.grandaura.repository.MenuRepository;
import org.example.grandaura.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * This controller handles both the Thymeleaf-based UI for creating bookings and REST API endpoints
 * for managing bookings. The base path is set to /api/bookings.
 */
@Controller
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final MenuRepository menuRepository;

    /**
     * Constructor-based dependency injection for BookingService.
     * @param bookingService The service layer for booking operations.
     * @param menuRepository The repository for menu operations.
     */
    @Autowired
    public BookingController(BookingService bookingService, MenuRepository menuRepository) {
        this.bookingService = bookingService;
        this.menuRepository = menuRepository;
    }

    /**
     * Displays the form for creating a new booking.
     * @param model The model to add the new Booking object for the form.
     * @return The name of the Thymeleaf template (createBooking).
     */
    // UI route moved to BookingUiController at /bookings/create

    /**
     * Handles the submission of a new booking form, saves it to the database, and redirects
     * back to the create-booking page with a success message.
     * @param booking The booking data from the form.
     * @param redirectAttributes Used to pass the flash message after redirect.
     * @return Redirect to the create-booking page.
     */
    @PostMapping
    public String createBooking(@ModelAttribute("booking") @Valid Booking booking, 
                              @RequestParam(value = "selectedMenuItems", required = false) String[] selectedMenuItems,
                              BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("booking", booking);
            return "createBooking";
        }
        
        // Process selected menu items
        if (selectedMenuItems != null && selectedMenuItems.length > 0) {
            String menuItemsString = String.join(", ", selectedMenuItems);
            booking.setSelectedMenuItems(menuItemsString);
        }
        
        Booking savedBooking = bookingService.saveBooking(booking);
        redirectAttributes.addFlashAttribute("bookingId", savedBooking.getId());
        return "redirect:/bookings/success";
    }

    // UI: list bookings page
    // List/success UI routes moved to BookingUiController

    // UI: edit page
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Booking not found");
            return "redirect:/bookings/my";
        }
        
        // Get available menu items
        List<Menu> availableMenus = menuRepository.findByIsAvailableTrue();
        model.addAttribute("booking", booking.get());
        model.addAttribute("availableMenus", availableMenus);
        return "editBooking";
    }

    // UI: update action
    @PostMapping("/{id}/edit")
    public String updateBookingForm(@PathVariable Long id, 
                                  @ModelAttribute("booking") @Valid Booking booking, 
                                  @RequestParam(value = "selectedMenuItems", required = false) String[] selectedMenuItems,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("booking", booking);
            return "editBooking";
        }
        if (!bookingService.getBookingById(id).isPresent()) {
            redirectAttributes.addFlashAttribute("message", "Booking not found");
            return "redirect:/bookings/my";
        }
        
        // Process selected menu items
        if (selectedMenuItems != null && selectedMenuItems.length > 0) {
            String menuItemsString = String.join(", ", selectedMenuItems);
            booking.setSelectedMenuItems(menuItemsString);
        }
        
        booking.setId(id);
        bookingService.saveBooking(booking);
        redirectAttributes.addFlashAttribute("message", "Booking updated");
        return "redirect:/bookings/my";
    }

    // UI: delete
    @PostMapping("/{id}/delete")
    public String deleteBookingForm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.deleteBooking(id);
        redirectAttributes.addFlashAttribute("message", "Booking deleted");
        return "redirect:/bookings/my";
    }

    /**
     * Retrieves a booking by ID via REST API.
     * @param id The ID of the booking to retrieve.
     * @return ResponseEntity with the booking or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all bookings via REST API.
     * @return ResponseEntity with a list of all bookings.
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    /**
     * Deletes a booking by ID via REST API.
     * @param id The ID of the booking to delete.
     * @return ResponseEntity with no content (204) on success.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates a booking by ID via REST API.
     * @param id The ID of the booking to update.
     * @param booking The updated booking data.
     * @return ResponseEntity with the updated booking or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        if (!bookingService.getBookingById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        booking.setId(id);
        Booking updatedBooking = bookingService.saveBooking(booking);
        return ResponseEntity.ok(updatedBooking);
    }
}