# FX Deal Data Warehouse

This project implements a data warehouse for FX (Foreign Exchange) deals, designed to accept deal details, validate them, prevent duplicates, and persist them into a PostgreSQL database.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
  - [Prerequisites](#prerequisites)
  - [Building the Project](#building-the-project)
  - [Running with Docker Compose](#running-with-docker-compose)
- [Usage](#usage)
- [Database Schema](#database-schema)
- [Error Handling and Logging](#error-handling-and-logging)
- [Unit Testing](#unit-testing)
- [Future Enhancements](#future-enhancements)

## Features

- Accepts FX deal details with unique ID, currency ISO codes, timestamp, and amount.
- Validates the structure and content of incoming deal requests.
- Prevents the import of duplicate deal requests based on a unique deal ID.
- Persists valid and non-duplicate deals into a PostgreSQL database.
- Implements proper error handling and logging.
- Includes unit tests for core logic.

## Technologies Used

- **Java 17**: Programming language.
- **Maven**: Build automation tool.
- **PostgreSQL**: Relational database for storing FX deal data.
- **Docker & Docker Compose**: For containerization and orchestration of the database.
- **HikariCP**: High-performance JDBC connection pool.
- **SLF4J & Logback**: For logging.
- **JUnit 5 & Mockito**: For unit testing.

## Project Structure

```
fx-deal-warehouse/
├── pom.xml
├── docker-compose.yml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── progresssoft/
│   │   │           ├── Deal.java             # Data structure for an FX deal
│   │   │           ├── DealRepository.java   # Handles database interactions
│   │   │           ├── DealService.java      # Business logic for processing deals
│   │   │           └── DealValidator.java    # Validates deal data
│   │   └── resources/
│   │       └── schema.sql        # Database schema definition
│   └── test/
│       └── java/
│           └── com/
│               └── progresssoft/
│                   ├── DealServiceTest.java  # Unit tests for DealService
│                   └── DealValidatorTest.java # Unit tests for DealValidator
└── README.md
```

## Setup and Installation

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6.0 or higher
- Docker Desktop (includes Docker Engine and Docker Compose)

### Building the Project

Navigate to the `fx-deal-warehouse` directory and build the project using Maven:

```bash
cd fx-deal-warehouse
mvn clean install
```

This command will compile the Java code, run tests, and package the application.

### Running with Docker Compose

1.  **Start the PostgreSQL database:**
    Navigate to the `fx-deal-warehouse` directory and run:
    ```bash
    docker-compose up -d db
    ```
    This will start the PostgreSQL container in the background. The `schema.sql` will be automatically executed to create the `fx_deals` table.

2.  **Verify database health (optional):**
    You can check the logs of the database container:
    ```bash
    docker-compose logs db
    ```
    Look for messages indicating that the database is ready to accept connections.

## Usage

Currently, the application is a library with core logic. To use it, you would integrate `DealService` into your main application flow.

A sample usage might look like this (in a `main` method or a separate entry point):

```java
import com.progresssoft.Deal;
import com.progresssoft.DealRepository;
import com.progresssoft.DealService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MainApplication {
    public static void main(String[] args) {
        DealRepository dealRepository = new DealRepository();
        DealService dealService = new DealService(dealRepository);

        try {
            Deal deal1 = new Deal(UUID.randomUUID(), "USD", "EUR", LocalDateTime.now(), new BigDecimal("100.50"));
            dealService.processDeal(deal1);

            Deal deal2 = new Deal(UUID.randomUUID(), "GBP", "JPY", LocalDateTime.now(), new BigDecimal("15000.75"));
            dealService.processDeal(deal2);

            dealService.processDeal(deal1);

            Deal invalidDeal = new Deal(UUID.randomUUID(), "US", "EUR", LocalDateTime.now(), new BigDecimal("50.00"));
            dealService.processDeal(invalidDeal);

        } finally {
            dealRepository.close(); // Close the connection pool
        }
    }
}
```

To run this example, you would need to create a `MainApplication.java` file in `src/main/java/com/progresssoft` and then execute it.

## Database Schema

The `fx_deals` table has the following structure:

| Column Name            | Type         | Constraints          | Description                               |
| :--------------------- | :----------- | :------------------- | :---------------------------------------- |
| `deal_id`              | `UUID`       | `PRIMARY KEY`        | Unique identifier for the FX deal.        |
| `from_currency_iso_code` | `VARCHAR(3)` | `NOT NULL`           | ISO 4217 code of the "from" currency.     |
| `to_currency_iso_code`   | `VARCHAR(3)` | `NOT NULL`           | ISO 4217 code of the "to" currency.       |
| `deal_timestamp`       | `TIMESTAMP`  | `NOT NULL`           | Timestamp when the deal occurred.         |
| `deal_amount`          | `DECIMAL(18, 6)` | `NOT NULL`           | Amount of the deal in the ordering currency. |

## Error Handling and Logging

- The application uses SLF4J with Logback for logging.
- Validation errors (`IllegalArgumentException`) are caught and logged by `DealService`.
- Database errors (`SQLException`) are caught and logged by `DealService`.
- Duplicate deal attempts are logged as warnings and skipped.
- Unexpected exceptions are caught and logged.

## Unit Testing

Unit tests are provided for `DealValidator` and `DealService` using JUnit 5 and Mockito.

To run the tests:

```bash
cd fx-deal-warehouse
mvn test
```

## Future Enhancements

- **REST API**: Expose the deal processing functionality via a RESTful API (e.g., using Spring Boot).
- **File Processing**: Implement functionality to read deal details from a file (e.g., CSV, JSON) and process them in batches.
- **More Robust Validation**: Implement more comprehensive currency code validation (e.g., against a list of active ISO codes).
- **Asynchronous Processing**: For high-throughput scenarios, consider asynchronous processing of deals using message queues.
- **Monitoring**: Add metrics and monitoring capabilities.
- **Authentication/Authorization**: Secure the API endpoints if exposed.
