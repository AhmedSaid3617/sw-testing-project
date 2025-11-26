package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@DisplayName("Parser Integration Tests")
public class ParserTest {
    
    private String usersFilePath;
    private String moviesFilePath;
    private Parser parser;

    @BeforeEach
    void setUp() throws IOException {
        // Create temporary test files
        usersFilePath = "test_users.txt";
        moviesFilePath = "test_movies.txt";
    }

    private void createUsersFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(usersFilePath)) {
            writer.write(content);
        }
    }

    private void createMoviesFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(moviesFilePath)) {
            writer.write(content);
        }
    }

    private void cleanupFiles() {
        new File(usersFilePath).delete();
        new File(moviesFilePath).delete();
    }

    @Test
    @DisplayName("Integration: Parse valid users and movies files")
    void testParseValidUsersAndMoviesFiles() throws Exception {
        String moviesData = "The Matrix,TM123\nAction,Sci-Fi\n" +
                           "Inception,I456\nAction,Thriller\n";
        String usersData = "John Doe,123456789\nTM123,I456\n" +
                          "Jane Smith,12345678A\nTM123\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        assertNotNull(result);
        assertEquals(2, result.getMovies().size());
        assertEquals(2, result.getUsers().size());
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Parse and verify movies are accessible")
    void testParseMoviesAccessible() throws Exception {
        String moviesData = "Avatar,A789\nAdventure,Sci-Fi\n" +
                           "Titanic,T123\nDrama,Romance\n";
        String usersData = "Alice Brown,555555555\nA789\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        List<Movie> movies = result.getMovies();
        assertEquals(2, movies.size());
        assertTrue(movies.stream().anyMatch(m -> m.getId().equals("A789")));
        assertTrue(movies.stream().anyMatch(m -> m.getId().equals("T123")));
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Parse and verify users are accessible")
    void testParseUsersAccessible() throws Exception {
        String moviesData = "The Dark Knight,TDK123\nAction,Crime\n";
        String usersData = "Bob Johnson,987654321\nTDK123\n" +
                          "Charlie Davis,111111111\nTDK123\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        List<User> users = result.getUsers();
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Bob Johnson")));
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Charlie Davis")));
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Parse with single movie and single user")
    void testParseSingleMovieAndSingleUser() throws Exception {
        String moviesData = "Interstellar,I123\nSci-Fi,Drama\n";
        String usersData = "Emma Wilson,222222222\nINT123\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        assertEquals(1, result.getMovies().size());
        assertEquals(1, result.getUsers().size());
        assertEquals("Emma Wilson", result.getUsers().get(0).getName());
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Parse with multiple users liking same movie")
    void testParseMultipleUsersLikingSameMovie() throws Exception {
        String moviesData = "The Matrix,TM123\nAction,Sci-Fi\n";
        String usersData = "John Doe,123456789\nTM123\n" +
                          "Jane Smith,12345678A\nTM123\n" +
                          "Bob Johnson,987654321\nTM123\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        assertEquals(1, result.getMovies().size());
        assertEquals(3, result.getUsers().size());
        assertTrue(result.getUsers().stream()
            .allMatch(u -> u.getLikedMovies().contains("TM123")));
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Parse with user liking multiple movies")
    void testParseUserLikingMultipleMovies() throws Exception {
        String moviesData = "The Matrix,TM123\nAction,Sci-Fi\n" +
                           "Inception,I456\nAction,Thriller\n" +
                           "Avatar,A789\nAdventure,Sci-Fi\n";
        String usersData = "Grace Lee,333333333\nTM123,I456,A789\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        assertEquals(3, result.getMovies().size());
        assertEquals(1, result.getUsers().size());
        assertEquals(3, result.getUsers().get(0).getLikedMovies().size());
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Parse file with complex data")
    void testParseComplexData() throws Exception {
        String moviesData = "The Dark Knight,TDK123\nAction,Crime,Drama\n" +
                           "Forrest Gump,FG456\nDrama,Romance\n" +
                           "The Avengers,TA789\nAction,Adventure,Sci-Fi\n";
        String usersData = "Henry Martinez,444444444\nTDK123,TA789\n" +
                          "Iris Chen,555555555\nFG456\n" +
                          "Jack Wilson,66666666Z\nTDK123,FG456,TA789\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        assertEquals(3, result.getMovies().size());
        assertEquals(3, result.getUsers().size());
        
        // Verify movie details
        Movie firstMovie = result.getMovies().get(0);
        assertEquals("The Dark Knight", firstMovie.getTitle());
        assertEquals(3, firstMovie.getGenres().size());
        
        // Verify user details
        User thirdUser = result.getUsers().get(2);
        assertEquals("Jack Wilson", thirdUser.getName());
        assertEquals(3, thirdUser.getLikedMovies().size());
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Invalid movie format throws exception")
    void testParseInvalidMovieFormatThrowsException() throws Exception {
        String moviesData = "invalid movie title,IM123\nAction\n"; // lowercase start
        String usersData = "Kate Brown,777777777\nIM123\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        
        assertThrows(Exception.class, () -> {
            parser.parse();
        });
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Invalid user format throws exception")
    void testParseInvalidUserFormatThrowsException() throws Exception {
        String moviesData = "The Matrix,TM123\nAction,Sci-Fi\n";
        String usersData = "leonidas 888888888\nTM123\n"; // Missing comma
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        
        assertThrows(Exception.class, () -> {
            parser.parse();
        });
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Verify ParseResult contains both movies and users")
    void testParseResultContainsBothData() throws Exception {
        String moviesData = "Inception,I456\nAction,Thriller\n";
        String usersData = "Mia Johnson,999999999\nI456\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        assertNotNull(result);
        assertNotNull(result.getMovies());
        assertNotNull(result.getUsers());
        assertFalse(result.getMovies().isEmpty());
        assertFalse(result.getUsers().isEmpty());
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Parse returns correct ParseResult type")
    void testParseReturnsParseResultType() throws Exception {
        String moviesData = "Avatar,A789\nAdventure,Sci-Fi\n";
        String usersData = "Noah Lee,11111111Z\nA789\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        assertTrue(result instanceof ParseResult);
        assertEquals(1, result.getMovies().size());
        assertEquals(1, result.getUsers().size());
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: File not found throws IOException")
    void testNonExistentFileThrowsException() {
        assertThrows(IOException.class, () -> {
            new Parser("nonexistent_users.txt", "nonexistent_movies.txt");
        });
    }

    @Test
    @DisplayName("Integration: Parse empty files")
    void testParseEmptyFiles() throws Exception {
        createMoviesFile("");
        createUsersFile("");
        
        parser = new Parser(usersFilePath, moviesFilePath);
        
        // Should throw or handle gracefully depending on implementation
        assertThrows(Exception.class, () -> {
            parser.parse();
        });
        
        cleanupFiles();
    }

    @Test
    @DisplayName("Integration: Verify data consistency between parsers")
    void testDataConsistencyBetweenParsers() throws Exception {
        String moviesData = "The Matrix,TM123\nAction,Sci-Fi\n" +
                           "Inception,I456\nAction,Thriller\n";
        String usersData = "Oliver Brown,222222222\nTM123,I456\n";
        
        createMoviesFile(moviesData);
        createUsersFile(usersData);
        
        parser = new Parser(usersFilePath, moviesFilePath);
        ParseResult result = parser.parse();
        
        // Verify user's liked movie IDs exist in movies
        User user = result.getUsers().get(0);
        for (String likedMovieId : user.getLikedMovies()) {
            boolean movieExists = result.getMovies().stream()
                .anyMatch(m -> m.getId().equals(likedMovieId));
            assertTrue(movieExists, "Movie ID " + likedMovieId + " not found in movies list");
        }
        
        cleanupFiles();
    }
}

