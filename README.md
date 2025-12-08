<div align="center">

# 🏥 ClincMangment

**A comprehensive Clinic Management System built with Kotlin and Spring Boot**

[Features](#-features) • [Quick Start](#-quick-start) • [Architecture](#-architecture) • [Screenshots](#-screenshots) • [Contributing](#-contributing)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green?logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.25-purple?logo=kotlin)](https://kotlinlang.org/)
[![Java](https://img.shields.io/badge/Java-17+-orange?logo=java)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Quick Start](#-quick-start)
- [Configuration](#-configuration)
- [API & Modules](#-api--modules)
- [Screenshots](#-screenshots)
- [Development](#-development)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

**ClincMangment** is a full-featured web-based clinic management system designed to streamline healthcare operations. It enables clinic administrators, doctors, nurses, and clinic owners to efficiently manage patients, visits, medical records, financials, and internal communications.

The application is built with:
- **Backend:** Kotlin + Spring Boot 3.5.7 with JPA/Hibernate
- **Frontend:** Thymeleaf templates (server-side rendering)
- **Database:** H2 (embedded for development)
- **Deployment:** Docker-ready with Maven wrapper

---

## ✨ Features

### 👤 Authentication & Authorization
- User login/logout system
- Role-based access control (Admin, Doctor, Nurse, Clinic Owner)
- Secure session management
- Custom user details service

### 🏥 Patient Management
- **Add new patients** with demographic information
- **Search patients** by name, phone, or ID
- **View patient profiles** with complete history
- **Edit patient records** (update contact, medical info)
- **Patient status tracking** (active, inactive, archived)

### 🩺 Visits & Clinical Records
- **Create and manage visits** with date, symptoms, diagnosis
- **Current visit view** for ongoing consultations
- **Edit visit records** and medical notes
- **Prescription management** with templates
- **Visit history** and clinical documentation

### 💊 Prescription Management
- Pre-designed prescription templates
- Create prescriptions for patients
- Template-based prescription generation
- Medication tracking

### 💰 Financial Management
- **Expense tracking** and categorization
- **Financial reports** and analytics
- **Invoice management**
- **Revenue tracking**
- **Cost analysis** by department/service

### 💬 Internal Chat
- Staff-to-staff messaging
- Real-time chat notifications
- Message history and search
- Department-wide communications

### ⚙️ Clinic Services
- **Service catalog management**
- Define clinic services (consultations, procedures, etc.)
- Service pricing and availability

### 📊 Admin Dashboard
- System-wide statistics
- User management
- Clinic configuration
- System monitoring

### 🎨 Role-Based Dashboards
- **Admin Dashboard:** System overview, user management, reporting
- **Doctor Dashboard:** Patient list, today's visits, prescriptions
- **Nurse Dashboard:** Patient care tasks, visit tracking
- **Clinic Owner Dashboard:** Financial reports, staff management

---

## 🛠️ Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Framework** | Spring Boot | 3.5.7 |
| **Language** | Kotlin | 1.9.25 |
| **Java** | OpenJDK | 17+ |
| **ORM** | Spring Data JPA / Hibernate | - |
| **Frontend** | Thymeleaf | 3.x |
| **Database** | H2 Database | - |
| **Build** | Maven | 3.6+ |
| **Server** | Tomcat | Embedded |
| **Serialization** | Jackson | - |

### Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot DevTools
- Jackson Kotlin Module
- MySQL Connector (for production)
- H2 Database (development)

---

## 📁 Project Structure

```
ClincMangment/
├── src/main/kotlin/com/clincmangment/
│   ├── ClincMangmentApplication.kt          # Main application entry point
│   ├── ServletInitializer.kt                # Servlet initialization for WAR packaging
│   ├── config/                              # Configuration classes
│   │   ├── SecurityConfig.kt
│   │   └── WebConfig.kt
│   ├── controller/                          # REST/Web controllers
│   │   ├── AdminController.kt
│   │   ├── DoctorController.kt
│   │   ├── NurseController.kt
│   │   ├── PatientController.kt
│   │   ├── VisitController.kt
│   │   ├── FinancialController.kt
│   │   ├── ExpenseController.kt
│   │   ├── ChatController.kt
│   │   ├── LoginController.kt
│   │   ├── PrescriptionController.kt
│   │   └── ClincController.kt
│   ├── service/                             # Business logic layer
│   │   ├── PatientService.kt
│   │   ├── VisitService.kt
│   │   ├── FinancialService.kt
│   │   ├── ChatService.kt
│   │   ├── ChatMessageService.kt
│   │   ├── PrescriptionService.kt
│   │   ├── DashboardService.kt
│   │   ├── CustomUserDetailsService.kt
│   │   └── ClinicService.kt
│   ├── model/                               # Entity models
│   │   ├── User.kt
│   │   ├── Patient.kt
│   │   ├── Visit.kt
│   │   ├── Prescription.kt
│   │   ├── Expense.kt
│   │   ├── ChatMessage.kt
│   │   ├── Clinic.kt
│   │   └── ClinicService.kt
│   ├── repository/                          # Data access layer (JPA)
│   │   ├── PatientRepository.kt
│   │   ├── UserRepository.kt
│   │   ├── VisitRepository.kt
│   │   ├── ChatMessageRepository.kt
│   │   └── ExpenseRepository.kt
│   └── utils/                               # Utility classes
│       ├── Constants.kt
│       └── Helpers.kt
├── src/main/resources/
│   ├── application.properties                # Application configuration
│   ├── templates/                           # Thymeleaf HTML templates
│   │   ├── layout.html                      # Base layout template
│   │   ├── login.html
│   │   ├── landing.html
│   │   ├── admin/
│   │   │   └── dashboard.html
│   │   ├── doctor/
│   │   │   ├── dashboard.html
│   │   │   ├── visits.html
│   │   │   ├── current_visit.html
│   │   │   └── clinic_settings.html
│   │   ├── nurse/
│   │   │   └── dashboard.html
│   │   ├── patients/
│   │   │   ├── new.html
│   │   │   ├── search.html
│   │   │   ├── view.html
│   │   │   └── found.html
│   │   ├── visits/
│   │   │   ├── new.html
│   │   │   ├── list.html
│   │   │   └── edit.html
│   │   ├── financial/
│   │   │   ├── add-expense.html
│   │   │   └── financial-report.html
│   │   ├── expenses/
│   │   │   └── expense-list.html
│   │   ├── chat/
│   │   │   └── chat.html
│   │   ├── fragments/
│   │   │   ├── header.html
│   │   │   ├── sidebar.html
│   │   │   └── chat-popup.html
│   │   └── error/
│   │       ├── 404.html
│   │       ├── 500.html
│   │       └── custom_error.html
│   └── static/                              # Static assets (CSS, JS, images)
├── data/
│   └── clinicdb.mv.db                       # H2 embedded database file
├── pom.xml                                  # Maven configuration
├── mvnw & mvnw.cmd                          # Maven wrapper
└── README.md                                # This file

```

---

## 🚀 Quick Start

### Prerequisites
- **Java 17 or higher** (JDK)
- **Maven 3.6+** (or use the included Maven wrapper)
- **Windows/Linux/macOS**

### Installation & Run

#### Option 1: Using Maven Wrapper (Recommended for Windows)

```powershell
# Clone the repository
git clone https://github.com/yourusername/ClincMangment.git
cd ClincMangment

# Run the application
.\mvnw.cmd spring-boot:run
```

#### Option 2: Build & Run JAR

```powershell
# Build the project
.\mvnw.cmd clean package -DskipTests

# Run the JAR
java -jar target/ClincMangment-0.0.1-SNAPSHOT.jar
```

#### Option 3: Using Docker (Optional)

```bash
docker build -t clinc-mangment .
docker run -p 8080:8080 clinc-mangment
```

### Access the Application

Once running, open your browser and navigate to:

```
http://localhost:8080
```

**Default credentials** (if configured):
- Username: `admin`
- Password: `admin123`

---

## ⚙️ Configuration

### application.properties

Key configuration options in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# Application Name
spring.application.name=ClincMangment

# Database Configuration (H2)
spring.datasource.url=jdbc:h2:file:./data/clinicdb
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console (Development)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Security (Optional)
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
```

### Switching to MySQL

To use MySQL instead of H2:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clinicdb
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

---

## 🔗 API & Modules

### Module Overview

| Module | Controller | Purpose |
|--------|-----------|---------|
| **Admin** | AdminController | System administration, user management |
| **Doctor** | DoctorController | Doctor-specific views and operations |
| **Nurse** | NurseController | Nurse dashboard and patient care tasks |
| **Patient** | PatientController | Patient CRUD operations, search |
| **Visit** | VisitController | Visit management and tracking |
| **Financial** | FinancialController | Financial reports and analytics |
| **Expense** | ExpenseController | Expense tracking and management |
| **Chat** | ChatController | Internal messaging system |
| **Prescription** | PrescriptionController | Prescription creation and management |
| **Authentication** | LoginController | User login/logout |

### Key Endpoints

```
GET  /                              Landing page
GET  /login                         Login page
POST /login                         Process login

GET  /admin/dashboard               Admin dashboard
GET  /doctor/dashboard              Doctor dashboard
GET  /nurse/dashboard               Nurse dashboard

GET  /patients                      List all patients
POST /patients/new                  Create new patient
GET  /patients/search               Search patients
GET  /patients/{id}                 View patient details

GET  /visits                        List visits
POST /visits/new                    Create visit
GET  /visits/{id}/edit              Edit visit
GET  /visits/current                Current visit

GET  /financial/reports             Financial reports
GET  /expenses                      View expenses
POST /expenses/add                  Add new expense

GET  /chat                          Chat interface
POST /chat/message                  Send message

GET  /h2-console                    H2 Database console (dev only)
```

---

## 📸 Screenshots

### 1. Login Page
![Login page](docs/screenshots/login.svg)

### 2. Doctor Dashboard
![Dashboard](docs/screenshots/dashboard.svg)

### 3. Patients Management
![Patients](docs/screenshots/patients.svg)

---

## 🔧 Development

### Build Commands

```powershell
# Clean and build
.\mvnw.cmd clean package

# Run tests
.\mvnw.cmd test

# Run application in dev mode
.\mvnw.cmd spring-boot:run

# Skip tests during build
.\mvnw.cmd package -DskipTests
```

### Code Structure Best Practices

- **Controllers:** Handle HTTP requests, delegate to services
- **Services:** Business logic and data manipulation
- **Repositories:** Data access layer (JPA)
- **Models:** Entity classes with JPA annotations
- **Templates:** Thymeleaf HTML views with fragments

### Logging

View logs in the console output. Adjust logging level in `application.properties`:

```properties
logging.level.com.clincmangment=DEBUG
logging.level.org.springframework.web=INFO
```

### Database Backup

Before making schema changes, backup your H2 database:

```powershell
Copy-Item -Path "data/clinicdb.mv.db" -Destination "data/clinicdb.mv.db.backup"
```

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create a feature branch** (`git checkout -b feature/amazing-feature`)
3. **Commit changes** (`git commit -m 'Add amazing feature'`)
4. **Push to branch** (`git push origin feature/amazing-feature`)
5. **Open a Pull Request**

### Code Guidelines

- Follow Kotlin style conventions
- Add comments for complex logic
- Write unit tests for new features
- Update README for major changes
- Keep commits atomic and well-described

### Reporting Issues

Found a bug? Please create an issue with:
- Clear description of the problem
- Steps to reproduce
- Expected vs actual behavior
- Environment details (OS, Java version, etc.)

---

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 📧 Contact & Support

For questions, suggestions, or issues:

- **GitHub Issues:** [Create an issue](https://github.com/yourusername/ClincMangment/issues)
- **Email:** support@clinicmangment.com
- **Documentation:** [Visit our wiki](https://github.com/yourusername/ClincMangment/wiki)

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Kotlin community
- All contributors and users

---

<div align="center">

**[⬆ back to top](#-clinc-mangment)**

Made with ❤️ by the ClincMangment Team

</div>

