-- Create users table for authentication
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
BEGIN
    CREATE TABLE users (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        email VARCHAR(160) NOT NULL UNIQUE,
        password_hash VARCHAR(200) NOT NULL,
        enabled BIT NOT NULL DEFAULT 1
    );
END
