-- Fix database schema for new catering fields
-- Run this script in your SQL Server Management Studio

USE GrandAuraDB;
GO

-- Add new columns to booking table if they don't exist
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('booking') AND name = 'bride_name')
BEGIN
    ALTER TABLE booking ADD bride_name VARCHAR(100);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('booking') AND name = 'groom_name')
BEGIN
    ALTER TABLE booking ADD groom_name VARCHAR(100);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('booking') AND name = 'preferred_venue')
BEGIN
    ALTER TABLE booking ADD preferred_venue VARCHAR(100);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('booking') AND name = 'catering_package')
BEGIN
    ALTER TABLE booking ADD catering_package VARCHAR(100);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('booking') AND name = 'dietary_requirements')
BEGIN
    ALTER TABLE booking ADD dietary_requirements VARCHAR(500);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('booking') AND name = 'special_catering_requests')
BEGIN
    ALTER TABLE booking ADD special_catering_requests VARCHAR(500);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('booking') AND name = 'estimated_guest_count')
BEGIN
    ALTER TABLE booking ADD estimated_guest_count INT;
END

-- Insert a test booking
IF NOT EXISTS (SELECT * FROM booking WHERE customer_email = 'test@grandaura.com')
BEGIN
    INSERT INTO booking (customer_name, customer_email, guest_count, venue, wedding_date, special_requests, bride_name, groom_name, preferred_venue, catering_package, dietary_requirements, special_catering_requests, estimated_guest_count)
    VALUES ('Test Customer', 'test@grandaura.com', 150, 'Grand Ballroom', '2025-12-25', 'Test special requests', 'Jane', 'John', 'Grand Ballroom - 250 to 280 pax', 'Premium Package', 'Vegetarian options', 'Live cooking station', 150);
END

PRINT 'Database schema updated successfully!';
