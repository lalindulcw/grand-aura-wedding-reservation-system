-- Advanced cleanup script for old bookings without catering options
-- Run this script in your SQL Server Management Studio

USE GrandAuraDB;
GO

-- Show current booking count
SELECT COUNT(*) as 'Total Bookings Before Cleanup' FROM booking;

-- Show bookings that will be deleted (those without catering info)
SELECT 
    id,
    customer_name,
    customer_email,
    wedding_date,
    venue,
    'WILL BE DELETED' as status
FROM booking 
WHERE (bride_name IS NULL OR bride_name = '')
  AND (groom_name IS NULL OR groom_name = '')
  AND (preferred_venue IS NULL OR preferred_venue = '')
  AND (catering_package IS NULL OR catering_package = '')
  AND (dietary_requirements IS NULL OR dietary_requirements = '')
  AND (special_catering_requests IS NULL OR special_catering_requests = '')
  AND (estimated_guest_count IS NULL OR estimated_guest_count = 0);

-- Delete old bookings without catering information
DELETE FROM booking 
WHERE (bride_name IS NULL OR bride_name = '')
  AND (groom_name IS NULL OR groom_name = '')
  AND (preferred_venue IS NULL OR preferred_venue = '')
  AND (catering_package IS NULL OR catering_package = '')
  AND (dietary_requirements IS NULL OR dietary_requirements = '')
  AND (special_catering_requests IS NULL OR special_catering_requests = '')
  AND (estimated_guest_count IS NULL OR estimated_guest_count = 0);

-- Show remaining bookings after cleanup
SELECT COUNT(*) as 'Total Bookings After Cleanup' FROM booking;

-- Show the remaining bookings with their catering information
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
    'KEPT - Has Catering Info' as status
FROM booking
ORDER BY id;

PRINT 'Cleanup completed! Old bookings without catering options have been removed.';


