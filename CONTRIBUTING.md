# Contributing to ClincMangment
Thank you for contributing to ClincMangment! 🎉

- GitHub contributors page
- Release notes
- README.md contributors section
Contributors will be recognized in:

## Recognition

- Check existing issues for answers
- Email: support@clinicmangment.com
- Create an issue with your question

## Questions?

By contributing, you agree that your contributions will be licensed under its MIT License.

## License

5. Push tag: `git push origin v1.0.0`
4. Create a Git tag: `git tag -a v1.0.0 -m "Release 1.0.0"`
3. Create a release branch: `release/v1.0.0`
2. Update version in pom.xml
1. Update CHANGELOG.md with new features/fixes

## Release Process

```
del data\clinicdb.mv.db
# Reset database (delete .mv.db file)

copy data\clinicdb.mv.db data\clinicdb.mv.db.backup
# Backup database
```bash
### Database Issues

```
.\mvnw.cmd test -Dtest=YourTest
# Run specific test

java -version  # Should be 17+
# Check Java version
```bash
### Tests Fail

```
.\mvnw.cmd clean package -DskipTests
# Clean and rebuild
```bash
### Build Fails

## Common Issues & Solutions

```
}
    assertEquals(expectedPatient, result)
    // Assert
    
    val result = service.getPatientById(patientId)
    // Act
    
    every { repository.findById(patientId) } returns Optional.of(expectedPatient)
    val expectedPatient = Patient(id = patientId, name = "Ahmed")
    val patientId = 1L
    // Arrange
fun testShouldReturnPatientWhenIdExists() {
@Test
```kotlin
Example:

- Test both success and error cases
- Mock external dependencies
- Use descriptive test names: `testShouldReturnPatientWhenIdExists()`
- Write tests for all new features

### Testing

- Backup before testing destructive changes
- Document breaking changes
- Add migrations if schema changes
- Use JPA entity annotations

### Database Changes

```
}
        .orElseThrow { PatientNotFoundException(patientId) }
    return repository.findById(patientId)
fun getPatientById(patientId: Long): Patient {
 */
 * @throws PatientNotFoundException if patient doesn't exist
 * @return the patient object, or null if not found
 * @param patientId the unique patient identifier
 *
 * Retrieves a patient by their ID.
/**
```kotlin
Example:

- Add KDoc comments for public APIs
- Keep functions small and focused
- Use meaningful variable and function names
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)

### Kotlin Style

## Coding Standards

```
    └── kotlin/            # Unit tests
└── test/
│       └── templates/      # Thymeleaf views
│       ├── application.properties
│   └── resources/
│   │   └── config/         # Configuration
│   │   ├── repository/     # Data access
│   │   ├── model/          # Entity classes
│   │   ├── service/        # Business logic
│   │   ├── controller/     # Request handlers
│   ├── kotlin/com/clincmangment/
├── main/
src/
```

## Project Structure

```
.\mvnw.cmd test jacoco:report
# Run with coverage

.\mvnw.cmd test -Dtest=PatientServiceTests
# Run specific test class

.\mvnw.cmd test
# Run all tests
```bash

### Running Tests

```
# Access at http://localhost:8080

.\mvnw.cmd spring-boot:run
# Run the application

.\mvnw.cmd clean install
# Install dependencies

cd ClincMangment
git clone https://github.com/yourusername/ClincMangment.git
# Clone the repo
```bash

### Local Setup

- IDE (IntelliJ IDEA, VS Code, etc.)
- Git
- Maven 3.6+
- Java 17+
### Prerequisites

## Development Setup

   - Re-request review after changes
   - Discuss disagreements respectfully
   - Address feedback promptly
4. **Respond to reviews:**

   - Screenshots (if UI changes)
   - Testing performed
   - Type of change (bug fix, feature, etc.)
   - Fixes #(issue number) if applicable
   - Clear description of changes
3. **PR Description should include:**

   - Fill in the PR template
   - Select your branch
   - Click "New Pull Request"
   - Go to the original repository
2. **Create a Pull Request:**

   ```
   git push origin feature/your-feature-name
   ```bash
1. **Push to your fork:**

#### Submitting a Pull Request

   - Update CHANGELOG.md
   - Add comments for complex logic
   - Update README.md if needed
5. **Update documentation:**

   - Aim for >80% code coverage
   - Ensure all tests pass
   - Write unit tests for new features
4. **Add/update tests:**

   ```
   .\mvnw.cmd spring-boot:run
   .\mvnw.cmd test
   ```bash
3. **Test your changes:**

   - `chore:` - build/dependencies
   - `test:` - adding tests
   - `refactor:` - code refactoring
   - `style:` - formatting
   - `docs:` - documentation
   - `fix:` - bug fix
   - `feat:` - new feature
   Use conventional commit prefixes:
   
   ```
   git commit -m "docs: update API documentation"
   git commit -m "fix: resolve null pointer in visit creation"
   git commit -m "feat: add patient search by phone number"
   ```bash
2. **Write meaningful commits:**

   - Keep line length reasonable (120 chars max)
   - Format code with IDE auto-formatting
   - Use Kotlin style guide conventions
1. **Follow code style:**

#### Making Changes

   ```
   git checkout -b feature/your-feature-name
   ```bash
3. **Create a feature branch:**
   ```
   cd ClincMangment
   git clone https://github.com/yourusername/ClincMangment.git
   ```bash
2. **Clone your fork** locally:
1. **Fork the repository** to your GitHub account

#### Getting Started

### Pull Requests

5. List any alternative solutions you've considered
4. Explain why this feature would be useful
3. List any examples or use cases
2. Provide a detailed description of the enhancement
1. Use a clear, descriptive title

We welcome feature requests! Please:

### Suggesting Enhancements

5. **Screenshots or error logs** (if applicable)
   - Browser (if UI-related)
   - Spring Boot version
   - Java version (`java -version`)
   - OS (Windows, Linux, macOS)
4. **Environment details:**
3. **Expected behavior** vs **actual behavior**
2. **Steps to reproduce** the issue
1. **Clear title and description** of the bug

If you find a bug, please create an issue with:

### Reporting Bugs

## How to Contribute

- Report inappropriate behavior to maintainers
- Welcome people of all backgrounds
- Be respectful and inclusive

We are committed to providing a welcoming and inspiring community. Please read and adhere to our Code of Conduct:

## Code of Conduct

Thank you for your interest in contributing to ClincMangment! We appreciate your help in making this project better. Please follow the guidelines below.


