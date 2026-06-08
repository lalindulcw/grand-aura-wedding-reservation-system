# Database Setup Guide

## Microsoft SQL Server Setup

### 1. Install SQL Server
- Download and install SQL Server (Express, Developer, or Enterprise)
- Enable TCP/IP protocol in SQL Server Configuration Manager
- Set port 1433 as the default port
- Enable SQL Server Authentication (mixed mode)

### 2. Create Database
Run the setup script in SQL Server Management Studio:

```sql
-- File: src/main/resources/sql-server-setup.sql
CREATE DATABASE EventPlanningDB;
GO

USE EventPlanningDB;
GO
```

### 3. Configure Application
Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=EventPlanningDB;encrypt=true;trustServerCertificate=true
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Database Schema

The application automatically creates the following tables:

### Users Table
- `id` (Primary Key)
- `username` (Unique)
- `password`
- `role` (EVENT_COORDINATOR, ADMIN)

### Weddings Table
- `id` (Primary Key)
- `couple_names`
- `wedding_date`
- `venue`
- `guest_count`
- `budget`
- `theme_preference`
- `music_style`
- `status`
- `special_requirements`

### Event Schedules Table
- `id` (Primary Key)
- `wedding_id` (Foreign Key)
- `activity_time`
- `duration_minutes`
- `activity_title`
- `description`
- `location`

### Decoration Inventory Table
- `id` (Primary Key)
- `item_name`
- `category`
- `quantity_available`
- `quantity_reserved`
- `condition_status`
- `description`

### Reminders Table
- `id` (Primary Key)
- `wedding_id` (Foreign Key)
- `title`
- `reminder_date`
- `reminder_time`
- `type`
- `message`
- `priority`

## Initial Data

The application loads initial data from `src/main/resources/data.sql`:
- 2 user accounts (coordinator, admin)
- 8 sample weddings
- 20 decoration inventory items

## Troubleshooting

### Connection Issues
- Verify SQL Server is running
- Check port 1433 is open
- Confirm TCP/IP is enabled
- Verify username/password

### Schema Issues
- Check Hibernate dialect configuration
- Verify user has DDL permissions
- Review application logs for errors

### Data Loading Issues
- Check foreign key constraints
- Verify data.sql syntax
- Review SQL Server error logs


