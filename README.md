# Task Management API

A comprehensive Spring Boot REST API for managing tasks following Domain-Driven Design (DDD) principles and Test-Driven Development (TDD) practices.

## Overview

This application provides a simplified backend for a Task Management System. It allows users to create, retrieve, update, and delete tasks. Each task has a unique ID, title, description, status, and due date.

**Architecture Pattern**: Domain-Driven Design (DDD)  
**Development Methodology**: Test-Driven Development (TDD)  
**Storage**: In-memory data store (no database required)

---

## Quick Start

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Installation & Setup

1. **Clone or download the project**
   ```bash
   cd Task-manager
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The API will be available at: `http://localhost:8080`

4. **Run tests**
   ```bash
   mvn test
   ```

---

## Project Structure

```
Task-manager/
├── src/
│   ├── main/java/com/example/taskmanager/
│   │   ├── model/
│   │   │   └── Task.java                 # Task entity with validation
│   │   ├── repository/
│   │   │   └── TaskRepository.java       # Data persistence layer
│   │   ├── service/
│   │   │   ├── TaskService.java          # Service interface
│   │   │   └── TaskServiceImpl.java       # Service implementation (business logic)
│   │   ├── controller/
│   │   │   └── TaskController.java       # REST API endpoints
│   │   └── exception/
│   │       ├── TaskNotFoundException.java  # Custom exception for not found
│   │       └── ValidationException.java    # Custom exception for validation
│   └── test/java/com/example/taskmanager/
│       ├── model/
│       │   └── TaskTest.java              # Unit tests for Task model
│       ├── repository/
│       │   └── TaskRepositoryTest.java    # Unit tests for Repository
│       ├── service/
│       │   └── TaskServiceTest.java       # Unit tests for Service
│       └── controller/
│           └── TaskControllerTest.java    # Integration tests for API endpoints
├── README.md                              # This file
└── pom.xml                                # Maven configuration
```

---

## REST API Endpoints

### 1. Create Task
**Request:**
```
POST /tasks
Content-Type: application/json

{
  "title": "Complete project",
  "description": "Finish the task manager implementation",
  "status": "PENDING",
  "dueDate": "2026-02-10"
}
```

**Response:** `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Complete project",
  "description": "Finish the task manager implementation",
  "status": "PENDING",
  "dueDate": "2026-02-10"
}
```

**Validation Rules:**
- `title` (required): Must not be null or empty
- `dueDate` (required): Must not be null and must be today or in the future

---

### 2. Get Task by ID
**Request:**
```
GET /tasks/{id}
```

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Complete project",
  "description": "Finish the task manager implementation",
  "status": "PENDING",
  "dueDate": "2026-02-10"
}
```

**Error Response:** `404 Not Found`
```json
{
  "error": "Task with id 'invalid-id' not found",
  "timestamp": "1707425892000"
}
```

---

### 3. Update Task
**Request:**
```
PUT /tasks/{id}
Content-Type: application/json

{
  "title": "Updated title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "dueDate": "2026-02-15"
}
```

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Updated title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "dueDate": "2026-02-15"
}
```

**Notes:**
- All fields in the request body are optional
- Only provided fields will be updated
- Validation errors return `400 Bad Request`

---

### 4. Delete Task
**Request:**
```
DELETE /tasks/{id}
```

**Response:** `204 No Content` (no body)

**Error Response:** `404 Not Found`
```json
{
  "error": "Task with id 'invalid-id' not found",
  "timestamp": "1707425892000"
}
```

---

### 5. List All Tasks
**Request:**
```
GET /tasks?status=PENDING&page=0&size=10
```

**Query Parameters:**
- `status` (optional): Filter by status (PENDING, IN_PROGRESS, or DONE)
- `page` (optional, default: 0): Page number for pagination
- `size` (optional, default: 10): Number of tasks per page

**Response:** `200 OK`
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "Complete project",
    "description": "Finish implementation",
    "status": "PENDING",
    "dueDate": "2026-02-10"
  },
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "title": "Review code",
    "description": "Code review",
    "status": "PENDING",
    "dueDate": "2026-02-12"
  }
]
```

**Examples:**
- Get all tasks: `GET /tasks`
- Get pending tasks: `GET /tasks?status=PENDING`
- Get completed tasks: `GET /tasks?status=DONE`
- Pagination: `GET /tasks?page=1&size=5`
- Filter and paginate: `GET /tasks?status=IN_PROGRESS&page=0&size=10`

---

## Task Model

### Task Entity

```java
public class Task {
    private String id;              // Auto-generated UUID
    private String title;            // Required, non-empty string
    private String description;      // Optional
    private Status status;           // PENDING (default), IN_PROGRESS, DONE
    private LocalDate dueDate;       // Required, today or future date
    
    public enum Status {
        PENDING,
        IN_PROGRESS,
        DONE
    }
}
```

### Status Values
- **PENDING**: Task is pending (default status for new tasks)
- **IN_PROGRESS**: Task is currently being worked on
- **DONE**: Task is completed

---

## Domain-Driven Design (DDD) Architecture

The application is organized into clear layers:

### 1. **Model Layer** (`model/`)
- **Task.java**: Core domain entity with built-in validation
- Encapsulates business rules and constraints
- Validates data on construction and modification

### 2. **Repository Layer** (`repository/`)
- **TaskRepository.java**: Data persistence abstraction
- Manages in-memory storage using HashMap
- Provides methods for CRUD operations
- Isolated from business logic

### 3. **Service Layer** (`service/`)
- **TaskService.java**: Service interface (abstraction)
- **TaskServiceImpl.java**: Business logic implementation
- Coordinates between controller and repository
- Handles transaction-like operations
- Throws domain-specific exceptions

### 4. **Controller Layer** (`controller/`)
- **TaskController.java**: HTTP request handler
- Translates HTTP requests to service calls
- Manages response status codes and error handling
- Implements pagination and filtering

### 5. **Exception Layer** (`exception/`)
- **TaskNotFoundException**: Thrown when task doesn't exist
- **ValidationException**: Thrown on validation failures
- Provides meaningful error messages

---

## Test-Driven Development (TDD) Coverage

### Unit Tests

#### **TaskTest.java** (13 test cases)
Tests the Task model with focus on validation:
- Constructor initialization
- Title validation (null, empty, valid)
- Due date validation (null, past, future, today)
- Status validation
- Description handling
- String representation

#### **TaskRepositoryTest.java** (12 test cases)
Tests repository CRUD operations:
- Save new and existing tasks
- Find by ID (existing and non-existing)
- Find all tasks
- Delete operations
- Existence checks
- Filtering by status
- Count and clear operations

#### **TaskServiceTest.java** (15 test cases)
Tests business logic:
- Task creation with default status
- ID generation
- Validation error handling
- Retrieval and error handling
- Update operations and validation
- Deletion with verification
- Sorting by due date
- Filtering by status

### Integration Tests

#### **TaskControllerTest.java** (16 test cases)
Tests REST API endpoints:
- POST /tasks: Create task (201)
- GET /tasks/{id}: Retrieve task (200, 404)
- PUT /tasks/{id}: Update task (200, 400, 404)
- DELETE /tasks/{id}: Delete task (204, 404)
- GET /tasks: List all tasks with pagination and filtering
  - No filter
  - Status filter
  - Pagination (page 0, page 1, beyond last page)

**Total Test Coverage**: 56 test cases

---

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=TaskServiceTest
```

### Run with coverage report
```bash
mvn test jacoco:report
```

### View test results
```bash
# Tests output in console during `mvn test`
# Test report available in: target/surefire-reports/
```

---

## Clean Code Practices

### Code Organization
- **Single Responsibility Principle**: Each class has one reason to change
- **Dependency Injection**: Constructor injection for loose coupling
- **Interfaces**: Service layer uses interface for abstraction
- **Exception Handling**: Custom exceptions with meaningful messages

### Naming Conventions
- Clear, meaningful variable and method names
- Consistent package structure following domain
- Descriptive test method names explaining what they test

### Error Handling
- Custom exceptions for different error scenarios
- Appropriate HTTP status codes (201, 200, 204, 400, 404)
- Error response JSON with message and timestamp
- Validation at model level (fail-fast approach)

### Documentation
- Javadoc comments on public methods
- Clear README with examples
- Inline comments where logic is complex
- Well-structured and readable code

---

## Bonus Features Implemented

### 1. **Pagination**
- Page number and size query parameters
- Proper boundary handling
- Default values (page=0, size=10)

### 2. **Filtering by Status**
- Filter tasks by status (PENDING, IN_PROGRESS, DONE)
- Combined with pagination
- Service-level filtering with sorting

### 3. **Date Validation**
- Ensures due dates are not in the past
- Accepts today's date as valid
- Validates at model construction

### 4. **Comprehensive Testing**
- 56 unit and integration test cases
- Mocking of dependencies
- Edge case handling
- Exception scenario testing

---

## Example Usage

### Using cURL

**Create a task:**
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Buy groceries",
    "description": "Milk, eggs, bread",
    "dueDate": "2026-02-15"
  }'
```

**Get a task:**
```bash
curl http://localhost:8080/tasks/{id}
```

**List all pending tasks with pagination:**
```bash
curl "http://localhost:8080/tasks?status=PENDING&page=0&size=5"
```

**Update a task:**
```bash
curl -X PUT http://localhost:8080/tasks/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "status": "DONE"
  }'
```

**Delete a task:**
```bash
curl -X DELETE http://localhost:8080/tasks/{id}
```

---

## Technology Stack

- **Framework**: Spring Boot
- **Language**: Java 11+
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **API Format**: REST, JSON

---

## Project Constraints Met

✅ REST API Endpoints (5 endpoints implemented)  
✅ Task Model with required fields and validation  
✅ Domain-Driven Design architecture (5 layers)  
✅ Test-Driven Development (56 test cases)  
✅ Clean code practices and error handling  
✅ In-memory data store (no database required)  
✅ Comprehensive README with setup instructions  
✅ Pagination implementation  
✅ Status filtering  
✅ Date validation (future dates only)

---

## Future Enhancements

- Add Spring Data JPA and database persistence
- Implement user authentication and authorization
- Add task priority levels
- Implement task categories/tags
- Add task comments and history tracking
- Implement task assignment to users
- Add email notifications for due dates
- WebSocket support for real-time updates
- API documentation with Swagger/OpenAPI

---

## License

This project is provided as-is for educational purposes.

---

## Support

For issues or questions, please refer to the test files for usage examples and the code comments for implementation details.

**Project Completed**: February 8, 2026  
**Status**: Ready for submission

