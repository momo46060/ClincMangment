# Quick Reference

## 🚀 Quick Start Commands

### Windows (PowerShell)
```powershell
# Clone and run
git clone https://github.com/yourusername/ClincMangment.git
cd ClincMangment
.\mvnw.cmd spring-boot:run

# Build only
.\mvnw.cmd clean package -DskipTests

# Run tests
.\mvnw.cmd test
```

### Linux/macOS
```bash
git clone https://github.com/yourusername/ClincMangment.git
cd ClincMangment
chmod +x mvnw
./mvnw spring-boot:run
```

## 🌐 Important URLs

| Purpose | URL |
|---------|-----|
| **Application** | http://localhost:8080 |
| **H2 Console** | http://localhost:8080/h2-console |
| **Docs** | http://localhost:8080/docs |

## 🔐 Default Credentials

```
Username: admin
Password: admin123
```

## 📁 Important Directories

| Path | Purpose |
|------|---------|
| `src/main/kotlin/` | Source code |
| `src/main/resources/templates/` | HTML templates |
| `data/` | Database files (H2) |
| `docs/screenshots/` | Screenshots |

## 🛠️ Development Commands

### Build & Package
```bash
.\mvnw.cmd clean package -DskipTests      # Build JAR
.\mvnw.cmd clean install                   # Install to local repo
.\mvnw.cmd clean compile                   # Compile only
```

### Testing
```bash
.\mvnw.cmd test                            # Run all tests
.\mvnw.cmd test -Dtest=PatientServiceTests # Run specific test
.\mvnw.cmd clean test jacoco:report        # Coverage report
```

### Running
```bash
.\mvnw.cmd spring-boot:run                 # Dev mode
java -jar target/*.jar                     # Run JAR
java -jar target/*.jar --server.port=9090 # Custom port
```

### IDE
```bash
# IntelliJ: Right-click ClincMangmentApplication.kt → Run
# VS Code: Open file, click "Run" button
```

## 🗄️ Database Management

### H2 Console Access
```
URL: http://localhost:8080/h2-console
Driver: org.h2.Driver
JDBC URL: jdbc:h2:file:./data/clinicdb
Username: sa
Password: (leave empty)
```

### Common Database Operations
```sql
-- View all users
SELECT * FROM users;

-- View all patients
SELECT * FROM patient;

-- View visits
SELECT * FROM visit;

-- Reset H2 database
DELETE FROM user;
DELETE FROM patient;
DELETE FROM visit;
```

### Backup Database
```powershell
# Windows
Copy-Item -Path "data/clinicdb.mv.db" -Destination "data/clinicdb.mv.db.backup"

# Linux/macOS
cp data/clinicdb.mv.db data/clinicdb.mv.db.backup
```

## 📂 Key Files

| File | Purpose |
|------|---------|
| `README.md` | Project overview |
| `INSTALL.md` | Installation guide |
| `CONTRIBUTING.md` | Contribution guidelines |
| `CHANGELOG.md` | Version history |
| `LICENSE` | MIT License |
| `pom.xml` | Maven configuration |
| `src/main/resources/application.properties` | Application config |

## 🐳 Docker Commands

```bash
# Build image
docker build -t clinc-mangment .

# Run container
docker run -p 8080:8080 clinc-mangment

# Using Docker Compose
docker-compose up -d
docker-compose logs -f
docker-compose down
```

## 🔍 Troubleshooting

### Problem: Port 8080 in use
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/macOS
lsof -i :8080
kill -9 <PID>
```

### Problem: Maven wrapper error
```bash
# Windows
.\mvnw.cmd --version

# Linux/macOS
./mvnw --version
```

### Problem: Database locked
```bash
# Delete lock files and restart
del data/clinicdb.lock.db
del data/clinicdb.trace.db
```

### Problem: Java not found
```bash
# Set JAVA_HOME
# Windows: $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
# Linux: export JAVA_HOME=/usr/lib/jvm/java-17-openjdk

java -version  # Verify
```

## 📞 Support Resources

| Resource | Link |
|----------|------|
| **Documentation** | [README.md](README.md) |
| **Installation** | [INSTALL.md](INSTALL.md) |
| **Contributing** | [CONTRIBUTING.md](CONTRIBUTING.md) |
| **Changelog** | [CHANGELOG.md](CHANGELOG.md) |
| **Issues** | GitHub Issues |
| **Email** | support@clinicmangment.com |

## 🎯 Common Workflows

### Starting Development
```bash
cd ClincMangment
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

### Making Code Changes
```bash
# Edit files
.\mvnw.cmd spring-boot:run

# Auto-reloads on change (DevTools enabled)
```

### Testing Changes
```bash
.\mvnw.cmd test
.\mvnw.cmd clean test jacoco:report
```

### Preparing for Release
```bash
.\mvnw.cmd clean package -DskipTests
java -jar target/ClincMangment-*.jar
```

### Deploying to Production
```bash
.\mvnw.cmd clean package -DskipTests
java -Xmx1024m -jar target/ClincMangment-*.jar
```

## 🔗 Framework Versions

- **Spring Boot:** 3.5.7
- **Kotlin:** 1.9.25
- **Java:** 17+
- **Thymeleaf:** 3.x
- **H2 Database:** Latest
- **Maven:** 3.6+

## 💡 Pro Tips

1. **Auto-format code:** In IDE, press `Ctrl+Alt+L` (or `Cmd+Option+L` on macOS)
2. **Use hot reload:** Spring DevTools auto-reloads on file save
3. **Check logs:** Look in console output for errors and stack traces
4. **Backup before testing:** Always backup `data/clinicdb.mv.db` before testing
5. **Use Git branches:** Create feature branches for new development
6. **Write tests:** Ensure >80% code coverage for new features

## 📊 Project Stats

- **Language:** Kotlin
- **Backend:** Spring Boot
- **Frontend:** Thymeleaf
- **Database:** H2 (H2/MySQL compatible)
- **Build:** Maven
- **Lines of Code:** ~5000+ (includes templates)
- **Features:** 10+ core modules

---

**For detailed help, see the main [README.md](README.md) file**

*Last Updated: December 8, 2025*

