-- Ensure customer_email exists on either dbo.booking or dbo.Booking (SQL Server is case-insensitive by default, but guard both)

IF OBJECT_ID('dbo.booking', 'U') IS NOT NULL AND COL_LENGTH('dbo.booking', 'customer_email') IS NULL
BEGIN
    ALTER TABLE dbo.booking ADD customer_email VARCHAR(160) NOT NULL DEFAULT 'unknown@example.com';
END

IF OBJECT_ID('dbo.Booking', 'U') IS NOT NULL AND COL_LENGTH('dbo.Booking', 'customer_email') IS NULL
BEGIN
    ALTER TABLE dbo.Booking ADD customer_email VARCHAR(160) NOT NULL DEFAULT 'unknown@example.com';
END



