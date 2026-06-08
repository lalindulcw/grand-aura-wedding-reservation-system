package org.example.grandaura.controller;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.HotelOwner;
import org.example.grandaura.service.HotelOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for Hotel Owner operations
 * Handles dashboard, analytics, and management functionality
 */
@Controller
@RequestMapping("/hotel-owner")
public class HotelOwnerController {

    private final HotelOwnerService hotelOwnerService;

    @Autowired
    public HotelOwnerController(HotelOwnerService hotelOwnerService) {
        this.hotelOwnerService = hotelOwnerService;
    }

    /**
     * Hotel Owner Dashboard - Main overview page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Get key statistics for dashboard
        long totalBookings = hotelOwnerService.getTotalBookings();
        int totalGuests = hotelOwnerService.getTotalGuests();
        double averageGuests = hotelOwnerService.getAverageGuestCount();
        List<Booking> upcomingBookings = hotelOwnerService.getUpcomingBookings();
        String mostPopularVenue = hotelOwnerService.getMostPopularVenue();
        
        // Get recent bookings (last 10)
        List<Booking> recentBookings = hotelOwnerService.getAllBookings().stream()
                .limit(10)
                .toList();

        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalGuests", totalGuests);
        model.addAttribute("averageGuests", String.format("%.1f", averageGuests));
        model.addAttribute("upcomingBookings", upcomingBookings.size());
        model.addAttribute("mostPopularVenue", mostPopularVenue);
        model.addAttribute("recentBookings", recentBookings);
        
        return "hotel-owner/dashboard";
    }

    /**
     * All Bookings Management Page
     */
    @GetMapping("/bookings")
    public String allBookings(
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        
        List<Booking> bookings;
        
        if (venue != null && !venue.isEmpty()) {
            bookings = hotelOwnerService.getBookingsByVenue(venue);
        } else if (startDate != null && endDate != null) {
            bookings = hotelOwnerService.getBookingsByDateRange(startDate, endDate);
        } else {
            bookings = hotelOwnerService.getAllBookings();
        }
        
        model.addAttribute("bookings", bookings);
        model.addAttribute("selectedVenue", venue);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "hotel-owner/allBookings";
    }

    /**
     * Analytics and Reports Page
     */
    @GetMapping("/analytics")
    public String analytics(Model model) {
        Map<String, Object> statistics = hotelOwnerService.getBookingStatistics();
        Map<String, Long> venueUtilization = hotelOwnerService.getVenueUtilization();
        Map<String, Long> monthlyStats = hotelOwnerService.getMonthlyBookingStats();
        
        model.addAttribute("statistics", statistics);
        model.addAttribute("venueUtilization", venueUtilization);
        model.addAttribute("monthlyStats", monthlyStats);
        
        return "hotel-owner/analytics";
    }

    /**
     * Venue Management Page
     */
    @GetMapping("/venues")
    public String venueManagement(Model model) {
        Map<String, Long> venueUtilization = hotelOwnerService.getVenueUtilization();
        List<String> availableVenues = List.of(
            "Chequerboard - Up to 1000 pax",
            "Jubilee Ballroom - 150 to 200 pax", 
            "Grand Ballroom - 250 to 280 pax"
        );
        
        model.addAttribute("venueUtilization", venueUtilization);
        model.addAttribute("availableVenues", availableVenues);
        
        return "hotel-owner/venues";
    }

    /**
     * View specific booking details
     */
    @GetMapping("/bookings/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Optional<Booking> booking = hotelOwnerService.getBookingById(id);
        
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "hotel-owner/bookingDetails";
        } else {
            return "redirect:/hotel-owner/bookings?error=Booking not found";
        }
    }

    /**
     * Export bookings data (placeholder for future CSV export)
     */
    @GetMapping("/export")
    public String exportBookings(Model model) {
        List<Booking> allBookings = hotelOwnerService.getAllBookings();
        model.addAttribute("bookings", allBookings);
        model.addAttribute("exportDate", LocalDate.now());
        return "hotel-owner/export";
    }

    /**
     * Export bookings to CSV
     */
    @GetMapping("/export/csv")
    public void exportBookingsToCSV(HttpServletResponse response) throws IOException {
        List<Booking> bookings = hotelOwnerService.getAllBookings();
        
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"grand-aura-bookings-" + LocalDate.now() + ".csv\"");
        
        PrintWriter writer = response.getWriter();
        
        // Write CSV header
        writer.println("ID,Customer Name,Email,Venue,Wedding Date,Guest Count,Special Requests");
        
        // Write booking data
        for (Booking booking : bookings) {
            writer.println(String.format("%d,\"%s\",\"%s\",\"%s\",%s,%d,\"%s\"",
                booking.getId(),
                booking.getCustomerName(),
                booking.getCustomerEmail(),
                booking.getVenue(),
                booking.getWeddingDate(),
                booking.getGuestCount(),
                booking.getSpecialRequests() != null ? booking.getSpecialRequests().replace("\"", "\"\"") : ""
            ));
        }
        
        writer.flush();
        writer.close();
    }

    /**
     * Export bookings to Excel
     */
    @GetMapping("/export/excel")
    public void exportBookingsToExcel(HttpServletResponse response) throws IOException {
        List<Booking> bookings = hotelOwnerService.getAllBookings();
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"grand-aura-bookings-" + LocalDate.now() + ".xlsx\"");
        
        // Create Excel content (simplified version)
        StringBuilder excelContent = new StringBuilder();
        excelContent.append("<?xml version=\"1.0\"?>\n");
        excelContent.append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\">\n");
        excelContent.append("<Worksheet ss:Name=\"Bookings\">\n");
        excelContent.append("<Table>\n");
        
        // Header row
        excelContent.append("<Row>\n");
        excelContent.append("<Cell><Data ss:Type=\"String\">ID</Data></Cell>\n");
        excelContent.append("<Cell><Data ss:Type=\"String\">Customer Name</Data></Cell>\n");
        excelContent.append("<Cell><Data ss:Type=\"String\">Email</Data></Cell>\n");
        excelContent.append("<Cell><Data ss:Type=\"String\">Venue</Data></Cell>\n");
        excelContent.append("<Cell><Data ss:Type=\"String\">Wedding Date</Data></Cell>\n");
        excelContent.append("<Cell><Data ss:Type=\"String\">Guest Count</Data></Cell>\n");
        excelContent.append("<Cell><Data ss:Type=\"String\">Special Requests</Data></Cell>\n");
        excelContent.append("</Row>\n");
        
        // Data rows
        for (Booking booking : bookings) {
            excelContent.append("<Row>\n");
            excelContent.append("<Cell><Data ss:Type=\"Number\">").append(booking.getId()).append("</Data></Cell>\n");
            excelContent.append("<Cell><Data ss:Type=\"String\">").append(escapeXml(booking.getCustomerName())).append("</Data></Cell>\n");
            excelContent.append("<Cell><Data ss:Type=\"String\">").append(escapeXml(booking.getCustomerEmail())).append("</Data></Cell>\n");
            excelContent.append("<Cell><Data ss:Type=\"String\">").append(escapeXml(booking.getVenue())).append("</Data></Cell>\n");
            excelContent.append("<Cell><Data ss:Type=\"String\">").append(booking.getWeddingDate()).append("</Data></Cell>\n");
            excelContent.append("<Cell><Data ss:Type=\"Number\">").append(booking.getGuestCount()).append("</Data></Cell>\n");
            excelContent.append("<Cell><Data ss:Type=\"String\">").append(escapeXml(booking.getSpecialRequests() != null ? booking.getSpecialRequests() : "")).append("</Data></Cell>\n");
            excelContent.append("</Row>\n");
        }
        
        excelContent.append("</Table>\n");
        excelContent.append("</Worksheet>\n");
        excelContent.append("</Workbook>\n");
        
        response.getWriter().write(excelContent.toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
    
    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }

    /**
     * Edit Booking Form
     */
    @GetMapping("/bookings/{id}/edit")
    public String editBookingForm(@PathVariable Long id, Model model) {
        Optional<Booking> booking = hotelOwnerService.getBookingById(id);
        
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "hotel-owner/editBooking";
        } else {
            return "redirect:/hotel-owner/bookings?error=Booking not found";
        }
    }

    /**
     * Update Booking
     */
    @PostMapping("/bookings/{id}/edit")
    public String updateBooking(@PathVariable Long id, 
                               @RequestParam String customerName,
                               @RequestParam String customerEmail,
                               @RequestParam(required = false) String brideName,
                               @RequestParam(required = false) String groomName,
                               @RequestParam String venue,
                               @RequestParam(required = false) String preferredVenue,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weddingDate,
                               @RequestParam int guestCount,
                               @RequestParam(required = false) String specialRequests,
                               @RequestParam(required = false) String cateringPackage,
                               @RequestParam(required = false) String dietaryRequirements,
                               @RequestParam(required = false) String specialCateringRequests,
                               RedirectAttributes redirectAttributes, 
                               Model model) {
        
        Optional<Booking> existingBookingOpt = hotelOwnerService.getBookingById(id);
        if (existingBookingOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Booking not found");
            return "redirect:/hotel-owner/bookings";
        }
        
        // Get existing booking and update only the editable fields
        Booking existingBooking = existingBookingOpt.get();
        
        // Update editable fields
        existingBooking.setCustomerName(customerName);
        existingBooking.setCustomerEmail(customerEmail);
        existingBooking.setBrideName(brideName);
        existingBooking.setGroomName(groomName);
        existingBooking.setVenue(venue);
        existingBooking.setPreferredVenue(preferredVenue);
        existingBooking.setWeddingDate(weddingDate);
        existingBooking.setGuestCount(guestCount);
        existingBooking.setSpecialRequests(specialRequests);
        existingBooking.setCateringPackage(cateringPackage);
        existingBooking.setDietaryRequirements(dietaryRequirements);
        existingBooking.setSpecialCateringRequests(specialCateringRequests);
        
        // Keep existing values for fields not in the form:
        // - selectedMenuItems (managed by customer/catering manager)
        // - estimatedGuestCount (catering-specific)
        // - bookingStatus (managed by front desk)
        // - checkInTime (managed by front desk)
        // - checkOutTime (managed by front desk)
        // - frontDeskNotes (managed by front desk)
        
        hotelOwnerService.saveBooking(existingBooking);
        
        redirectAttributes.addFlashAttribute("success", "Booking updated successfully");
        return "redirect:/hotel-owner/bookings/" + id;
    }

    /**
     * Cancel Booking Confirmation (GET)
     */
    @GetMapping("/bookings/{id}/cancel-confirm")
    public String cancelBookingConfirm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Booking> booking = hotelOwnerService.getBookingById(id);
        
        if (booking.isPresent()) {
            hotelOwnerService.deleteBooking(id);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Booking not found");
        }
        
        return "redirect:/hotel-owner/bookings";
    }

    /**
     * Cancel Booking (POST - for form submissions)
     */
    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Booking> booking = hotelOwnerService.getBookingById(id);
        
        if (booking.isPresent()) {
            hotelOwnerService.deleteBooking(id);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Booking not found");
        }
        
        return "redirect:/hotel-owner/bookings";
    }

    /**
     * Download Booking Details as PDF
     */
    @GetMapping("/bookings/{id}/download-pdf")
    public void downloadBookingPDF(@PathVariable Long id, HttpServletResponse response) {
        try {
            Optional<Booking> bookingOpt = hotelOwnerService.getBookingById(id);
            
            if (bookingOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
                return;
            }
            
            Booking booking = bookingOpt.get();
            
            // Set response headers for PDF download
            response.setContentType("application/pdf");
            String filename = "Grand_Aura_Booking_" + booking.getId() + "_" + 
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
                .setFontSize(32)
                .setBold()
                .setFontColor(goldColor)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginBottom(5);
            document.add(title);
            
            com.itextpdf.layout.element.Paragraph subtitle = new com.itextpdf.layout.element.Paragraph("Luxury Wedding Venue")
                .setFontSize(14)
                .setFontColor(darkColor)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginBottom(5);
            document.add(subtitle);
            
            com.itextpdf.layout.element.Paragraph bookingTitle = new com.itextpdf.layout.element.Paragraph("BOOKING DETAILS")
                .setFontSize(20)
                .setBold()
                .setFontColor(darkColor)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginBottom(30);
            document.add(bookingTitle);
            
            // Booking ID
            com.itextpdf.layout.element.Paragraph bookingId = new com.itextpdf.layout.element.Paragraph("Booking #" + booking.getId())
                .setFontSize(12)
                .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginBottom(20);
            document.add(bookingId);
            
            // Horizontal line
            com.itextpdf.layout.element.LineSeparator line = new com.itextpdf.layout.element.LineSeparator(
                new com.itextpdf.kernel.pdf.canvas.draw.SolidLine(1f));
            line.setMarginBottom(20);
            document.add(line);
            
            // Customer Information Section
            com.itextpdf.layout.element.Paragraph customerHeader = new com.itextpdf.layout.element.Paragraph("CUSTOMER INFORMATION")
                .setFontSize(14)
                .setBold()
                .setFontColor(goldColor)
                .setMarginBottom(10);
            document.add(customerHeader);
            
            com.itextpdf.layout.element.Table customerTable = new com.itextpdf.layout.element.Table(new float[]{2, 3})
                .useAllAvailableWidth()
                .setMarginBottom(20);
            
            customerTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Customer Name:").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            customerTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph(booking.getCustomerName()))
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            
            customerTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Email:").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            customerTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph(booking.getCustomerEmail()))
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            
            if (booking.getBrideName() != null && !booking.getBrideName().isEmpty()) {
                customerTable.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph("Bride's Name:").setBold())
                    .setBackgroundColor(lightGray)
                    .setPadding(8)
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                customerTable.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(booking.getBrideName()))
                    .setPadding(8)
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            }
            
            if (booking.getGroomName() != null && !booking.getGroomName().isEmpty()) {
                customerTable.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph("Groom's Name:").setBold())
                    .setBackgroundColor(lightGray)
                    .setPadding(8)
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                customerTable.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(booking.getGroomName()))
                    .setPadding(8)
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            }
            
            document.add(customerTable);
            
            // Event Details Section
            com.itextpdf.layout.element.Paragraph eventHeader = new com.itextpdf.layout.element.Paragraph("EVENT DETAILS")
                .setFontSize(14)
                .setBold()
                .setFontColor(goldColor)
                .setMarginBottom(10);
            document.add(eventHeader);
            
            com.itextpdf.layout.element.Table eventTable = new com.itextpdf.layout.element.Table(new float[]{2, 3})
                .useAllAvailableWidth()
                .setMarginBottom(20);
            
            eventTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Wedding Date:").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            eventTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph(booking.getWeddingDate().format(java.time.format.DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"))))
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            
            eventTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Venue:").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            eventTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph(booking.getPreferredVenue() != null ? booking.getPreferredVenue() : booking.getVenue()))
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            
            eventTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph("Guest Count:").setBold())
                .setBackgroundColor(lightGray)
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            eventTable.addCell(new com.itextpdf.layout.element.Cell()
                .add(new com.itextpdf.layout.element.Paragraph(String.valueOf(booking.getGuestCount()) + " guests"))
                .setPadding(8)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            
            if (booking.getBookingStatus() != null) {
                eventTable.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph("Status:").setBold())
                    .setBackgroundColor(lightGray)
                    .setPadding(8)
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                eventTable.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(booking.getBookingStatus()))
                    .setPadding(8)
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
            }
            
            document.add(eventTable);
            
            // Catering Information (if available)
            if (booking.getCateringPackage() != null && !booking.getCateringPackage().isEmpty()) {
                com.itextpdf.layout.element.Paragraph cateringHeader = new com.itextpdf.layout.element.Paragraph("CATERING INFORMATION")
                    .setFontSize(14)
                    .setBold()
                    .setFontColor(goldColor)
                    .setMarginBottom(10);
                document.add(cateringHeader);
                
                com.itextpdf.layout.element.Table cateringTable = new com.itextpdf.layout.element.Table(new float[]{2, 3})
                    .useAllAvailableWidth()
                    .setMarginBottom(20);
                
                cateringTable.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph("Package:").setBold())
                    .setBackgroundColor(lightGray)
                    .setPadding(8)
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                cateringTable.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(booking.getCateringPackage()))
                    .setPadding(8)
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                
                if (booking.getDietaryRequirements() != null && !booking.getDietaryRequirements().isEmpty()) {
                    cateringTable.addCell(new com.itextpdf.layout.element.Cell()
                        .add(new com.itextpdf.layout.element.Paragraph("Dietary Requirements:").setBold())
                        .setBackgroundColor(lightGray)
                        .setPadding(8)
                        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                    cateringTable.addCell(new com.itextpdf.layout.element.Cell()
                        .add(new com.itextpdf.layout.element.Paragraph(booking.getDietaryRequirements()))
                        .setPadding(8)
                        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                }
                
                document.add(cateringTable);
            }
            
            // Special Requests
            if (booking.getSpecialRequests() != null && !booking.getSpecialRequests().isEmpty()) {
                com.itextpdf.layout.element.Paragraph requestsHeader = new com.itextpdf.layout.element.Paragraph("SPECIAL REQUESTS")
                    .setFontSize(14)
                    .setBold()
                    .setFontColor(goldColor)
                    .setMarginBottom(10);
                document.add(requestsHeader);
                
                com.itextpdf.layout.element.Paragraph requests = new com.itextpdf.layout.element.Paragraph(booking.getSpecialRequests())
                    .setBackgroundColor(lightGray)
                    .setPadding(15)
                    .setMarginBottom(20);
                document.add(requests);
            }
            
            // Footer
            document.add(new com.itextpdf.layout.element.LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine(1f)).setMarginTop(20));
            
            com.itextpdf.layout.element.Paragraph footer = new com.itextpdf.layout.element.Paragraph(
                    "Generated on: " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy")) + 
                    " • © 2025 Grand Aura Hotel • Confidential Document")
                .setFontSize(9)
                .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginTop(10);
            document.add(footer);
            
            // Close document
            document.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating PDF: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * Hotel Owner Profile Management
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        // For now, return a placeholder profile page
        // In a real application, you'd get the current hotel owner from authentication
        HotelOwner hotelOwner = new HotelOwner();
        hotelOwner.setOwnerName("Grand Aura Hotel Owner");
        hotelOwner.setEmail("hotel.owner@grandaura.com");
        hotelOwner.setHotelName("Grand Aura Hotel");
        hotelOwner.setDescription("Luxury wedding venue management");
        model.addAttribute("hotelOwner", hotelOwner);
        return "hotel-owner/profile";
    }

    /**
     * Update hotel owner profile
     */
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String ownerName, @RequestParam String email, Model model) {
        try {
            // Get all hotel owners and find the one we want to update
            // In a real application, you'd get the current user from authentication
            List<HotelOwner> allOwners = hotelOwnerService.getAllHotelOwners();
            HotelOwner existingOwner = null;
            
            // Find the existing owner (there should be only one in this demo)
            if (!allOwners.isEmpty()) {
                existingOwner = allOwners.get(0); // Get the first (and likely only) owner
            }
            
            HotelOwner updatedOwner;
            if (existingOwner != null) {
                // Update existing owner
                updatedOwner = existingOwner;
                updatedOwner.setOwnerName(ownerName);
                updatedOwner.setEmail(email);
                // Keep existing password hash and other fields
            } else {
                // Create new owner if none exists
                updatedOwner = new HotelOwner();
                updatedOwner.setOwnerName(ownerName);
                updatedOwner.setEmail(email);
                updatedOwner.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye"); // BCrypt hash for "password"
                updatedOwner.setEnabled(true);
                updatedOwner.setHotelName("Grand Aura Hotel");
                updatedOwner.setDescription("Luxury wedding venue management");
            }
            
            hotelOwnerService.saveHotelOwner(updatedOwner);
            model.addAttribute("success", "Profile updated successfully! You can now login with your new email address.");
            model.addAttribute("hotelOwner", updatedOwner);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            // Keep the original data on error
            HotelOwner fallbackOwner = new HotelOwner();
            fallbackOwner.setOwnerName("Grand Aura Hotel Owner");
            fallbackOwner.setEmail("hotel.owner@grandaura.com");
            fallbackOwner.setHotelName("Grand Aura Hotel");
            fallbackOwner.setDescription("Luxury wedding venue management");
            model.addAttribute("hotelOwner", fallbackOwner);
        }
        return "hotel-owner/profile";
    }
}
