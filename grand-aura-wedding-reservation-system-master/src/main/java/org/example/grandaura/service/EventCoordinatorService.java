package org.example.grandaura.service;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.EventCoordinator;
import org.example.grandaura.repository.BookingRepository;
import org.example.grandaura.repository.EventCoordinatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for Event Coordinator operations
 * Provides event coordination, customer management, and planning capabilities
 */
@Service
public class EventCoordinatorService {

    private final EventCoordinatorRepository eventCoordinatorRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public EventCoordinatorService(EventCoordinatorRepository eventCoordinatorRepository, 
                                   BookingRepository bookingRepository) {
        this.eventCoordinatorRepository = eventCoordinatorRepository;
        this.bookingRepository = bookingRepository;
    }

    // Event Coordinator Management Methods
    
    /**
     * Save or update event coordinator
     */
    public EventCoordinator saveEventCoordinator(EventCoordinator eventCoordinator) {
        return eventCoordinatorRepository.save(eventCoordinator);
    }

    /**
     * Find event coordinator by email
     */
    public Optional<EventCoordinator> findByEmail(String email) {
        return eventCoordinatorRepository.findByEmail(email);
    }

    /**
     * Get all event coordinators
     */
    public List<EventCoordinator> getAllEventCoordinators() {
        return eventCoordinatorRepository.findAll();
    }

    /**
     * Delete event coordinator by ID
     */
    public void deleteEventCoordinator(Long id) {
        eventCoordinatorRepository.deleteById(id);
    }

    // Event Management Methods

    /**
     * Get all bookings for event coordination
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Get booking by ID
     */
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    /**
     * Get upcoming bookings (wedding date >= today)
     */
    public List<Booking> getUpcomingBookings() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findAll().stream()
                .filter(booking -> !booking.getWeddingDate().isBefore(today))
                .toList();
    }

    /**
     * Get bookings by date range
     */
    public List<Booking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findAll().stream()
                .filter(booking -> !booking.getWeddingDate().isBefore(startDate) 
                        && !booking.getWeddingDate().isAfter(endDate))
                .toList();
    }

    /**
     * Get bookings by venue
     */
    public List<Booking> getBookingsByVenue(String venue) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getVenue().equals(venue))
                .toList();
    }

    /**
     * Get bookings with special requests
     */
    public List<Booking> getBookingsWithSpecialRequests() {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getSpecialRequests() != null && !booking.getSpecialRequests().trim().isEmpty())
                .toList();
    }

    /**
     * Get venue utilization statistics
     */
    public Map<String, Long> getVenueUtilization() {
        return bookingRepository.findAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Booking::getVenue,
                        java.util.stream.Collectors.counting()
                ));
    }

    /**
     * Get monthly booking statistics
     */
    public Map<String, Long> getMonthlyBookingStats() {
        return bookingRepository.findAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        booking -> booking.getWeddingDate().getMonth().toString(),
                        java.util.stream.Collectors.counting()
                ));
    }

    /**
     * Get total number of bookings
     */
    public long getTotalBookings() {
        return bookingRepository.count();
    }

    /**
     * Get total guest count across all bookings
     */
    public int getTotalGuests() {
        return bookingRepository.findAll().stream()
                .mapToInt(Booking::getGuestCount)
                .sum();
    }

    /**
     * Get average guest count per booking
     */
    public double getAverageGuestCount() {
        return bookingRepository.findAll().stream()
                .mapToInt(Booking::getGuestCount)
                .average()
                .orElse(0.0);
    }

    /**
     * Get most popular venue
     */
    public String getMostPopularVenue() {
        return getVenueUtilization().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No bookings yet");
    }

    /**
     * Get event coordination statistics
     */
    public Map<String, Object> getEventCoordinationStatistics() {
        return Map.of(
                "totalBookings", getTotalBookings(),
                "totalGuests", getTotalGuests(),
                "averageGuestCount", getAverageGuestCount(),
                "upcomingBookings", getUpcomingBookings().size(),
                "mostPopularVenue", getMostPopularVenue(),
                "venueUtilization", getVenueUtilization(),
                "monthlyStats", getMonthlyBookingStats(),
                "bookingsWithSpecialRequests", getBookingsWithSpecialRequests().size()
        );
    }

    /**
     * Update booking (for event coordinator edits)
     */
    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    /**
     * Save booking
     */
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
}

