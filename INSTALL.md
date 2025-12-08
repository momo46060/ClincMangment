 # Installation & Setup Guide

Complete guide to install and run **ClincMangment** on your system.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Windows Installation](#windows-installation)
- [Linux/macOS Installation](#linuxmacos-installation)
- [Configuration](#configuration)
- [Verification](#verification)
- [Troubleshooting](#troubleshooting)
- [Docker Setup](#docker-setup)
- [Production Deployment](#production-deployment)

---

## Prerequisites

### Required Software

1. **Java Development Kit (JDK) 17 or higher**
   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
   - Or use: [OpenJDK](https://adoptium.net/)
   - Verify: `java -version`

2. **Git** (for cloning the repository)
   - Download: [Git](https://git-scm.com/)
   - Verify: `git --version`

3. **Maven 3.6+** (optional - Maven wrapper included)
   - Download: [Maven](https://maven.apache.org/download.cgi)
   - Verify: `mvn -version`

### System Requirements

- **RAM:** Minimum 2GB, recommended 4GB+
- **Disk Space:** 500MB for application + 100MB for database
- **OS:** Windows 7+, Linux, or macOS 10.12+
- **Browser:** Modern browser (Chrome, Firefox, Edge, Safari)

---

## Windows Installation

### Step 1: Clone the Repository

```powershell
# Create a workspace folder
mkdir C:\Development
cd C:\Development

# Clone the repository
git clone https://github.com/yourusername/ClincMangment.git
cd ClincMangment

# Verify files are downloaded
dir
```

### Step 2: Verify Java Installation

```powershell
# Check Java version
java -version

# Check Java home
$env:JAVA_HOME
```

**Expected Output:**
```
java version "17.x.x"
Java(TM) SE Runtime Environment
```

### Step 3: Run the Application

#### Method 1: Using Maven Wrapper (Easiest)

```powershell
# Navigate to project directory
cd C:\Development\ClincMangment

# Build and run (first time - may take 2-5 minutes)
.\mvnw.cmd spring-boot:run
```

**Expected Output:**
```
Started ClincMangmentApplication in X seconds
Tomcat started on port(s): 8080
```

#### Method 2: Build JAR and Run

```powershell
# Build the project
.\mvnw.cmd clean package -DskipTests

# Run the JAR file
java -jar target\ClincMangment-0.0.1-SNAPSHOT.jar
```

#### Method 3: Using IDE

**IntelliJ IDEA:**
1. Open project: `File` → `Open` → Select ClincMangment folder
2. Wait for Maven to download dependencies
3. Right-click `ClincMangmentApplication.kt` → `Run`

**VS Code:**
1. Open folder in VS Code
2. Install "Extension Pack for Java"
3. Open `ClincMangmentApplication.kt`
4. Click "Run" in the code editor

### Step 4: Access the Application

Open your browser and navigate to:
```
http://localhost:8080
```

You should see the ClincMangment login page.

---

## Linux/macOS Installation

### Step 1: Install Java (if not installed)

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk

# Verify
java -version
```

**macOS (using Homebrew):**
```bash
brew install openjdk@17
sudo ln -sfn /usr/local/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk

# Verify
java -version
```

**Red Hat/CentOS:**
```bash
sudo yum install java-17-openjdk java-17-openjdk-devel

# Verify
java -version
```

### Step 2: Clone and Setup

```bash
# Create development directory
mkdir -p ~/development
cd ~/development

# Clone repository
git clone https://github.com/yourusername/ClincMangment.git
cd ClincMangment

# Make Maven wrapper executable
chmod +x mvnw
```

### Step 3: Run Application

```bash
# Option 1: Using Maven wrapper
./mvnw spring-boot:run

# Option 2: Build JAR first
./mvnw clean package -DskipTests
java -jar target/ClincMangment-0.0.1-SNAPSHOT.jar
```

### Step 4: Access Application

```
http://localhost:8080
```

---

## Configuration

### Basic Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server port
server.port=8080

# Application name
spring.application.name=ClincMangment

# Database (H2)
spring.datasource.url=jdbc:h2:file:./data/clinicdb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### MySQL Configuration

Replace `application.properties` H2 configuration with:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/clinicdb?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=false
```

### Create MySQL Database

```sql
CREATE DATABASE clinicdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'clinic_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON clinicdb.* TO 'clinic_user'@'localhost';
FLUSH PRIVILEGES;
```

### Environment Variables

Create `.env` file in project root:

```
SERVER_PORT=8080
DATABASE_URL=jdbc:h2:file:./data/clinicdb
DATABASE_USERNAME=sa
DATABASE_PASSWORD=
```

Load in `application.properties`:
```properties
server.port=${SERVER_PORT}
spring.datasource.url=${DATABASE_URL}
```

---

## Verification

### Test Installation

```bash
# Run tests
.\mvnw.cmd test

# Build and package
.\mvnw.cmd clean package -DskipTests

# Check version
.\mvnw.cmd -v
```

### Access H2 Console (Development Only)

```
http://localhost:8080/h2-console
```

**Connection Details:**
- URL: `jdbc:h2:file:./data/clinicdb`
- Username: `sa`
- Password: (leave empty)

### Check Logs

```bash
# View recent logs
tail -f target/clinicdb.trace.db

# Or filter logs in console
.\mvnw.cmd spring-boot:run 2>&1 | findstr /I "ERROR WARN"
```

---

## Troubleshooting

### Issue: "Java not found"

**Solution:**
```bash
# Set JAVA_HOME environment variable
# Windows (PowerShell):
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Linux/macOS:
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Verify
echo $env:JAVA_HOME
```

### Issue: "Port 8080 already in use"

**Solution:**
```bash
# Change port in application.properties
server.port=8081

# Or kill process using port
# Windows (PowerShell):
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/macOS:
lsof -i :8080
kill -9 <PID>
```

### Issue: "Maven wrapper not found"

**Solution:**
```bash
# Ensure you're in project root directory
cd ClincMangment

# For Windows - use mvnw.cmd
.\mvnw.cmd --version

# For Linux/macOS - use mvnw
./mvnw --version
```

### Issue: "Database lock error"

**Solution:**
```bash
# Close application and backup database
copy data\clinicdb.mv.db data\clinicdb.mv.db.backup

# Delete lock file
del data\clinicdb.lock.db

# Delete trace file
del data\clinicdb.trace.db

# Restart application
.\mvnw.cmd spring-boot:run
```

### Issue: "Out of Memory" error

**Solution:**
```bash
# Increase heap size
# Windows:
$env:MAVEN_OPTS = "-Xmx1024m"

# Linux/macOS:
export MAVEN_OPTS="-Xmx1024m"

# Then run:
.\mvnw.cmd spring-boot:run
```

### Issue: "Tests fail with timeout"

**Solution:**
```bash
# Increase test timeout
.\mvnw.cmd test -DargLine="-Xmx512m" -Dorg.awaitility.timeout=10000
```

---

## Docker Setup

### Build Docker Image

```bash
# Create Dockerfile in project root (if not exists)
# Then build image
docker build -t clinc-mangment:latest .

# Run container
docker run -d \
  --name clinc-app \
  -p 8080:8080 \
  -v clinc-data:/app/data \
  clinc-mangment:latest
```

### Docker Compose

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  app:
    image: clinc-mangment:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:h2:file:/app/data/clinicdb
    volumes:
      - clinc-data:/app/data

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_DATABASE=clinicdb
      - MYSQL_ROOT_PASSWORD=root_password
    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  clinc-data:
  mysql-data:
```

Run with Docker Compose:
```bash
docker-compose up -d
```

---

## Production Deployment

### Pre-Deployment Checklist

- [ ] Update version in `pom.xml`
- [ ] Update `CHANGELOG.md`
- [ ] Run full test suite: `.\mvnw.cmd test`
- [ ] Build without tests: `.\mvnw.cmd clean package -DskipTests`
- [ ] Test JAR locally: `java -jar target/*.jar`
- [ ] Configure production database
- [ ] Set production environment variables
- [ ] Enable HTTPS/SSL
- [ ] Configure logging level for production
- [ ] Backup database

### Production Configuration

Set environment variables:

```bash
# Database
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/clinicdb
export SPRING_DATASOURCE_USERNAME=clinic_user
export SPRING_DATASOURCE_PASSWORD=secure_password

# Server
export SERVER_PORT=8080
export SPRING_JPA_HIBERNATE_DDL_AUTO=validate

# Logging
export LOGGING_LEVEL_ROOT=WARN
export LOGGING_LEVEL_COM_CLINCMANGMENT=INFO

# Security
export SERVER_SSL_ENABLED=true
export SERVER_SSL_KEY_STORE=classpath:keystore.jks
export SERVER_SSL_KEY_STORE_PASSWORD=keystore_password
```

### Run Production Application

```bash
java -Xmx1024m -Xms512m \
  -jar target/ClincMangment-0.0.1-SNAPSHOT.jar
```

### Monitoring

Set up monitoring for:
- Application logs
- Database connections
- Memory usage
- Uptime/availability
- Error rates

### Backup Strategy

```bash
# Daily backup script
#!/bin/bash
BACKUP_DIR="/backups/clinicdb"
mkdir -p $BACKUP_DIR
cp /app/data/clinicdb.mv.db $BACKUP_DIR/clinicdb_$(date +%Y%m%d_%H%M%S).mv.db

# Keep last 30 days
find $BACKUP_DIR -mtime +30 -delete
```

---

## Next Steps

1. **Access the application:** http://localhost:8080
2. **Create admin user** via database or admin panel
3. **Configure clinic settings** in admin dashboard
4. **Add staff users** (doctors, nurses)
5. **Start managing patients and visits**

## Support

- **Documentation:** See [README.md](README.md)
- **Issues:** Create issue on GitHub
- **Email:** support@clinicmangment.com

---

**Last Updated:** December 8, 2025

