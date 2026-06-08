-- Clean up old bookings that don't have catering options
-- Run this script in your SQL Server Management Studio

USE GrandAuraDB;
GO

-- First, let's see what bookings we have
SELECT 
    id,
    customer_name,
    customer_email,
    wedding_date,
    venue,
    bride_name,
    groom_name,
    preferred_venue,
    catering_package,
    dietary_requirements,
    special_catering_requests,
    estimated_guest_count
FROM booking
ORDER BY id;

-- Delete bookings that don't have catering information
-- (where bride_name, groom_name, preferred_venue, and catering_package are all NULL)
DELETE FROM booking 
WHERE bride_name IS NULL 
  AND groom_name IS NULL 
  AND preferred_venue IS NULL 
  AND catering_package IS NULL 
  AND dietary_requirements IS NULL 
  AND special_catering_requests IS NULL 
  AND estimated_guest_count IS NULL;

-- Show remaining bookings after cleanup
SELECT 
    id,
    customer_name,
    customer_email,
    wedding_date,
    venue,
    bride_name,
    groom_name,
    preferred_venue,
    catering_package
FROM booking
ORDER BY id;

PRINT 'Old bookings without catering options have been deleted!';


