package com.example.top_down;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import com.example.RecommendationWriter;
import com.example.User;
import com.example.Movie;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class RecommendationWriterIntegrationTest {

    private final String testFile = "tests_recommendations.txt";

    @AfterEach
    void cleanup() throws IOException {
        // Delete the file after each test to keep things clean
        Files.deleteIfExists(Paths.get(testFile));
    }

    @Test
    void testWriteRecommendations() throws Exception {

        User user = new User("Alice", "123456789", List.of("M001", "I001"));
        Movie m1 = new Movie("Inception", "I001", List.of("Action", "Thriller"));
        Movie m2 = new Movie("Matrix", "M001", List.of("Action", "Sci-Fi"));

        List<Movie> recommendations = List.of(m1, m2);

        // Create RecommendationWriter
        RecommendationWriter writer = new RecommendationWriter(testFile);

        // Write recommendations to file
        writer.writeRecommendations(user, recommendations);

        // Read file content to verify
        List<String> lines = Files.readAllLines(Paths.get(testFile));
        assertEquals(2, lines.size());
        assertEquals("Alice,123456789", lines.get(0));
        assertEquals("Inception,Matrix", lines.get(1));
    }

    @Test
    void testWriteRecommendationsEmptyList() throws Exception {
        User user = new User("Bob", "987654321", List.of());
        List<Movie> recommendations = List.of();

        RecommendationWriter writer = new RecommendationWriter(testFile);
        writer.writeRecommendations(user, recommendations);

        List<String> lines = Files.readAllLines(Paths.get(testFile));
        assertEquals(1, lines.size());
        assertEquals("Bob,987654321", lines.get(0));
    }
}
