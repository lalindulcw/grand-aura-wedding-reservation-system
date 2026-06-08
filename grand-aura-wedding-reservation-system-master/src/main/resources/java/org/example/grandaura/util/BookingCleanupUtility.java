package org.example.grandaura.util;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility class to clean up old bookings without catering options
 * This can be run independently to clean up the database
 */
@Component
public class BookingCleanupUtility implements CommandLineRunner {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only run cleanup if specifically requested
        if (args.length > 0 && "cleanup-bookings".equals(args[0])) {
            cleanupOldBookings();
        }
    }

    public void cleanupOldBookings() {
        System.out.println("🧹 Starting cleanup of old bookings without catering options...");
        
        // Find all bookings without catering information
        List<Booking> allBookings = bookingRepository.findAll();
        List<Booking> oldBookings = allBookings.stream()
            .filter(booking -> 
                (booking.getBrideName() == null || booking.getBrideName().isEmpty()) &&
                (booking.getGroomName() == null || booking.getGroomName().isEmpty()) &&
                (booking.getPreferredVenue() == null || booking.getPreferredVenue().isEmpty()) &&
                (booking.getCateringPackage() == null || booking.getCateringPackage().isEmpty()) &&
                (booking.getDietaryRequirements() == null || booking.getDietaryRequirements().isEmpty()) &&
                (booking.getSpecialCateringRequests() == null || booking.getSpecialCateringRequests().isEmpty()) &&
                (booking.getEstimatedGuestCount() == null || booking.getEstimatedGuestCount() == 0)
            )
            .collect(java.util.stream.Collectors.toList());
        
        System.out.println("📊 Found " + allBookings.size() + " total bookings");
        System.out.println("🗑️ Found " + oldBookings.size() + " old bookings to delete");
        
        if (!oldBookings.isEmpty()) {
            System.out.println("📋 Old bookings to be deleted:");
            oldBookings.forEach(booking -> 
                System.out.println("  - ID: " + booking.getId() + 
                    ", Customer: " + booking.getCustomerName() + 
                    ", Date: " + booking.getWeddingDate() + 
                    ", Venue: " + booking.getVenue())
            );
            
            // Delete the old bookings
            bookingRepository.deleteAll(oldBookings);
            System.out.println("✅ Successfully deleted " + oldBookings.size() + " old bookings");
        } else {
            System.out.println("✅ No old bookings found to delete");
        }
        
        // Show remaining bookings
        List<Booking> remainingBookings = bookingRepository.findAll();
        System.out.println("📊 Remaining bookings: " + remainingBookings.size());
        
        if (!remainingBookings.isEmpty()) {
            System.out.println("📋 Remaining bookings with catering info:");
            remainingBookings.forEach(booking -> 
                System.out.println("  - ID: " + booking.getId() + 
                    ", Customer: " + booking.getCustomerName() + 
                    ", Bride: " + (booking.getBrideName() != null ? booking.getBrideName() : "N/A") +
                    ", Groom: " + (booking.getGroomName() != null ? booking.getGroomName() : "N/A") +
                    ", Venue: " + (booking.getPreferredVenue() != null ? booking.getPreferredVenue() : booking.getVenue()))
            );
        }
        
        System.out.println("🎉 Cleanup completed!");
    }
}


