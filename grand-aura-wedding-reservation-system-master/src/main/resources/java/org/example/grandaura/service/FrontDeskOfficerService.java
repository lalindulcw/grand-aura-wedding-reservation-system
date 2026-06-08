package org.example.grandaura.service;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.FrontDeskOfficer;
import org.example.grandaura.entity.UserAccount;
import org.example.grandaura.repository.BookingRepository;
import org.example.grandaura.repository.FrontDeskOfficerRepository;
import org.example.grandaura.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for Front Desk Officer operations
 * Handles check-in/out, guest management, and booking coordination
 */
@Service
public class FrontDeskOfficerService {

    private final FrontDeskOfficerRepository frontDeskOfficerRepository;
    private final BookingRepository bookingRepository;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public FrontDeskOfficerService(FrontDeskOfficerRepository frontDeskOfficerRepository,
                                  BookingRepository bookingRepository,
                                  UserAccountRepository userAccountRepository) {
        this.frontDeskOfficerRepository = frontDeskOfficerRepository;
        this.bookingRepository = bookingRepository;
        this.userAccountRepository = userAccountRepository;
    }

    // Front Desk Officer Management

    public FrontDeskOfficer saveFrontDeskOfficer(FrontDeskOfficer officer) {
        return frontDeskOfficerRepository.save(officer);
    }

    public Optional<FrontDeskOfficer> findByEmail(String email) {
        return frontDeskOfficerRepository.findByEmail(email);
    }

    public List<FrontDeskOfficer> getAllFrontDeskOfficers() {
        return frontDeskOfficerRepository.findAll();
    }

    public void deleteFrontDeskOfficer(Long id) {
        frontDeskOfficerRepository.deleteById(id);
    }

    // Booking Management

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getTodaysCheckIns() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getWeddingDate().isEqual(today))
                .collect(Collectors.toList());
    }

    public List<Booking> getUpcomingBookings() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getWeddingDate().isAfter(today))
                .sorted(Comparator.comparing(Booking::getWeddingDate))
                .collect(Collectors.toList());
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    /**
     * Process Check-In
     */
    public Booking checkInBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setBookingStatus("Checked In");
            booking.setCheckInTime(java.time.LocalDateTime.now());
            return bookingRepository.save(booking);
        }
        return null;
    }

    /**
     * Process Check-Out
     */
    public Booking checkOutBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setBookingStatus("Completed");
            booking.setCheckOutTime(java.time.LocalDateTime.now());
            return bookingRepository.save(booking);
        }
        return null;
    }

    /**
     * Update Booking Status
     */
    public Booking updateBookingStatus(Long bookingId, String status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setBookingStatus(status);
            return bookingRepository.save(booking);
        }
        return null;
    }

    /**
     * Add Front Desk Notes
     */
    public Booking addFrontDeskNotes(Long bookingId, String notes) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            String existingNotes = booking.getFrontDeskNotes();
            if (existingNotes != null && !existingNotes.isEmpty()) {
                booking.setFrontDeskNotes(existingNotes + "\n\n" + notes);
            } else {
                booking.setFrontDeskNotes(notes);
            }
            return bookingRepository.save(booking);
        }
        return null;
    }

    // Guest/Customer Management

    public List<UserAccount> getAllCustomers() {
        return userAccountRepository.findAll();
    }

    public Optional<UserAccount> getCustomerByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    public UserAccount updateCustomer(UserAccount customer) {
        return userAccountRepository.save(customer);
    }

    // Dashboard Statistics

    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Booking> allBookings = getAllBookings();
        LocalDate today = LocalDate.now();
        
        // Today's check-ins
        long todaysCheckIns = allBookings.stream()
                .filter(b -> b.getWeddingDate().isEqual(today))
                .count();
        
        // Upcoming bookings (next 7 days)
        long upcomingBookings = allBookings.stream()
                .filter(b -> b.getWeddingDate().isAfter(today) && 
                           b.getWeddingDate().isBefore(today.plusDays(8)))
                .count();
        
        // Total active customers
        long totalCustomers = userAccountRepository.count();
        
        // Total bookings
        long totalBookings = allBookings.size();
        
        stats.put("todaysCheckIns", todaysCheckIns);
        stats.put("upcomingBookings", upcomingBookings);
        stats.put("totalCustomers", totalCustomers);
        stats.put("totalBookings", totalBookings);
        
        return stats;
    }

    // Check-in/Check-out Management

    public Map<String, Object> getCheckInData() {
        Map<String, Object> data = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate weekFromNow = today.plusDays(7);
        
        // Today's check-ins
        List<Booking> todaysBookings = getAllBookings().stream()
                .filter(b -> b.getWeddingDate() != null && b.getWeddingDate().isEqual(today))
                .collect(Collectors.toList());
        
        // Upcoming bookings (next 7 days, excluding today)
        List<Booking> upcomingBookings = getAllBookings().stream()
                .filter(b -> b.getWeddingDate() != null && 
                           b.getWeddingDate().isAfter(today) && 
                           b.getWeddingDate().isBefore(weekFromNow.plusDays(1)))
                .sorted(Comparator.comparing(Booking::getWeddingDate))
                .collect(Collectors.toList());
        
        data.put("todaysBookings", todaysBookings);
        data.put("upcomingBookings", upcomingBookings);
        
        return data;
    }

    // Booking Confirmation

    public Map<String, Object> getBookingsByDate(LocalDate date) {
        Map<String, Object> data = new HashMap<>();
        
        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(b -> b.getWeddingDate().isEqual(date))
                .collect(Collectors.toList());
        
        data.put("bookings", bookings);
        data.put("date", date);
        data.put("count", bookings.size());
        
        return data;
    }

    // Guest Services Analytics

    public Map<String, Object> getGuestAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        List<Booking> allBookings = getAllBookings();
        
        // Basic Statistics
        int totalBookings = allBookings.size();
        int totalGuests = allBookings.stream().mapToInt(Booking::getGuestCount).sum();
        int avgGuestCount = totalBookings > 0 ? totalGuests / totalBookings : 0;
        long totalCustomers = userAccountRepository.count();
        
        analytics.put("totalBookings", totalBookings);
        analytics.put("totalGuests", totalGuests);
        analytics.put("avgGuestCount", avgGuestCount);
        analytics.put("totalCustomers", totalCustomers);
        
        // Venue Statistics
        Map<String, Long> venuePopularity = allBookings.stream()
                .collect(Collectors.groupingBy(
                    booking -> booking.getPreferredVenue() != null ? booking.getPreferredVenue() : booking.getVenue(),
                    Collectors.counting()
                ));
        
        // Convert to list of VenueStat objects
        List<Map<String, Object>> venueStats = new ArrayList<>();
        long maxBookings = venuePopularity.values().stream().max(Long::compareTo).orElse(1L);
        
        for (Map.Entry<String, Long> entry : venuePopularity.entrySet()) {
            Map<String, Object> stat = new HashMap<>();
            String venueName = entry.getKey();
            Long bookingCount = entry.getValue();
            
            // Calculate total guests and average for this venue
            List<Booking> venueBookings = allBookings.stream()
                    .filter(b -> {
                        String venue = b.getPreferredVenue() != null ? b.getPreferredVenue() : b.getVenue();
                        return venue.equals(venueName);
                    })
                    .collect(Collectors.toList());
            
            int venueTotalGuests = venueBookings.stream().mapToInt(Booking::getGuestCount).sum();
            int venueAvgGuests = bookingCount > 0 ? (int)(venueTotalGuests / bookingCount) : 0;
            int popularityPercent = (int)((bookingCount * 100.0) / maxBookings);
            
            stat.put("venueName", venueName);
            stat.put("bookingCount", bookingCount);
            stat.put("totalGuests", venueTotalGuests);
            stat.put("avgGuests", venueAvgGuests);
            stat.put("popularityPercent", popularityPercent);
            
            venueStats.add(stat);
        }
        
        // Sort by booking count descending
        venueStats.sort((a, b) -> Long.compare((Long)b.get("bookingCount"), (Long)a.get("bookingCount")));
        
        analytics.put("venueStats", venueStats);
        
        // Catering Package Statistics
        Map<String, Long> cateringPackages = allBookings.stream()
                .filter(b -> b.getCateringPackage() != null && !b.getCateringPackage().isEmpty())
                .collect(Collectors.groupingBy(Booking::getCateringPackage, Collectors.counting()));
        
        // Convert to list of CateringStat objects
        List<Map<String, Object>> cateringStats = new ArrayList<>();
        long totalCateringBookings = cateringPackages.values().stream().mapToLong(Long::longValue).sum();
        
        for (Map.Entry<String, Long> entry : cateringPackages.entrySet()) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("packageName", entry.getKey());
            stat.put("count", entry.getValue());
            int percentage = totalCateringBookings > 0 ? (int)((entry.getValue() * 100.0) / totalCateringBookings) : 0;
            stat.put("percentage", percentage);
            cateringStats.add(stat);
        }
        
        // Sort by count descending
        cateringStats.sort((a, b) -> Long.compare((Long)b.get("count"), (Long)a.get("count")));
        
        analytics.put("cateringStats", cateringStats);
        
        // Recent Bookings (last 10)
        List<Booking> recentBookings = allBookings.stream()
                .sorted(Comparator.comparing(Booking::getWeddingDate).reversed())
                .limit(10)
                .collect(Collectors.toList());
        
        analytics.put("recentBookings", recentBookings);
        
        return analytics;
    }
}

