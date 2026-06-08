package org.example.grandaura.service;

import org.example.grandaura.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking saveBooking(Booking booking);
    Optional<Booking> getBookingById(Long id);
    List<Booking> getAllBookings();
    void deleteBooking(Long id);
    List<Booking> getBookingsForCustomer(String customerEmail);
}