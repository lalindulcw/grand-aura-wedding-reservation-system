# SQL Server Migration Guide

This document outlines the changes made to migrate the Event Planning Dashboard from H2 database to Microsoft SQL Server.

## Changes Made

### 1. Dependencies (pom.xml)
- **Removed**: H2 database dependency
- **Kept**: Microsoft SQL Server JDBC driver (mssql-jdbc)

### 2. Database Configuration (application.properties)
- **Updated**: Database URL to SQL Server format
- **Updated**: Driver class name to SQL Server driver
- **Updated**: Hibernate dialect to SQL Server dialect
- **Removed**: H2 console configuration

### 3. Model Classes
Updated column definitions for SQL Server compatibility:
- **Wedding.java**: Changed `TEXT` to `NVARCHAR(MAX)` for `special_requirements`
- **DecorationInventory.java**: Changed `TEXT` to `NVARCHAR(MAX)` for `description`
- **EventSchedule.java**: Changed `TEXT` to `NVARCHAR(MAX)` for `description`
- **Reminder.java**: Changed `TEXT` to `NVARCHAR(MAX)` for `message`

### 4. Data Scripts
- **Updated**: Date format in data.sql from H2 `DATE 'YYYY-MM-DD'` to SQL Server `'YYYY-MM-DD'`
- **Removed**: H2-specific commands

## Prerequisites

### SQL Server Setup
1. **Install SQL Server** (Express, Developer, or Enterprise edition)
2. **Enable TCP/IP** protocol in SQL Server Configuration Manager
3. **Set port 1433** as the default port
4. **Enable SQL Server Authentication** (mixed mode)

### Database Creation
Run the provided setup script:
```sql
-- Execute src/main/resources/sql-server-setup.sql
-- This creates the EventPlanningDB database
```

## Configuration

### Database Connection
Update `src/main/resources/application.properties` with your SQL Server details:

```properties
# Database Configuration (Microsoft SQL Server)
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=EventPlanningDB;encrypt=true;trustServerCertificate=true
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.datasource.username=sa
spring.datasource.password=YourPassword123!
```

### Connection Parameters
- **Server**: `localhost:1433` (adjust if SQL Server is on different host/port)
- **Database**: `EventPlanningDB`
- **Username**: `sa` (or your preferred SQL Server user)
- **Password**: Update to match your SQL Server password
- **Encryption**: Enabled with `trustServerCertificate=true` for development

## Testing the Migration

### 1. Start SQL Server
Ensure SQL Server is running and accessible.

### 2. Create Database
Execute the setup script to create the database.

### 3. Update Configuration
Modify `application.properties` with your SQL Server credentials.

### 4. Run Application
```bash
mvn spring-boot:run
```

### 5. Verify
- Check application logs for successful database connection
- Access the application at `http://localhost:8080`
- Verify data is loaded correctly

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Verify SQL Server is running
   - Check port 1433 is open
   - Confirm TCP/IP is enabled

2. **Authentication Failed**
   - Verify username/password
   - Check SQL Server authentication mode
   - Ensure user has proper permissions

3. **Database Not Found**
   - Run the setup script to create the database
   - Check database name in connection URL

4. **Schema Creation Issues**
   - Verify user has DDL permissions
   - Check Hibernate dialect configuration

### Logs to Check
- Application startup logs
- SQL Server error logs
- Network connectivity logs

## Security Notes

- **Production**: Use strong passwords and proper authentication
- **Encryption**: Configure proper SSL certificates for production
- **Permissions**: Grant minimal required permissions to application user
- **Network**: Use secure network connections in production

## Rollback Plan

To revert to H2 database:
1. Restore original `pom.xml` with H2 dependency
2. Restore original `application.properties` configuration
3. Restore original model classes with `TEXT` column definitions
4. Restore original `data.sql` with H2 date format
