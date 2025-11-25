package com.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecommendationWriterTest {
    RecommendationWriter recommendationWriter;
    Path tempFile;

    @BeforeEach
    void setUp() throws Exception {
        tempFile = Files.createTempFile("rec_test", ".txt");
        recommendationWriter = new RecommendationWriter(tempFile.toString());
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testOneUser() throws Exception {
        List<Movie> recommendations = List.of(
            new Movie("Inception", "I123", List.of("Sci-Fi", "Thriller")),
            new Movie("The Matrix", "TM456", List.of("Sci-Fi", "Action")),
            new Movie("Interstellar", "I789", List.of("Sci-Fi", "Drama"))
        );
        User user = new User("John Doe", "123456789", recommendations);
        
        recommendationWriter.writeRecommendations(user, recommendations);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("John Doe,123456789", lines.get(0));
        assertEquals("Inception,The Matrix,Interstellar", lines.get(1));   
    }

    @Test
    void testMultipleUsersWithMovies() throws Exception {
        List<Movie> recommendations1 = List.of(
            new Movie("Avatar", "A123", List.of("Sci-Fi", "Action")),
            new Movie("Titanic", "T456", List.of("Romance", "Drama"))
        );
        User user1 = new User("Alice Smith", "111222333", recommendations1);
        recommendationWriter.writeRecommendations(user1, recommendations1);

        List<Movie> recommendations2 = List.of(
            new Movie("Inception", "I789", List.of("Sci-Fi", "Thriller")),
            new Movie("The Matrix", "TM012", List.of("Sci-Fi", "Action")),
            new Movie("Interstellar", "I345", List.of("Sci-Fi", "Drama"))
        );
        User user2 = new User("Bob Johnson", "444555666", recommendations2);
        recommendationWriter.writeRecommendations(user2, recommendations2);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("Alice Smith,111222333", lines.get(0));
        assertEquals("Avatar,Titanic", lines.get(1));
        assertEquals("Bob Johnson,444555666", lines.get(2));
        assertEquals("Inception,The Matrix,Interstellar", lines.get(3));
    }

    @Test
    void testUserWithNoRecommendations() throws Exception {
        List<Movie> recommendations = List.of();
        User user = new User("Jane Doe", "987654321", recommendations);
        
        recommendationWriter.writeRecommendations(user, recommendations);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("Jane Doe,987654321", lines.get(0));
        assertEquals(1, lines.size()); // Only user line, no recommendations
    }

    @Test
    void testMultipleUsersWithNoRecommendations() throws Exception {
        List<Movie> recommendations1 = List.of();
        User user1 = new User("Charlie Brown", "111111111", recommendations1);
        recommendationWriter.writeRecommendations(user1, recommendations1);

        List<Movie> recommendations2 = List.of();
        User user2 = new User("Diana Prince", "222222222", recommendations2);
        recommendationWriter.writeRecommendations(user2, recommendations2);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("Charlie Brown,111111111", lines.get(0));
        assertEquals("Diana Prince,222222222", lines.get(1));
        assertEquals(2, lines.size()); // Only user lines, no recommendations
    }

    @Test
    void testMultipleUsersSomeWithRecommendations() throws Exception {
        List<Movie> recommendations1 = List.of(
            new Movie("Avatar", "A123", List.of("Sci-Fi", "Action"))
        );
        User user1 = new User("Eve Adams", "333333333", recommendations1);
        recommendationWriter.writeRecommendations(user1, recommendations1);

        List<Movie> recommendations2 = List.of();
        User user2 = new User("Frank Miller", "444444444", recommendations2);
        recommendationWriter.writeRecommendations(user2, recommendations2);

        List<Movie> recommendations3 = List.of(
            new Movie("Inception", "I789", List.of("Sci-Fi", "Thriller")),
            new Movie("The Matrix", "TM012", List.of("Sci-Fi", "Action"))
        );
        User user3 = new User("Grace Lee", "555555555", recommendations3);
        recommendationWriter.writeRecommendations(user3, recommendations3);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("Eve Adams,333333333", lines.get(0));
        assertEquals("Avatar", lines.get(1));
        assertEquals("Frank Miller,444444444", lines.get(2));
        assertEquals("Grace Lee,555555555", lines.get(3));
        assertEquals("Inception,The Matrix", lines.get(4));
        assertEquals(5, lines.size());
    }

}
