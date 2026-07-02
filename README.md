# Event Planning Dashboard

A comprehensive wedding event planning dashboard built with Spring Boot and Microsoft SQL Server.

## 🚀 Features

- **Wedding Management**: Create, update, and track wedding events
- **Event Scheduling**: Plan and manage wedding day schedules
- **Decoration Inventory**: Track decoration items and availability
- **Reminder System**: Set and manage important reminders
- **User Authentication**: Secure login with role-based access
- **Real-time Dashboard**: Live statistics and data visualization

## 🛠️ Technology Stack

- **Backend**: Spring Boot 2.7.14
- **Database**: Microsoft SQL Server
- **Frontend**: Thymeleaf + HTML/CSS/JavaScript
- **Security**: Spring Security
- **Build Tool**: Maven
- **Java Version**: 11+

## 📋 Prerequisites

- Java 11 or higher
- Microsoft SQL Server (Express, Developer, or Enterprise)
- Maven 3.6+

## 🚀 Quick Start

### 1. Database Setup
```sql
-- Run the setup script in SQL Server Management Studio
-- File: src/main/resources/sql-server-setup.sql
```

### 2. Configuration
Update `src/main/resources/application.properties` with your SQL Server details:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=EventPlanningDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run the Application
```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven
mvn spring-boot:run
```

### 4. Access the Application
- **URL**: http://localhost:8080
- **Login Credentials**:
  - Username: `coordinator` or `admin`
  - Password: `password`

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/event_planning_dashboard/
│   │       ├── config/          # Security and configuration
│   │       ├── controller/      # REST controllers
│   │       ├── model/           # JPA entities
│   │       ├── Repository/      # Data access layer
│   │       └── Service/         # Business logic
│   └── resources/
│       ├── static/              # Static assets (CSS, JS, images)
│       ├── templates/           # Thymeleaf templates
│       ├── application.properties
│       ├── data.sql            # Initial data
│       └── sql-server-setup.sql # Database setup script
├── docs/                       # Documentation
└── target/                     # Compiled classes
```

## 🗄️ Database Schema

The application uses the following main entities:
- **Users**: Authentication and authorization
- **Weddings**: Wedding event information
- **Event Schedules**: Wedding day timeline
- **Decoration Inventory**: Available decoration items
- **Reminders**: Important event reminders

## 🔧 Configuration

### Database Configuration
- **Auto Schema Creation**: Enabled via Hibernate
- **Data Initialization**: Automatic via `data.sql`
- **Connection Pool**: HikariCP

### Security Configuration
- **Authentication**: Form-based login
- **Authorization**: Role-based access control
- **Password Encoding**: Plain text (for development)

## 📊 API Endpoints

### Public Endpoints
- `GET /` - Login page
- `GET /login` - Login form
- `POST /login` - Authentication

### Protected Endpoints
- `GET /dashboard` - Main dashboard
- `GET /coordinator/dashboard` - Coordinator dashboard
- `POST /weddings` - Create/Update wedding
- `POST /schedules` - Add event schedule
- `POST /reminders` - Create reminder
- `POST /inventory` - Add inventory item

### Test Endpoints
- `GET /test-persistence` - Database connection test
- `GET /debug-weddings` - Debug wedding data

## 🚀 Deployment

### Development
```bash
mvn spring-boot:run
```

### Production
```bash
mvn clean package
java -jar target/event-planning-dashboard-0.0.1-SNAPSHOT.jar
```

## 📝 License

This project is licensed under the MIT License.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📞 Support

For support and questions, please contact the development team.

