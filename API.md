# API Documentation

Complete API reference for ClincMangment application endpoints.

## Base URL

```
http://localhost:8080
```

## Table of Contents

- [Authentication](#authentication)
- [Patient Management](#patient-management)
- [Visits Management](#visits-management)
- [Financial Management](#financial-management)
- [Chat System](#chat-system)
- [Admin Operations](#admin-operations)
- [Error Codes](#error-codes)

---

## Authentication

### Login
```
POST /login
```

**Request Body:**
```json
{
  "username": "doctor1",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 1,
    "username": "doctor1",
    "role": "DOCTOR"
  }
}
```

### Logout
```
GET /logout
```

**Response (200):**
```json
{
  "success": true,
  "message": "Logout successful"
}
```

---

## Patient Management

### Get All Patients
```
GET /patients
```

**Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)
- `sort` (optional): Sort field (default: id)

**Response (200):**
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "Ahmed",
      "lastName": "Mohamed",
      "phone": "+201234567890",
      "email": "ahmed@example.com",
      "dateOfBirth": "1985-04-12",
      "address": "Cairo, Egypt",
      "createdAt": "2025-12-08T10:30:00"
    }
  ],
  "totalElements": 150,
  "totalPages": 15,
  "currentPage": 0
}
```

### Create New Patient
```
POST /patients/new
```

**Request Body:**
```json
{
  "firstName": "Fatima",
  "lastName": "Ali",
  "phone": "+201098765432",
  "email": "fatima@example.com",
  "dateOfBirth": "1990-06-15",
  "address": "Alexandria, Egypt",
  "medicalHistory": "Hypertension, Diabetes"
}
```

**Response (201):**
```json
{
  "id": 151,
  "firstName": "Fatima",
  "lastName": "Ali",
  "phone": "+201098765432",
  "email": "fatima@example.com",
  "dateOfBirth": "1990-06-15",
  "address": "Alexandria, Egypt",
  "createdAt": "2025-12-08T11:00:00"
}
```

### Search Patients
```
GET /patients/search
```

**Parameters:**
- `q` (required): Search query (name, phone, email)
- `filter` (optional): Filter type (name, phone, email)

**Response (200):**
```json
{
  "results": [
    {
      "id": 1,
      "firstName": "Ahmed",
      "lastName": "Mohamed",
      "phone": "+201234567890",
      "email": "ahmed@example.com"
    }
  ],
  "totalResults": 5
}
```

### Get Patient Details
```
GET /patients/{id}
```

**Parameters:**
- `id` (required): Patient ID

**Response (200):**
```json
{
  "id": 1,
  "firstName": "Ahmed",
  "lastName": "Mohamed",
  "phone": "+201234567890",
  "email": "ahmed@example.com",
  "dateOfBirth": "1985-04-12",
  "address": "Cairo, Egypt",
  "medicalHistory": "No known allergies",
  "visits": [
    {
      "id": 1,
      "date": "2025-12-07T14:30:00",
      "symptoms": "Headache, Fever",
      "diagnosis": "Common cold"
    }
  ]
}
```

### Update Patient
```
PUT /patients/{id}
```

**Request Body:**
```json
{
  "firstName": "Ahmed",
  "lastName": "Mohamed",
  "phone": "+201234567890",
  "email": "ahmed.new@example.com",
  "address": "New Cairo, Egypt"
}
```

**Response (200):**
```json
{
  "id": 1,
  "firstName": "Ahmed",
  "lastName": "Mohamed",
  "updatedAt": "2025-12-08T11:30:00"
}
```

### Delete Patient
```
DELETE /patients/{id}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Patient deleted successfully"
}
```

---

## Visits Management

### Get All Visits
```
GET /visits
```

**Parameters:**
- `patientId` (optional): Filter by patient
- `doctorId` (optional): Filter by doctor
- `status` (optional): Filter by status (scheduled, completed, cancelled)

**Response (200):**
```json
{
  "visits": [
    {
      "id": 1,
      "patientId": 1,
      "doctorId": 5,
      "date": "2025-12-08T14:00:00",
      "symptoms": "Fever, Cough",
      "diagnosis": "Flu",
      "prescription": "Paracetamol 500mg, 3 times daily",
      "status": "completed"
    }
  ],
  "totalCount": 45
}
```

### Create New Visit
```
POST /visits/new
```

**Request Body:**
```json
{
  "patientId": 1,
  "doctorId": 5,
  "date": "2025-12-09T10:00:00",
  "symptoms": "Sore throat",
  "diagnosis": "Pharyngitis",
  "notes": "Recommend rest and fluids",
  "prescription": "Amoxicillin 500mg, 3 times daily for 10 days"
}
```

**Response (201):**
```json
{
  "id": 46,
  "patientId": 1,
  "doctorId": 5,
  "date": "2025-12-09T10:00:00",
  "createdAt": "2025-12-08T12:00:00"
}
```

### Get Current Visit
```
GET /visits/current
```

**Parameters:**
- `doctorId` (required): Doctor ID

**Response (200):**
```json
{
  "id": 1,
  "patientId": 1,
  "doctorId": 5,
  "startTime": "2025-12-08T14:00:00",
  "symptoms": "Fever, Cough",
  "notes": "Patient appears to have flu"
}
```

### Update Visit
```
PUT /visits/{id}
```

**Request Body:**
```json
{
  "symptoms": "Updated symptoms",
  "diagnosis": "Updated diagnosis",
  "prescription": "Updated prescription"
}
```

**Response (200):**
```json
{
  "id": 1,
  "patientId": 1,
  "updatedAt": "2025-12-08T14:30:00"
}
```

---

## Financial Management

### Get Financial Reports
```
GET /financial/reports
```

**Parameters:**
- `startDate` (optional): Start date (YYYY-MM-DD)
- `endDate` (optional): End date (YYYY-MM-DD)
- `type` (optional): Report type (revenue, expense, summary)

**Response (200):**
```json
{
  "startDate": "2025-11-01",
  "endDate": "2025-12-08",
  "totalRevenue": 5000.00,
  "totalExpenses": 1200.00,
  "netProfit": 3800.00,
  "breakdown": {
    "consultations": 3500.00,
    "procedures": 1500.00,
    "medicines": 800.00
  }
}
```

### Get Expenses
```
GET /expenses
```

**Parameters:**
- `category` (optional): Expense category
- `startDate` (optional): Start date
- `endDate` (optional): End date

**Response (200):**
```json
{
  "expenses": [
    {
      "id": 1,
      "category": "Supplies",
      "description": "Medical supplies",
      "amount": 500.00,
      "date": "2025-12-05",
      "createdBy": "admin"
    }
  ],
  "totalExpenses": 1200.00
}
```

### Add Expense
```
POST /expenses/add
```

**Request Body:**
```json
{
  "category": "Utilities",
  "description": "Monthly electricity bill",
  "amount": 450.00,
  "date": "2025-12-08"
}
```

**Response (201):**
```json
{
  "id": 2,
  "category": "Utilities",
  "description": "Monthly electricity bill",
  "amount": 450.00,
  "date": "2025-12-08",
  "createdAt": "2025-12-08T12:00:00"
}
```

---

## Chat System

### Get Chat Messages
```
GET /chat/messages
```

**Parameters:**
- `conversationId` (optional): Specific conversation
- `limit` (optional): Message limit (default: 50)

**Response (200):**
```json
{
  "messages": [
    {
      "id": 1,
      "from": "doctor1",
      "to": "nurse1",
      "message": "Patient in room 3 needs vitals check",
      "timestamp": "2025-12-08T14:30:00",
      "read": true
    }
  ]
}
```

### Send Message
```
POST /chat/message
```

**Request Body:**
```json
{
  "to": "nurse1",
  "message": "Please check patient vitals"
}
```

**Response (201):**
```json
{
  "id": 2,
  "from": "doctor1",
  "to": "nurse1",
  "message": "Please check patient vitals",
  "timestamp": "2025-12-08T14:35:00"
}
```

### Mark Message as Read
```
PUT /chat/messages/{id}/read
```

**Response (200):**
```json
{
  "success": true,
  "message": "Message marked as read"
}
```

---

## Admin Operations

### Get System Dashboard
```
GET /admin/dashboard
```

**Response (200):**
```json
{
  "totalPatients": 150,
  "totalDoctors": 8,
  "totalNurses": 5,
  "totalVisitsToday": 12,
  "totalVisitsThisMonth": 250,
  "systemHealth": "operational",
  "lastBackup": "2025-12-08T02:00:00"
}
```

### Get All Users
```
GET /admin/users
```

**Parameters:**
- `role` (optional): Filter by role
- `status` (optional): active, inactive, suspended

**Response (200):**
```json
{
  "users": [
    {
      "id": 1,
      "username": "admin",
      "email": "admin@clinic.com",
      "role": "ADMIN",
      "status": "active",
      "createdAt": "2025-01-01T00:00:00"
    }
  ],
  "totalUsers": 15
}
```

### Create User
```
POST /admin/users
```

**Request Body:**
```json
{
  "username": "newdoctor",
  "email": "doctor@clinic.com",
  "password": "SecurePass123!",
  "role": "DOCTOR",
  "firstName": "Ahmed",
  "lastName": "Mohamed"
}
```

**Response (201):**
```json
{
  "id": 10,
  "username": "newdoctor",
  "email": "doctor@clinic.com",
  "role": "DOCTOR",
  "createdAt": "2025-12-08T12:00:00"
}
```

### Update User
```
PUT /admin/users/{id}
```

**Response (200):**
```json
{
  "success": true,
  "message": "User updated successfully"
}
```

### Delete User
```
DELETE /admin/users/{id}
```

**Response (200):**
```json
{
  "success": true,
  "message": "User deleted successfully"
}
```

---

## Error Codes

| Code | Status | Description |
|------|--------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Invalid input or missing parameters |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource already exists |
| 500 | Server Error | Internal server error |
| 503 | Service Unavailable | Service temporarily unavailable |

### Error Response Format

```json
{
  "success": false,
  "error": "error_code",
  "message": "Human readable error message",
  "timestamp": "2025-12-08T12:00:00",
  "path": "/api/endpoint"
}
```

### Common Error Examples

**401 Unauthorized:**
```json
{
  "error": "UNAUTHORIZED",
  "message": "Authentication required. Please login."
}
```

**404 Not Found:**
```json
{
  "error": "NOT_FOUND",
  "message": "Patient with ID 999 not found"
}
```

**400 Bad Request:**
```json
{
  "error": "INVALID_INPUT",
  "message": "Invalid email format",
  "fields": {
    "email": "Invalid format"
  }
}
```

---

## Authentication Headers

All authenticated endpoints require:

```
Authorization: Bearer {token}
Content-Type: application/json
```

Example using cURL:
```bash
curl -H "Authorization: Bearer abc123xyz" \
     -H "Content-Type: application/json" \
     http://localhost:8080/patients
```

---

## Rate Limiting

- Default: 100 requests per minute per user
- Limits enforced per endpoint for high-load operations

---

## Pagination

Endpoints supporting pagination use:

```
?page=0&size=10&sort=id,desc
```

Response includes:
```json
{
  "content": [...],
  "totalElements": 150,
  "totalPages": 15,
  "currentPage": 0,
  "pageSize": 10,
  "hasNext": true,
  "hasPrevious": false
}
```

---

## Date Format

All dates use ISO 8601 format:
```
YYYY-MM-DDTHH:MM:SS
Example: 2025-12-08T14:30:00
```

---

**For more information, see [README.md](README.md)**

*Last Updated: December 8, 2025*

