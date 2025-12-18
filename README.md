# Movie Recommendation System

A Java-based movie recommendation system that generates personalized movie recommendations for users based on their liked movies' genres.

## Overview

This system parses movie and user data from text files, analyzes user preferences, and generates movie recommendations by matching genres. It's built as a Maven project with comprehensive unit and integration testing.

## Features

- **Genre-based Recommendations**: Recommends movies that share genres with movies the user has liked
- **Multiple User Support**: Processes recommendations for multiple users in batch
- **Data Validation**: Validates movie and user data integrity
- **File-based I/O**: Reads from text files and writes recommendations to output file

## Project Structure

```
movie-recommendation/
├── src/
│   ├── main/java/com/example/
│   │   ├── Main.java                    # Entry point
│   │   ├── Parser.java                  # File parsing coordinator
│   │   ├── MovieParser.java             # Movie data parser
│   │   ├── UserParser.java              # User data parser
│   │   ├── DataStore.java               # Data storage and retrieval
│   │   ├── Recommender.java             # Recommendation engine
│   │   ├── RecommendationWriter.java    # Output writer
│   │   ├── Movie.java                   # Movie model
│   │   ├── User.java                    # User model
│   │   └── ParseResult.java             # Parse result container
│   └── test/java/com/example/           # Test suites
├── movies.txt                            # Movie data input
├── users.txt                             # User data input
├── recommendations.txt                   # Generated output
└── pom.xml                               # Maven configuration
```

## Requirements

- Java 17 or higher
- Maven 3.6+

## Installation

Clone the repository:
```bash
git clone <repository-url>
cd movie-recommendation
```

## Input File Format

### movies.txt
```
Movie Title,MovieID
Genre1,Genre2,Genre3
Another Movie,AnotherID
Genre1,Genre2
```

Example:
```
The Matrix,TM001
Action,Sci-Fi,Thriller
Inception,I002
Action,Sci-Fi,Mystery
The Shawshank Redemption,TSR003
Drama
```

### users.txt
Format should include user name, ID, and liked movie IDs (refer to User and UserParser classes for specifics).

## Usage

### Building the Project

```bash
mvn clean compile
```

### Running the Application

```bash
mvn exec:java -Dexec.mainClass="com.example.Main"
```

The system will:
1. Parse `movies.txt` and `users.txt`
2. Generate recommendations for each user
3. Write results to `recommendations.txt`

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn clean test jacoco:report
```

View the coverage report at `target/site/jacoco/index.html`

## How It Works

1. **Parsing**: The system reads and validates movie and user data from text files
2. **Data Storage**: Parsed data is stored in a `DataStore` for efficient lookup
3. **Recommendation**: For each user:
   - Identifies genres from movies they've liked
   - Finds unwatched movies matching those genres
   - Returns the matching movies as recommendations
4. **Output**: Writes user information and their recommendations to a file

## Output Format

The `recommendations.txt` file contains:
```
User Name,UserID
Movie1,Movie2,Movie3
Another User,AnotherID
MovieA,MovieB
```

## Testing

The project includes comprehensive testing:
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **Data Flow Tests**: Validate data flow through the system
- **White Box Testing**: Coverage of code paths and branches

Test coverage reports are generated using JaCoCo.

## Dependencies

- JUnit 5.9.1 - Testing framework
- Mockito 5.8.0 - Mocking framework
- Maven Surefire 3.0.0-M7 - Test runner
- JaCoCo - Code coverage
