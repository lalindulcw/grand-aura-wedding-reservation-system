package org.example.grandaura.service;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.HotelOwner;
import org.example.grandaura.repository.BookingRepository;
import org.example.grandaura.repository.HotelOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for Hotel Owner operations
 * Provides analytics, management, and reporting capabilities
 */
@Service
public class HotelOwnerService {

    private final HotelOwnerRepository hotelOwnerRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public HotelOwnerService(HotelOwnerRepository hotelOwnerRepository, BookingRepository bookingRepository) {
        this.hotelOwnerRepository = hotelOwnerRepository;
        this.bookingRepository = bookingRepository;
    }

    // Hotel Owner Management Methods
    
    /**
     * Save or update hotel owner
     */
    public HotelOwner saveHotelOwner(HotelOwner hotelOwner) {
        return hotelOwnerRepository.save(hotelOwner);
    }

    /**
     * Find hotel owner by email
     */
    public Optional<HotelOwner> findByEmail(String email) {
        return hotelOwnerRepository.findByEmail(email);
    }

    /**
     * Get all hotel owners
     */
    public List<HotelOwner> getAllHotelOwners() {
        return hotelOwnerRepository.findAll();
    }

    /**
     * Delete hotel owner by ID
     */
    public void deleteHotelOwner(Long id) {
        hotelOwnerRepository.deleteById(id);
    }

    // Analytics and Reporting Methods

    /**
     * Get all bookings for hotel owner dashboard
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
     * Save or update booking
     */
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    /**
     * Delete booking by ID
     */
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
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
     * Get bookings by venue
     */
    public List<Booking> getBookingsByVenue(String venue) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getVenue().equals(venue))
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
     * Get upcoming bookings (wedding date >= today)
     */
    public List<Booking> getUpcomingBookings() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findAll().stream()
                .filter(booking -> !booking.getWeddingDate().isBefore(today))
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
     * Get average guest count per booking
     */
    public double getAverageGuestCount() {
        return bookingRepository.findAll().stream()
                .mapToInt(Booking::getGuestCount)
                .average()
                .orElse(0.0);
    }

    /**
     * Get total revenue estimate (assuming base price per guest)
     */
    public double getEstimatedRevenue(double pricePerGuest) {
        return getTotalGuests() * pricePerGuest;
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
     * Get booking statistics summary
     */
    public Map<String, Object> getBookingStatistics() {
        return Map.of(
                "totalBookings", getTotalBookings(),
                "totalGuests", getTotalGuests(),
                "averageGuestCount", getAverageGuestCount(),
                "upcomingBookings", getUpcomingBookings().size(),
                "mostPopularVenue", getMostPopularVenue(),
                "venueUtilization", getVenueUtilization(),
                "monthlyStats", getMonthlyBookingStats()
        );
    }
}
