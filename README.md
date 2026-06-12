# TripHive Backend

A full-featured backend application for an Airbnb-like hotel booking platform built with Spring Boot 4.0.1 and Java 17. The system supports dynamic pricing strategies, secure authentication with JWT, payment processing via Stripe, and comprehensive booking management.

## API Documentation

Explore all available endpoints through Swagger UI:

**Backend URL:**  


## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup & Installation](#setup--installation)
- [Environment Variables](#environment-variables)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Architecture & Design Patterns](#architecture--design-patterns)
- [Pricing Strategies](#pricing-strategies)
- [Contributing](#contributing)

## Features

### Core Functionality
- **User Management** - User registration, login, and profile management
- **Hotel Management** - Create, update, and manage hotel properties
- **Room Inventory** - Manage room availability and pricing
- **Booking System** - End-to-end booking workflow with payment integration
- **Guest Management** - Add and manage guests for bookings
- **Dynamic Pricing** - Multiple pricing strategies based on market conditions

### Security & Authentication
- JWT-based authentication and authorization
- Spring Security integration
- Password encryption and secure token handling
- Role-based access control

### Payment Processing
- Stripe integration for secure payments
- Payment processing and confirmation
- Transaction management

### API Documentation
- Swagger/OpenAPI documentation
- Interactive API explorer
- Request/response examples

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Framework** | Spring Boot 4.0.1 |
| **Language** | Java 17 |
| **Database** | PostgreSQL |
| **ORM** | Spring Data JPA / Hibernate |
| **Security** | Spring Security + JWT (JJWT 0.12.6) |
| **Payment** | Stripe API |
| **Documentation** | SpringDoc OpenAPI 3.0.0 |
| **Mapping** | ModelMapper 3.2.6 |
| **Build** | Maven |
| **Lombok** | Boilerplate reduction |

## Project Structure

```
src/main/java/com/example/AirBnb_Clone/
├── advices/              # Global exception handlers & advice
├── config/               # Spring configuration & beans
├── controller/           # REST API endpoints
├── dto/                  # Data Transfer Objects
├── entity/               # JPA entities (User, Hotel, Room, Guest, etc.)
├── enums/                # Enumeration classes
├── exceptions/           # Custom exceptions
├── repository/           # Data access layer (JPA Repositories)
├── security/             # JWT & security configurations
├── service/              # Service interfaces
├── serviceImpl/           # Service implementations
├── strategy/             # Pricing strategy implementations
├── util/                 # Utility classes
└── AirBnbCloneApplication.java  # Main application class

src/main/resources/
├── application.properties # Configuration properties
├── static/               # Static resources
└── templates/            # HTML templates
```

## Prerequisites

- **Java 17** or higher
- **PostgreSQL 12+**
- **Maven 3.6+**
- **Git**
- **Stripe Account** (for payment processing)

## Setup & Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd AirBnb-Clone
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE airbnb_clone;
```

### 4. Configure Environment Variables

Create a `.env` file or set system environment variables (see [Environment Variables](#environment-variables) section)

### 5. Build the Application

```bash
mvn clean build
```

### 6. Run the Application

```bash
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn package
java -jar target/AirBnb-Clone-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080` (or configured port)

## Environment Variables

Create a `.env` file in the project root or set these as system environment variables:

```properties
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/airbnb_clone
DB_USERNAME=postgres
DB_PASSWORD=your_password

# JWT Security
JWT_SECRET_KEY=your_very_long_secret_key_with_at_least_256_bits_for_HS256

# Payment Processing
STRIPE_API_KEY=sk_test_your_stripe_secret_key

# Frontend URL
FRONTEND_URL=http://localhost:8080
```

### Example for Development:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/airbnb_clone
export DB_USERNAME=postgres
export DB_PASSWORD=password123
export JWT_SECRET_KEY=myverysecureKeyWithAtLeast256BitsForHS256Algorithm
export STRIPE_API_KEY=sk_test_1234567890abcdefghijk
```

## API Documentation

### Swagger UI

Once the application is running, access the interactive API documentation:

```
http://localhost:8080/swagger-ui.html
```

### Main API Endpoints

#### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

#### Hotels
- `GET /api/hotels` - List all hotels
- `GET /api/hotels/{id}` - Get hotel details
- `POST /api/hotels` - Create new hotel
- `PUT /api/hotels/{id}` - Update hotel
- `DELETE /api/hotels/{id}` - Delete hotel

#### Rooms
- `GET /api/hotels/{hotelId}/rooms` - Get rooms for a hotel
- `GET /api/rooms/{id}` - Get room details
- `POST /api/rooms` - Create new room
- `PUT /api/rooms/{id}` - Update room
- `DELETE /api/rooms/{id}` - Delete room

#### Bookings
- `POST /api/bookings` - Create a booking
- `GET /api/bookings/{id}` - Get booking details
- `GET /api/bookings` - List user's bookings
- `PATCH /api/bookings/{id}/cancel` - Cancel booking

#### Payments
- `POST /api/bookings/{bookingId}/payments` - Process payment
- `GET /api/bookings/{bookingId}/payments` - Get payment status

#### Users
- `GET /api/users/{id}` - Get user profile
- `PUT /api/users/{id}` - Update user profile

## Database Schema

### Main Entities

**User**
- User ID, Email, Password, Name, Phone, Address, Role

**Hotel**
- Hotel ID, Name, City, Country, Address, Description, Manager (FK to User)

**Room**
- Room ID, Hotel ID (FK), Room Number, Type, Capacity, Base Price, Status

**Inventory**
- Inventory ID, Room ID (FK), Date, Available Count, Booked Count

**Booking**
- Booking ID, Hotel ID (FK), Room ID (FK), User ID (FK), Check-in, Check-out, Status

**Guest**
- Guest ID, Booking ID (FK), Name, Email, Phone

**Payment**
- Payment ID, Booking ID (FK), Amount, Status, Stripe Transaction ID

**HotelMinPrice**
- Min Price ID, Hotel ID (FK), Min Price, Last Updated

## Architecture & Design Patterns

### Design Patterns Used

1. **Strategy Pattern** - Dynamic pricing strategies
   - `BasePricingStrategy` - Base room rate
   - `OccupancyPricingStrategy` - Adjust based on occupancy
   - `UrgencyPricingStrategy` - Time-based pricing
   - `HolidayPricingStrategy` - Holiday surcharges
   - `SurgePricingStrategy` - Demand-based surge pricing

2. **Service Layer Pattern** - Clear separation between business logic and data access
3. **Repository Pattern** - Data access abstraction using Spring Data JPA
4. **DTO Pattern** - Data transfer objects for request/response handling
5. **Singleton Pattern** - Spring beans and services
6. **Facade Pattern** - Simplified interfaces through services

### Layered Architecture

```
┌─────────────────────────────────────┐
│      REST Controller Layer          │
│  (Handles HTTP requests/responses)  │
├─────────────────────────────────────┤
│      Service Layer                  │
│  (Business logic & orchestration)   │
├─────────────────────────────────────┤
│      Repository Layer               │
│  (Data access with JPA)             │
├─────────────────────────────────────┤
│      Database Layer                 │
│  (PostgreSQL)                       │
└─────────────────────────────────────┘
```

## Pricing Strategies

The system implements multiple pricing strategies to optimize revenue:

### 1. Base Pricing Strategy
Default room rate based on room type and configuration.

### 2. Occupancy-Based Pricing
Adjusts prices based on current occupancy rates:
- High occupancy → Higher prices
- Low occupancy → Lower prices (promotional)

### 3. Urgency-Based Pricing
Time-sensitive pricing:
- Bookings close to check-in date → Premium pricing
- Advance bookings → Standard pricing

### 4. Holiday Pricing Strategy
Special surcharges during peak holidays and seasons.

### 5. Surge Pricing Strategy
Demand-driven pricing based on booking velocity and market conditions.

### Pricing Service
The `PricingService` orchestrates all strategies:
```java
PricingService.calculatePrice(room, checkIn, checkOut) 
  → Applies all active strategies 
  → Returns optimized final price
```

## Booking Workflow

1. **Room Search** - User searches available rooms
2. **Room Selection** - User selects desired room
3. **Guest Details** - Add guest information
4. **Price Calculation** - System calculates dynamic price
5. **Booking Creation** - Booking record created with PENDING status
6. **Payment Processing** - Stripe payment processing
7. **Booking Confirmation** - Status updated to CONFIRMED
8. **Check-in** - Status updated to CHECKED_IN
9. **Check-out** - Status updated to CHECKED_OUT

## Key Classes & Responsibilities

| Class | Responsibility |
|-------|-----------------|
| `AirBnbCloneApplication` | Application entry point |
| `User` | User entity with roles and auth info |
| `Hotel` | Hotel property details |
| `Room` | Room configurations and pricing |
| `Booking` | Booking lifecycle management |
| `BookingService` | Booking operations and validation |
| `PricingService` | Dynamic price calculations |
| `AuthService` | Authentication and JWT token handling |
| `CheckoutService` | Payment checkout processing |
| `JwtService` | JWT token generation and validation |

## Security Features

- **JWT Authentication** - Stateless, token-based authentication
- **Password Hashing** - BCrypt password encryption
- **CORS Configuration** - Controlled cross-origin requests
- **Role-Based Access Control** - User, HotelManager, Admin roles
- **Input Validation** - DTO validation with constraints
- **Exception Handling** - Centralized error handling with advice

## Testing

Run unit tests:

```bash
mvn test
```

Run integration tests:

```bash
mvn verify
```

## Building for Production

### Build JAR Package

```bash
mvn clean package -P production
```

### Docker Deployment (if Dockerfile exists)

```bash
docker build -t airbnb-clone:latest .
docker run -p 8080:8080 --env-file .env airbnb-clone:latest
```

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running
- Check `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` in environment
- Ensure database exists: `CREATE DATABASE airbnb_clone;`

### JWT Token Errors
- Ensure `JWT_SECRET_KEY` is set and at least 256 bits
- Check token expiration in `JwtService`

### Stripe Payment Failures
- Verify `STRIPE_API_KEY` is correct and in test mode
- Check Stripe account balance and permissions

### Port Already in Use
```bash
# Change port in application.properties or via environment:
export SERVER_PORT=8081
```

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Stripe API Documentation](https://stripe.com/docs/api)
- [JWT Introduction](https://jwt.io)

## Contributing Guidelines

### Development Workflow

1. **Fork** the repository
2. **Create** feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** changes (`git commit -m 'Add amazing feature'`)
4. **Push** to branch (`git push origin feature/amazing-feature`)
5. **Open** Pull Request

### Code Standards

- Follow Java naming conventions (camelCase for variables, PascalCase for classes)
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Add comments for complex business logic
- Maintain consistent indentation (4 spaces)
- Write descriptive commit messages
- Keep methods small and focused on single responsibility
- Use Spring annotations appropriately
- Write unit tests for business logic

## License & Legal

This project is open-sourced software licensed under the [MIT License](https://opensource.org/licenses/MIT).

### Third-party Licenses

- **Spring Boot**: Apache License 2.0
- **Spring Data JPA**: Apache License 2.0
- **Spring Security**: Apache License 2.0
- **PostgreSQL Driver**: BSD License
- **Lombok**: MIT License
- **JJWT**: Apache License 2.0
- **Stripe Java SDK**: Apache License 2.0
- **ModelMapper**: Apache License 2.0
- **SpringDoc OpenAPI**: Apache License 2.0

## Acknowledgments & Credits

- **Spring Team**: For the comprehensive Spring Boot framework
- **Spring Security Team**: For robust authentication and authorization
- **PostgreSQL Team**: For reliable database management
- **Stripe Team**: For secure payment gateway integration
- **Open Source Community**: For inspiration and best practices

---

**Note**: This is a project developed for educational and learning purposes. For production deployment, additional security hardening, performance optimizations, and compliance measures may be required.

© 2026 Robiul Hassan. All rights reserved. Unauthorized copying, reproduction, or distribution of this project is prohibited.
