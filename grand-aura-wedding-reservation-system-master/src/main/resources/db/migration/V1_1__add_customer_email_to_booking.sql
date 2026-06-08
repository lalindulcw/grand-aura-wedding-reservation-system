IF COL_LENGTH('booking', 'customer_email') IS NULL
BEGIN
    ALTER TABLE booking ADD customer_email VARCHAR(160) NOT NULL DEFAULT 'unknown@example.com';
END


